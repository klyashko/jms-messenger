package com.idea.tools.jms;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;
import com.idea.tools.utils.Assert;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.idea.tools.dto.ContentType.TEXT;
import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.utils.Checked.function;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ActiveMQConnectionStrategy implements ConnectionStrategy {

    @Override
    public Connection connect(Server server) throws JMSException {
        Assert.equals(server.getType(), ACTIVE_MQ, String.format("Unsupported server type %s", server.getType()));
        Assert.notNull(server.getConnectionType(), "Connection type must not be null");

        String url = String.format("%s://%s:%s", server.getConnectionType().getExtension(), server.getHost(), server.getPort());
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(new URI(url));
            Connection connection;
            if (isNotEmpty(server.getLogin())) {
                connection = factory.createConnection(server.getLogin(), server.getPassword());
            } else {
                connection = factory.createConnection();
            }
            Assert.notNull(connection, "Connection may not be obtained", IllegalStateException::new);
            return connection;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(String.format("Url is malformed <%s>", url));
        }
    }

    @Override
    public Queue createQueueDestination(QueueDto queue) {
        return new ActiveMQQueue(queue.getName());
    }

    @Override
    public Optional<MessageDto> map(Message message) {
        if (message instanceof ActiveMQTextMessage) {
            return Optional.ofNullable(mapTextMessage((ActiveMQTextMessage) message));
        }
        return Optional.empty();
    }

    @Override
    public List<QueueDto> getQueues(Connection connection) {
        if (!(connection instanceof ActiveMQConnection)) {
            return Collections.emptyList();
        }
        try {
            ActiveMQConnection activeMQConnection = (ActiveMQConnection) connection;
            activeMQConnection.start();
            DestinationSource destinationSource = activeMQConnection.getDestinationSource();
            return destinationSource.getQueues()
                                    .stream()
                                    .map(function(q -> {
                                        QueueDto dto = new QueueDto();
                                        dto.setName(q.getQueueName());
                                        return dto;
                                    }))
                                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private MessageDto mapTextMessage(ActiveMQTextMessage msg) {
        try {
            MessageDto dto = new MessageDto();
            dto.setJmsType(msg.getJMSType());
            dto.setTimestamp(msg.getTimestamp());
            dto.setType(TEXT);
            dto.setPayload(msg.getText());
            dto.setHeaders(msg.getProperties());
            return dto;
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
