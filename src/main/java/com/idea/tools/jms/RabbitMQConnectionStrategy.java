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
import org.apache.commons.lang3.StringUtils;

import javax.jms.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.Optional;

import static com.idea.tools.dto.ServerType.RABBIT_MQ;

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
			BytesMessage byteMessage = (BytesMessage) message;
			byte[] byteData = new byte[(int) byteMessage.getBodyLength()];
			byteMessage.readBytes(byteData);
			byteMessage.reset();
			RMQMessage msg = deserialize(byteData);
			return msg != null ? super.map(msg) : Optional.empty();
		}
		return super.map(message);
	}

	private RMQMessage deserialize(byte[] b) throws Exception {
		/* If we don't recognise the message format this throws an exception */
		ByteArrayInputStream bin = new ByteArrayInputStream(b);
		WhiteListObjectInputStream in = new WhiteListObjectInputStream(bin);
		// read the class name from the stream
		String clazz = in.readUTF();

		// instantiate the message object
		CustomRMQTextMessage msg = instantiate(clazz);
		if (msg == null) {
			return null;
		}

		// read the message id
		in.readUTF();
		// read JMS properties
		int propsSize = in.readInt();
		for (int i = 0; i < propsSize; i++) {
			String name = in.readUTF();
			Object value = CustomRMQTextMessage.readPrimitive(in);
			msg.setObjectProperty(name, value);
		}
		//read custom properties
		propsSize = in.readInt();
		for (int i = 0; i < propsSize; i++) {
			String name = in.readUTF();
			Object value = CustomRMQTextMessage.readPrimitive(in);
			msg.setObjectProperty(name, value);
		}
		// read the body of the message
		msg.readBody(in, bin);
		return msg;
	}

	private CustomRMQTextMessage instantiate(String clazz) {
		if (StringUtils.isBlank(clazz)) {
			return null;
		}
		switch (clazz) {
			case "com.rabbitmq.jms.client.message.RMQTextMessage":
				return new CustomRMQTextMessage();
			default:
				return null;
		}
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
