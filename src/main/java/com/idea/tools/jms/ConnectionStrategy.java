package com.idea.tools.jms;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;

import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Queue;
import java.util.Optional;

public interface ConnectionStrategy {

    Optional<ConnectionFactory> getConnectionFactory(Server server);

    Queue createQueueDestination(QueueDto messageEntity);

    Optional<MessageDto> map(Message message);

}
