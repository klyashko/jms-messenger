package com.idea.tools.jms;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.idea.tools.dto.ContentType.TEXT;
import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.utils.Checked.function;

public class ActiveMQConnectionStrategy implements ConnectionStrategy {

    @Override
    public Optional<ConnectionFactory> getConnectionFactory(Server server) {
        return Optional.ofNullable(server)
                       .filter(s -> ACTIVE_MQ.equals(s.getType()) && s.getConnectionType() != null)
                       .map(s -> String.format("%s://%s:%s", s.getConnectionType().getExtension(), s.getHost(), s.getPort()))
                       .map(function(URI::new))
                       .map(ActiveMQConnectionFactory::new);
    }

    @Override
    public Queue createQueueDestination(QueueDto queue) {
        if (queue != null) {
            return new ActiveMQQueue(queue.getName());
        }
        return null;
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
