package com.idea.tools.jms;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.ServerDto;
import java.util.List;
import java.util.Optional;
import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;

public interface ConnectionStrategy {

	Connection connect(ServerDto server) throws Exception;

	QueueBrowser browser(Session session, String destination) throws Exception;

	Optional<MessageDto> map(Message message) throws Exception;

	List<DestinationDto> getDestinations(Connection connection);
}
