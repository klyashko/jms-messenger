package com.idea.tools.jms;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;

import javax.jms.Connection;
import javax.jms.Message;
import java.util.List;
import java.util.Optional;

public interface ConnectionStrategy {

    Connection connect(Server server) throws Exception;

    Optional<MessageDto> map(Message message);

    List<QueueDto> getQueues(Connection connection);
}
