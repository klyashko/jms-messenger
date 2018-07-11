package com.idea.tools.service;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;
import com.idea.tools.dto.ServerType;
import com.idea.tools.jms.ActiveMQConnectionStrategy;
import com.idea.tools.jms.ArtemisConnectionStrategy;
import com.idea.tools.jms.ConnectionStrategy;
import com.idea.tools.utils.Assert;
import com.intellij.openapi.diagnostic.Logger;

import javax.jms.*;
import java.lang.IllegalStateException;
import java.util.*;

import static com.idea.tools.App.queueService;
import static com.idea.tools.App.serverService;
import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.dto.ServerType.ARTEMIS;
import static com.idea.tools.utils.Utils.partitioningBy;
import static com.idea.tools.utils.Utils.toMap;
import static java.util.function.Function.identity;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class JmsService {

    private static final Logger LOGGER = Logger.getInstance(JmsService.class);

    private static final Map<ServerType, ConnectionStrategy> STRATEGIES = new HashMap<>();

    static {
        STRATEGIES.put(ACTIVE_MQ, new ActiveMQConnectionStrategy());
        STRATEGIES.put(ARTEMIS, new ArtemisConnectionStrategy());
    }

    public void testConnection(Server server) throws Exception {
        try (Connection connection = connectionStrategy(server).connect(server)) {
            connection.start();
        }
    }

    public void send(MessageDto msg) throws Exception {
        Assert.notNull(msg.getQueue(), "Queue must not be null");
        Assert.notNull(msg.getQueue().getServer(), "Server must not be null");
        Assert.notNull(msg.getType(), "Message type must not be null");

        QueueDto queue = msg.getQueue();
        Server server = queue.getServer();

        ConnectionStrategy strategy = connectionStrategy(server);

        try (Connection connection = strategy.connect(server);
             Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(strategy.createQueueDestination(queue))
        ) {
            Message jmsMessage = msg.getType().create(session, msg);
            jmsMessage.setJMSType(msg.getJmsType());
            jmsMessage.setJMSTimestamp(msg.getTimestamp());
            producer.send(jmsMessage);
        }
    }

    public List<MessageDto> receive(QueueDto queue) throws Exception {
        Assert.notNull(queue.getServer(), "Server must not be null");

        Server server = queue.getServer();
        ConnectionStrategy strategy = connectionStrategy(server);
        List<MessageDto> msgs = new ArrayList<>();
        try (Connection connection = strategy.connect(server);
             Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
             QueueBrowser browser = session.createBrowser(strategy.createQueueDestination(queue))
        ) {
            connection.start();
            @SuppressWarnings("unchecked")
            Enumeration<Message> enumeration = browser.getEnumeration();
            while (enumeration.hasMoreElements()) {
                strategy.map(enumeration.nextElement()).ifPresent(msgs::add);
            }
        }
        return msgs;
    }

    public void refresh(List<Server> servers) {
        servers.forEach(server -> {
            Map<Boolean, List<QueueDto>> tmp = partitioningBy(server.getQueues(), QueueDto::isAddedManually);
            Map<String, QueueDto> toKeep = toMap(tmp.get(true), QueueDto::getName, identity());

            try {
                ConnectionStrategy strategy = connectionStrategy(server);
                try (Connection connection = strategy.connect(server)) {
                    strategy.getQueues(connection).forEach(dto ->
                            toKeep.computeIfAbsent(dto.getName(), name -> {
                                dto.setServer(server);
                                queueService().persist(dto);
                                return dto;
                            }));
                }
            } catch (Exception e) {
                LOGGER.error("An exception has been thrown during connecting to a server", e, server.getName());
            }

            server.setQueues(new ArrayList<>(toKeep.values()));
            serverService().saveOrUpdate(server);
        });
    }

    public boolean removeFromQueue(MessageDto messageDto, QueueDto queue) throws Exception {
        Assert.notNull(queue.getServer(), "Server must not be null");

        Server server = queue.getServer();
        ConnectionStrategy strategy = connectionStrategy(server);
        String selector = String.format("JMSMessageID='%s'", messageDto.getMessageID());
        try (Connection connection = strategy.connect(server);
             Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
             MessageConsumer consumer = session.createConsumer(strategy.createQueueDestination(queue), selector)
        ) {
            connection.start();
            return consumer.receive(100) != null;
        }
    }

    private ConnectionStrategy connectionStrategy(Server server) {
        ConnectionStrategy strategy = STRATEGIES.get(server.getType());
        Assert.notNull(strategy, String.format("Unsupported server type %s", server.getType()), IllegalStateException::new);
        return strategy;
    }

}
