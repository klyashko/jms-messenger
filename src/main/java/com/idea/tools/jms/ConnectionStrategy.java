package com.idea.tools.jms;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.ServerDto;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.List;
import java.util.Optional;

public interface ConnectionStrategy {

    Connection connect(ServerDto server) throws Exception;

    Optional<MessageDto> map(Message message) throws JMSException;

    List<QueueDto> getQueues(Connection connection);
}
