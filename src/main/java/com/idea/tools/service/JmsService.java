package com.idea.tools.service;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;
import com.idea.tools.dto.ServerType;
import com.idea.tools.jms.ActiveMQConnectionStrategy;
import com.idea.tools.jms.ConnectionStrategy;
import lombok.SneakyThrows;

import javax.jms.*;
import java.util.*;

import static com.idea.tools.App.queueService;
import static com.idea.tools.App.serverService;
import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.utils.Checked.consumer;
import static com.idea.tools.utils.Checked.predicate;
import static com.idea.tools.utils.Utils.partitioningBy;
import static com.idea.tools.utils.Utils.toMap;
import static java.util.function.Function.identity;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class JmsService {

    private static final Map<ServerType, ConnectionStrategy> STRATEGIES = new HashMap<>();

    static {
        STRATEGIES.put(ACTIVE_MQ, new ActiveMQConnectionStrategy());
    }

    public boolean testConnection(Server server) {
        return connectionStrategy(server)
                .flatMap(strategy -> strategy.getConnectionFactory(server))
                .filter(predicate(factory -> {
                    try (Connection connection = connect(server, factory)) {
                        return connection != null;
                    }
                }))
                .isPresent();
    }

    public void send(MessageDto msg) {
        if (!validate(msg)) {
            return;
        }
        QueueDto queue = msg.getQueue();
        Server server = queue.getServer();

        connectionStrategy(server).ifPresent(strategy ->
                strategy.getConnectionFactory(server).ifPresent(consumer(factory -> {
                    try (Connection connection = connect(server, factory);
                         Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
                         MessageProducer producer = session.createProducer(strategy.createQueueDestination(queue))
                    ) {
                        Message jmsMessage = msg.getType().create(session, msg);
                        jmsMessage.setJMSType(msg.getJmsType());
                        jmsMessage.setJMSTimestamp(msg.getTimestamp());
                        producer.send(jmsMessage);
                    }
                })));
    }

    public List<MessageDto> receive(QueueDto queue) {
        return Optional.ofNullable(queue.getServer())
                       .flatMap(server -> connectionStrategy(server)
                               .flatMap(strategy -> strategy.getConnectionFactory(server)
                                                            .map(cf -> receive(server, strategy, cf, queue))
                               )
                       )
                       .orElseGet(Collections::emptyList);
    }

    public void refresh(List<Server> servers) {
        servers.forEach(server -> {
            Map<Boolean, List<QueueDto>> tmp = partitioningBy(server.getQueues(), QueueDto::isAddedManually);
            Map<String, QueueDto> toKeep = toMap(tmp.get(true), QueueDto::getName, identity());

            connectionStrategy(server)
                    .ifPresent(strategy -> {
                        strategy.getConnectionFactory(server).ifPresent(factory -> {
                            try (Connection connection = connect(server, factory)) {
                                List<QueueDto> serverQueues = strategy.getQueues(connection);

                                serverQueues.forEach(dto -> {
                                    toKeep.computeIfAbsent(dto.getName(), name -> {
                                        dto.setServer(server);
                                        queueService().persist(dto);
                                        return dto;
                                    });
                                });
                            } catch (JMSException e) {
                                e.printStackTrace();
                            }
                        });
                    });

            server.setQueues(new ArrayList<>(toKeep.values()));
            serverService().saveOrUpdate(server);
        });
//        serverService().update(servers);
    }

    private List<MessageDto> receive(Server server, ConnectionStrategy strategy, ConnectionFactory factory, QueueDto queue) {
        List<MessageDto> msgs = new ArrayList<>();
        try (Connection connection = connect(server, factory);
             Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
             QueueBrowser browser = session.createBrowser(strategy.createQueueDestination(queue))
        ) {
            connection.start();
            @SuppressWarnings("unchecked")
            Enumeration<Message> enumeration = browser.getEnumeration();
            while (enumeration.hasMoreElements()) {
                strategy.map(enumeration.nextElement()).ifPresent(msgs::add);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return msgs;
    }

    @SneakyThrows(JMSException.class)
    private Connection connect(Server server, ConnectionFactory cf) {
        if (isNotEmpty(server.getLogin())) {
            return cf.createConnection(server.getLogin(), server.getPassword());
        }
        return cf.createConnection();
    }

    private boolean validate(MessageDto msg) {
        return msg != null && msg.getQueue() != null && msg.getQueue().getServer() != null && msg.getType() != null;
    }

    private Optional<ConnectionStrategy> connectionStrategy(Server server) {
        return Optional.ofNullable(server).map(s -> STRATEGIES.get(s.getType()));
    }

}
