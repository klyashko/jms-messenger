package com.idea.tools.jms;

import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.utils.Checked.function;
import static com.idea.tools.utils.Utils.toList;
import static com.idea.tools.utils.Utils.uri;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.idea.tools.dto.ConnectionType;
import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.DestinationType;
import com.idea.tools.dto.SSLConfiguration;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.utils.Assert;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import javax.jms.Connection;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;

public class ActiveMQConnectionStrategy extends AbstractConnectionStrategy {

	@Override
	public Connection connect(ServerDto server) throws Exception {
		validate(server);
		Assert.equals(server.getType(), ACTIVE_MQ, "Unsupported server type %s", server.getType());

		ActiveMQConnectionFactory factory = connectionFactory(server);
		return connect(server, factory);
	}

	@Override
	public List<DestinationDto> getDestinations(Connection connection) {
		if (!(connection instanceof ActiveMQConnection)) {
			return Collections.emptyList();
		}
		try {
			ActiveMQConnection activeMQConnection = (ActiveMQConnection) connection;
			activeMQConnection.start();
			DestinationSource destinationSource = activeMQConnection.getDestinationSource();
			List<DestinationDto> queues = toList(destinationSource.getQueues(), function(q -> {
				DestinationDto dto = new DestinationDto();
				dto.setName(q.getQueueName());
				dto.setType(DestinationType.QUEUE);
				return dto;
			}));
			List<DestinationDto> topics = toList(destinationSource.getTopics(), function(t -> {
				DestinationDto dto = new DestinationDto();
				dto.setName(t.getTopicName());
				dto.setType(DestinationType.TOPIC);
				return dto;
			}));
			queues.addAll(topics);
			return queues;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	private ActiveMQConnectionFactory connectionFactory(ServerDto server) throws Exception {
		URI uri = uri(getUrlString(server));
		ConnectionType type = server.getConnectionType();
		switch (type) {
			case TCP:
				return new ActiveMQConnectionFactory(uri);
			case SSL:
				SSLConfiguration ssl = server.getSslConfiguration();
				ActiveMQSslConnectionFactory cf = new ActiveMQSslConnectionFactory(uri);
				if (isNotBlank(ssl.getTruststore())) {
					cf.setTrustStore(ssl.getTruststore());
				}
				if (isNotBlank(ssl.getTruststorePassword())) {
					cf.setTrustStorePassword(ssl.getTruststorePassword());
				}
				if (isNotBlank(ssl.getKeystore())) {
					cf.setKeyStore(ssl.getKeystore());
				}
				if (isNotBlank(ssl.getKeystorePassword())) {
					cf.setKeyStorePassword(ssl.getKeystorePassword());
				}
				return cf;
			default:
				throw new IllegalArgumentException(String.format("Unsupported server connection type [%s]", type));
		}
	}

}
