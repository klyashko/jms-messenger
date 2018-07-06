package com.idea.tools.service;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;
import com.idea.tools.dto.ServerType;
import com.idea.tools.jms.ActiveMQConnectionStrategy;
import com.idea.tools.jms.ConnectionStrategy;
import com.idea.tools.utils.Cleaner;
import lombok.SneakyThrows;

import javax.jms.*;
import java.util.*;

import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.utils.Checked.consumer;
import static com.idea.tools.utils.Checked.predicate;
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
                .filter(predicate(cf -> {
                    try (Cleaner cleaner = new Cleaner()) {
                        return connection(server, cf, cleaner) != null;
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
                strategy.getConnectionFactory(server).ifPresent(consumer(cf -> {
                    try (Cleaner cleaner = new Cleaner()) {
                        Connection connection = connection(server, cf, cleaner);
                        Session session = session(connection, cleaner);
                        MessageProducer producer = producer(session, strategy, queue, cleaner);
                        Message jmsMessage = msg.getType().create(session, msg);
                        jmsMessage.setJMSType(msg.getJmsType());
                        jmsMessage.setJMSTimestamp(msg.getTimestamp());
                        producer.send(jmsMessage);
                    }
                })));
    }

    public List<MessageDto> receive(QueueDto queue) {
        List<MessageDto> msgs = new ArrayList<>();
        Optional.ofNullable(queue.getServer())
                .ifPresent(server ->
                        connectionStrategy(server).ifPresent(
                                strategy -> strategy.getConnectionFactory(server).ifPresent(consumer(cf -> {
                                    try (Cleaner cleaner = new Cleaner()) {
                                        Connection connection = connection(server, cf, cleaner);
                                        Session session = session(connection, cleaner);
                                        QueueBrowser browser = browser(session, strategy, queue, cleaner);
                                        connection.start();
                                        @SuppressWarnings("unchecked")
                                        Enumeration<Message> enumeration = browser.getEnumeration();
                                        while (enumeration.hasMoreElements()) {
                                            strategy.map(enumeration.nextElement()).ifPresent(msgs::add);
                                        }
                                    }
                                }))));
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

    private Connection connection(Server server, ConnectionFactory cf, Cleaner cleaner) {
        return cleaner.register(() -> connect(server, cf), Connection::close);
    }

    private Session session(Connection connection, Cleaner cleaner) {
        return cleaner.register(() -> connection.createSession(false, AUTO_ACKNOWLEDGE), Session::close);
    }

    private MessageProducer producer(Session session, ConnectionStrategy strategy, QueueDto queue, Cleaner cleaner) {
        return cleaner.register(() -> session.createProducer(strategy.createQueueDestination(queue)), MessageProducer::close);
    }

    private QueueBrowser browser(Session session, ConnectionStrategy strategy, QueueDto queue, Cleaner cleaner) {
        return cleaner.register(() -> session.createBrowser(strategy.createQueueDestination(queue)), QueueBrowser::close);
    }

}
