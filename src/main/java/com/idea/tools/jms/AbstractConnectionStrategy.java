package com.idea.tools.jms;

import static com.idea.tools.dto.ContentType.TEXT;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.HeaderDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.utils.Assert;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

public abstract class AbstractConnectionStrategy implements ConnectionStrategy {

	@Override
	public Optional<MessageDto> map(Message message) throws Exception {
		if (message instanceof TextMessage) {
			return Optional.ofNullable(mapTextMessage((TextMessage) message));
		} else if (message instanceof BytesMessage) {
			return Optional.ofNullable(mapBytesMessage((BytesMessage) message));
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

	protected MessageDto mapMessage(Message msg) throws JMSException {
		MessageDto dto = new MessageDto();
		dto.setMessageID(msg.getJMSMessageID());
		dto.setCorrelationId(msg.getJMSCorrelationID());
		dto.setJmsType(msg.getJMSType());
		dto.setTimestamp(msg.getJMSTimestamp());
		dto.setHeaders(mapProperties(msg));
		dto.setPriority(msg.getJMSPriority());
		dto.setExpiration(msg.getJMSExpiration());
		dto.setDeliveryMode(msg.getJMSDeliveryMode());
		return dto;
	}

	protected MessageDto mapTextMessage(TextMessage msg) throws JMSException {
		MessageDto dto = mapMessage(msg);
		dto.setType(TEXT);
		dto.setPayload(msg.getText());
		return dto;
	}

	protected MessageDto mapBytesMessage(BytesMessage msg) throws JMSException {
		MessageDto dto = mapMessage(msg);
		//FIXME provide support for byte messages
		dto.setType(TEXT);
		dto.setPayload(new String(readBody(msg)));
		return dto;
	}

	protected byte[] readBody(BytesMessage msg) throws JMSException {
		byte[] data = new byte[(int) msg.getBodyLength()];
		msg.readBytes(data);
		msg.reset();
		return data;
	}

	protected List<HeaderDto> mapProperties(Message msg) throws JMSException {
		List<HeaderDto> properties = new ArrayList<>();
		Enumeration srcProperties = msg.getPropertyNames();
		while (srcProperties.hasMoreElements()) {
			String name = (String) srcProperties.nextElement();
			String value = Objects.toString(msg.getObjectProperty(name));
			properties.add(new HeaderDto(name, value));
		}
		return properties;
	}

}
