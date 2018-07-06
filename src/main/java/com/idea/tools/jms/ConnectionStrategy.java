package com.idea.tools.jms;

import com.idea.tools.dto.MessageEntity;
import com.idea.tools.dto.Server;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import java.util.Optional;

public interface ConnectionStrategy {

    Optional<ConnectionFactory> getConnectionFactory(Server server);

    Destination createDestination(MessageEntity messageEntity);

}
