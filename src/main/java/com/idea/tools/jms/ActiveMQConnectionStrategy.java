package com.idea.tools.jms;

import com.idea.tools.dto.MessageEntity;
import com.idea.tools.dto.Server;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import java.net.URI;
import java.util.Optional;

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
    public Destination createDestination(MessageEntity messageEntity) {
        if (messageEntity.getQueue() != null) {
            return new ActiveMQQueue(messageEntity.getQueue().getName());
        }
        return null;
    }

}
