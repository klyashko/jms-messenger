package com.idea.tools.jms;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.DestinationType;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.ServerDto;
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

import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.utils.Checked.function;
import static com.idea.tools.utils.Utils.toList;
import static com.idea.tools.utils.Utils.uri;

public class ActiveMQConnectionStrategy extends AbstractConnectionStrategy {

    @Override
    public Connection connect(ServerDto server) throws JMSException {
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
    public List<DestinationDto> getDestinations(Connection connection) {
        if (!(connection instanceof ActiveMQConnection)) {
            return Collections.emptyList();
        }
        try {
            ActiveMQConnection activeMQConnection = (ActiveMQConnection) connection;
            activeMQConnection.start();
            DestinationSource destinationSource = activeMQConnection.getDestinationSource();
            List<DestinationDto> queues = toList(destinationSource.getQueues(), function(q -> {
                DestinationDto dto = new DestinationDto();
                dto.setName(q.getQueueName());
                dto.setType(DestinationType.QUEUE);
                return dto;
            }));
            List<DestinationDto> topics = toList(destinationSource.getTopics(), function(t -> {
                DestinationDto dto = new DestinationDto();
                dto.setName(t.getTopicName());
                dto.setType(DestinationType.TOPIC);
                return dto;
            }));
            queues.addAll(topics);
            return queues;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

}
