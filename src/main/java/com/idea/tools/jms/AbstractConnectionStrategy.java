package com.idea.tools.jms;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.HeaderDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.utils.Assert;

import javax.jms.*;
import java.lang.IllegalStateException;
import java.util.*;

import static com.idea.tools.dto.ContentType.TEXT;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public abstract class AbstractConnectionStrategy implements ConnectionStrategy {

	@Override
	public Optional<MessageDto> map(Message message) throws Exception {
		if (message instanceof TextMessage) {
			return Optional.ofNullable(mapTextMessage((TextMessage) message));
		}
		return Optional.empty();
	}

	@Override
	public QueueBrowser browser(Session session, String destination) throws Exception {
		return session.createBrowser(session.createQueue(destination));
	}

	@Override
	public List<DestinationDto> getDestinations(Connection connection) {
		return Collections.emptyList();
	}

	protected Connection connect(ServerDto server, ConnectionFactory factory) throws JMSException {
		Connection connection;
		if (isNotEmpty(server.getLogin())) {
			connection = factory.createConnection(server.getLogin(), server.getPassword());
		} else {
			connection = factory.createConnection();
		}
		Assert.notNull(connection, "Connection may not be obtained", IllegalStateException::new);
		return connection;
	}

	protected void validate(ServerDto server) {
		Assert.notNull(server.getConnectionType(), "Connection type must not be null");
		Assert.notNull(server.getHost(), "Host must not be null");
		Assert.notNull(server.getPort(), "Port must not be null");
	}

	protected String getUrlString(ServerDto server) {
		return String.format("%s://%s:%s", server.getConnectionType().getExtension(), server.getHost(), server.getPort());
	}

	protected MessageDto mapTextMessage(TextMessage msg) throws JMSException {
		MessageDto dto = new MessageDto();
		dto.setMessageID(msg.getJMSMessageID());
		dto.setCorrelationId(msg.getJMSCorrelationID());
		dto.setJmsType(msg.getJMSType());
		dto.setTimestamp(msg.getJMSTimestamp());
		dto.setType(TEXT);
		dto.setPayload(msg.getText());
		dto.setHeaders(mapProperties(msg));
		dto.setPriority(msg.getJMSPriority());
		dto.setExpiration(msg.getJMSExpiration());
		dto.setDeliveryMode(msg.getJMSDeliveryMode());
		return dto;
	}

	protected List<HeaderDto> mapProperties(Message msg) throws JMSException {
		List<HeaderDto> properties = new ArrayList<>();
		Enumeration srcProperties = msg.getPropertyNames();
		while (srcProperties.hasMoreElements()) {
			String propertyName = (String) srcProperties.nextElement();
			properties.add(new HeaderDto(propertyName, msg.getObjectProperty(propertyName).toString()));
		}
		return properties;
	}

}
