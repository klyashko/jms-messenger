package com.idea.tools.jms;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;
import com.idea.tools.utils.Assert;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.client.impl.ServerLocatorImpl;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Queue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.idea.tools.dto.ServerType.ARTEMIS;

public class ArtemisConnectionStrategy extends AbstractConnectionStrategy {

    @Override
    public Connection connect(Server server) throws Exception {
        validate(server);
        Assert.equals(server.getType(), ARTEMIS, String.format("Unsupported server type %s", server.getType()));

        ConnectionFactory factory = connectionFactory(server);
        return connect(server, factory);
    }

    @Override
    public Queue createQueueDestination(QueueDto messageEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<MessageDto> map(Message message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<QueueDto> getQueues(Connection connection) {
        throw new UnsupportedOperationException();
    }

    private ActiveMQJMSConnectionFactory connectionFactory(Server server) {
        switch (server.getConnectionType()) {
            case HTTP:
                Map<String, Object> params = new HashMap<>();
                params.put("httpUpgradeEndpoint", "http-acceptor");
                params.put("activemqServerName", "default");
                params.put("httpUpgradeEnabled", "true");
                params.put("port", server.getPort());
                params.put("host", server.getHost());

                TransportConfiguration tc = new TransportConfiguration(
                        "org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory",
                        params
                );
                ServerLocatorImpl locator = new ServerLocatorImpl(false, tc);
                return new ActiveMQJMSConnectionFactory(locator);
            case TCP:
                String url = getUrlString(server);
                return new ActiveMQJMSConnectionFactory(url);
            default:
                throw new UnsupportedOperationException(String.format("Unsupported connection type %s", server.getConnectionType()));
        }
    }

}
