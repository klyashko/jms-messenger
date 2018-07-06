package com.idea.tools.service;

import com.idea.tools.dto.MessageEntity;
import com.idea.tools.dto.Server;
import com.idea.tools.dto.ServerType;
import com.idea.tools.jms.ActiveMQConnectionStrategy;
import com.idea.tools.jms.ConnectionStrategy;
import com.idea.tools.utils.Cleaner;
import lombok.SneakyThrows;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.utils.Checked.consumer;
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
                .map(cf -> connect(server, cf))
                .isPresent();
    }

    public void send(MessageEntity msg) {
        if (!validate(msg)) {
            return;
        }
        Server server = msg.getQueue().getServer();

        connectionStrategy(server).ifPresent(strategy ->
                strategy.getConnectionFactory(server).ifPresent(consumer(cf -> {
                    try (Cleaner cleaner = new Cleaner()) {
                        Connection connection = cleaner.register(() -> connect(server, cf), Connection::close);
                        Session session = cleaner.register(() -> connection.createSession(false, AUTO_ACKNOWLEDGE), Session::close);
                        MessageProducer producer = cleaner.register(() -> session.createProducer(strategy.createDestination(msg)), MessageProducer::close);
                        Message jmsMessage = msg.getType().create(session, msg);
                        jmsMessage.setJMSType(msg.getJmsType());
                        jmsMessage.setJMSTimestamp(msg.getTimestamp());
                        producer.send(jmsMessage);
                    }
                })));
    }

    @SneakyThrows(JMSException.class)
    private Connection connect(Server server, ConnectionFactory cf) {
        if (isNotEmpty(server.getLogin())) {
            return cf.createConnection(server.getLogin(), server.getPassword());
        }
        return cf.createConnection();
    }

    private boolean validate(MessageEntity msg) {
        return msg != null && msg.getQueue() != null && msg.getQueue().getServer() != null && msg.getType() != null;
    }

    private Optional<ConnectionStrategy> connectionStrategy(Server server) {
        return Optional.ofNullable(server).map(s -> STRATEGIES.get(s.getType()));
    }

}
