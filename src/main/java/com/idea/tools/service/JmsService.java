package com.idea.tools.service;

import com.idea.tools.dto.*;
import com.idea.tools.jms.*;
import com.idea.tools.utils.Assert;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import javax.jms.*;
import java.lang.IllegalStateException;
import java.util.*;

import static com.idea.tools.dto.DestinationType.QUEUE;
import static com.idea.tools.dto.ServerType.*;
import static com.idea.tools.service.DestinationService.destinationService;
import static com.idea.tools.service.ServerService.serverService;
import static com.idea.tools.utils.Checked.consumer;
import static com.idea.tools.utils.Utils.partitioningBy;
import static com.idea.tools.utils.Utils.toMap;
import static java.util.function.Function.identity;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class JmsService {

	private static final Logger LOGGER = Logger.getInstance(JmsService.class);

	private static final Map<ServerType, ConnectionStrategy> STRATEGIES = new HashMap<>();

	static {
		STRATEGIES.put(ACTIVE_MQ, new ActiveMQConnectionStrategy());
		STRATEGIES.put(RABBIT_MQ, new RabbitMQConnectionStrategy());
		STRATEGIES.put(ARTEMIS, new ArtemisConnectionStrategy());
		STRATEGIES.put(HORNETQ, new HornetConnectionStrategy());
		STRATEGIES.put(KAFKA, new KafkaConnectionStrategy());
	}

	private final Project project;

	public JmsService(Project project) {this.project = project;}

	public static JmsService jmsService(Project project) {
		return ServiceManager.getService(project, JmsService.class);
	}

	public void testConnection(ServerDto server) throws Exception {
		try (Connection connection = connectionStrategy(server).connect(server)) {
			connection.start();
		}
	}

	public void send(MessageDto msg) throws Exception {
		Assert.notNull(msg.getDestination(), "Queue must not be null");
		Assert.notNull(msg.getDestination().getServer(), "Server must not be null");
		Assert.notNull(msg.getType(), "Message type must not be null");

		DestinationDto destination = msg.getDestination();
		ServerDto server = destination.getServer();

		ConnectionStrategy strategy = connectionStrategy(server);

		try (Connection connection = strategy.connect(server);
			 Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
			 MessageProducer producer = session.createProducer(destination.getType().createDestination(session, destination))
		) {
			Message jmsMessage = msg.getType().create(session, msg);
			jmsMessage.setJMSType(msg.getJmsType());
			jmsMessage.setJMSTimestamp(msg.getTimestamp());
			setMessageProperties(jmsMessage, msg.getHeaders());
			producer.send(jmsMessage);
		}
	}

	public List<MessageDto> receive(DestinationDto destination) throws Exception {
		Assert.notNull(destination.getServer(), "Server must not be null");
		Assert.equals(destination.getType(), QUEUE, "Only from queue message can be received");

		ServerDto server = destination.getServer();
		ConnectionStrategy strategy = connectionStrategy(server);
		List<MessageDto> msgs = new ArrayList<>();
		try (Connection connection = strategy.connect(server);
			 Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
			 QueueBrowser browser = strategy.browser(session, destination.getName())
		) {
			connection.start();
			@SuppressWarnings("unchecked")
			Enumeration<Message> enumeration = browser.getEnumeration();
			while (enumeration.hasMoreElements()) {
				strategy.map(enumeration.nextElement())
						.ifPresent(m -> {
							m.setDestination(destination);
							msgs.add(m);
						});
			}
		}
		return msgs;
	}

	public boolean removeFromQueue(MessageDto messageDto, DestinationDto destination) throws Exception {
		Assert.notNull(destination.getServer(), "Server must not be null");
		Assert.equals(destination.getType(), QUEUE, "Only from queue message can be deleted");

		ServerDto server = destination.getServer();
		ConnectionStrategy strategy = connectionStrategy(server);
		String selector = String.format("JMSMessageID='%s'", messageDto.getMessageID());
		try (Connection connection = strategy.connect(server);
			 Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
			 MessageConsumer consumer = session.createConsumer(session.createQueue(destination.getName()), selector)
		) {
			connection.start();
			return consumer.receive(100) != null;
		}
	}

	private ConnectionStrategy connectionStrategy(ServerDto server) {
		ConnectionStrategy strategy = STRATEGIES.get(server.getType());
		Assert.notNull(strategy, String.format("Unsupported server type %s", server.getType()), IllegalStateException::new);
		return strategy;
	}

	private void setMessageProperties(Message msg, List<HeaderDto> properties) {
		properties.forEach(consumer(h -> msg.setObjectProperty(h.getName(), h.getValue())));
	}

	public void refresh(List<ServerDto> servers) {
		servers.forEach(server -> {
			Map<Boolean, List<DestinationDto>> tmp = partitioningBy(server.getDestinations(), DestinationDto::isAddedManually);
			Map<String, DestinationDto> toKeep = toMap(tmp.get(true), DestinationDto::getName, identity());

			try {
				ConnectionStrategy strategy = connectionStrategy(server);
				try (Connection connection = strategy.connect(server)) {
					strategy.getDestinations(connection).forEach(dto ->
							toKeep.computeIfAbsent(dto.getName(), name -> {
								dto.setServer(server);
								destinationService(project).persist(dto);
								return dto;
							}));
				}
			} catch (Exception e) {
				LOGGER.error("An exception has been thrown during connecting to a server", e, server.getName());
			}

			server.setDestinations(new ArrayList<>(toKeep.values()));
			serverService(project).saveOrUpdate(server);
		});
	}

}
