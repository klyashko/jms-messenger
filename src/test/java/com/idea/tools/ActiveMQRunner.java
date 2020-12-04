package com.idea.tools;

import static org.apache.activemq.command.ActiveMQDestination.createDestination;

import java.net.URI;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;

public class ActiveMQRunner {

	@Test
	public void test() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = new ActiveMQQueue("Q1");
		MessageProducer producer = session.createProducer(queue);

		for (int i = 0; i < 5; i++) {
			TextMessage message = session.createTextMessage("Hi " + i);
			producer.send(message);
		}

		producer.close();
		session.close();
		connection.close();
	}

	@Test
	public void broker() throws Exception {
		BrokerService broker = BrokerFactory.createBroker(new URI("broker:(tcp://localhost:61616)"));
		broker.setPersistent(false);
		broker.setDestinations(new ActiveMQDestination[]{createDestination("Q1", ActiveMQDestination.QUEUE_TYPE)});
		broker.start();
        System.out.println("DONE");
		synchronized (this) {
			this.wait();
		}
	}

	@Test
	public void runner() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		try (Connection connection = connectionFactory.createConnection()) {
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("Q1");
			MessageProducer producer = session.createProducer(queue);
			String task = "Task";
			for (int i = 0; i < 1; i++) {
				String payload = task + i;
				Message msg = session.createTextMessage(payload);
				msg.setObjectProperty("test", "test" + i);
				msg.setObjectProperty("null", null);
				msg.setObjectProperty("name", "value" + i);
				msg.setObjectProperty("header", "value" + i);
				System.out.println("Sending text '" + payload + "'");
				producer.send(msg);
			}
		}
	}

}
