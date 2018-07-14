package com.idea.tools.jms;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;
import com.idea.tools.utils.Assert;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.utils.Checked.function;
import static com.idea.tools.utils.Utils.uri;

public class ActiveMQConnectionStrategy extends AbstractConnectionStrategy {

    @Override
    public Connection connect(Server server) throws JMSException {
        validate(server);
        Assert.equals(server.getType(), ACTIVE_MQ, String.format("Unsupported server type %s", server.getType()));

        String url = getUrlString(server);
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(uri(url));
        return connect(server, factory);
    }

    @Override
    public Optional<MessageDto> map(Message message) throws JMSException {
        if (message instanceof ActiveMQTextMessage) {
            return Optional.ofNullable(super.mapTextMessage((TextMessage) message));
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

}
