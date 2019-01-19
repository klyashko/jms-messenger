package com.idea.tools.jms;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.utils.Assert;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import com.rabbitmq.jms.client.RMQMessage;
import com.rabbitmq.jms.client.message.RMQBytesMessage;
import com.rabbitmq.jms.client.message.RMQTextMessage;
import com.rabbitmq.jms.util.WhiteListObjectInputStream;

import javax.jms.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.Optional;

import static com.idea.tools.dto.ServerType.RABBIT_MQ;
import static com.idea.tools.utils.Utils.getOrDefault;

public class RabbitMQConnectionStrategy extends AbstractConnectionStrategy {

	@Override
	public Connection connect(ServerDto server) throws Exception {
		validate(server);
		Assert.equals(server.getType(), RABBIT_MQ, "Unsupported server type %s", server.getType());

		RMQConnectionFactory factory = new RMQConnectionFactory();
		factory.setVirtualHost(server.getVirtualHost());
		factory.setHost(server.getHost());
		factory.setPort(server.getPort());
		return connect(server, factory);
	}

	@Override
	public QueueBrowser browser(Session session, String destination) throws Exception {
		Queue queue = session.createQueue(destination);
		if (queue instanceof RMQDestination) {
			RMQDestination rmqDestination = (RMQDestination) queue;
			rmqDestination.setDeclared(false);
			rmqDestination.setAmqp(true);
		}
		return session.createBrowser(queue);
	}

	@Override
	public Optional<MessageDto> map(Message message) throws Exception {
		if (message instanceof RMQBytesMessage) {
			byte[] byteData = readBody((BytesMessage) message);
			//try to extract wrapped jms message (works only for java clients)
			RMQMessage msg = deserialize(byteData);
			return msg != null ? super.map(msg) : super.map(message);
		}
		return super.map(message);
	}

	@Override
	protected MessageDto mapMessage(Message msg) throws JMSException {
		if (isAmqp(msg)) {
			MessageDto dto = new MessageDto();
			dto.setMessageID(getOrDefault(msg::getJMSMessageID, ""));
			dto.setCorrelationId(getOrDefault(msg::getJMSCorrelationID, ""));
			dto.setJmsType(msg.getJMSType());
			dto.setTimestamp(getOrDefault(msg::getJMSTimestamp, 0L));
			dto.setHeaders(mapProperties(msg));
			dto.setPriority(getOrDefault(msg::getJMSPriority, -1));
			dto.setExpiration(getOrDefault(msg::getJMSExpiration, -1L));
			dto.setDeliveryMode(msg.getJMSDeliveryMode());
			return dto;
		}
		return super.mapMessage(msg);
	}

	private boolean isAmqp(Message message) throws JMSException {
		Destination destination = message.getJMSDestination();
		if (destination instanceof RMQDestination) {
			return ((RMQDestination) destination).isAmqp();
		}
		return false;
	}

	private RMQMessage deserialize(byte[] b) throws Exception {
		/* If we don't recognise the message format this throws an exception */
		ByteArrayInputStream bin = new ByteArrayInputStream(b);
		WhiteListObjectInputStream in;
		try {
			in = new WhiteListObjectInputStream(bin);
		} catch (Exception e) {
			// Unable to deserialize message
			return null;
		}
		// read the class name from the stream
		String clazz = in.readUTF();

		// instantiate the message object
		CustomRMQTextMessage msg = instantiate(clazz);
		if (msg == null) {
			return null;
		}

		// skip a message id
		in.readUTF();
		// read JMS properties
		readProperties(in, msg);
		//read custom properties
		readProperties(in, msg);
		// read the body of the message
		msg.readBody(in, bin);
		return msg;
	}

	private void readProperties(WhiteListObjectInputStream in, CustomRMQTextMessage msg) throws Exception {
		int propsSize = in.readInt();
		for (int i = 0; i < propsSize; i++) {
			String name = in.readUTF();
			Object value = CustomRMQTextMessage.readPrimitive(in);
			msg.setObjectProperty(name, value);
		}
	}

	private CustomRMQTextMessage instantiate(String clazz) {
		if ("com.rabbitmq.jms.client.message.RMQTextMessage".equals(clazz)) {
			return new CustomRMQTextMessage();
		}
		return null;
	}

	private static class CustomRMQTextMessage extends RMQTextMessage {
		protected static Object readPrimitive(ObjectInput in) throws IOException, ClassNotFoundException {
			return RMQTextMessage.readPrimitive(in);
		}

		@Override
		protected void readBody(ObjectInput inputStream, ByteArrayInputStream bin) throws IOException, ClassNotFoundException {
			super.readBody(inputStream, bin);
		}
	}

}
