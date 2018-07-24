package com.idea.tools.jms;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.utils.Assert;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.client.impl.ServerLocatorImpl;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.jetbrains.annotations.NotNull;

import javax.jms.*;
import java.util.*;

import static com.idea.tools.dto.ServerType.ARTEMIS;
import static org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants.*;

public class ArtemisConnectionStrategy extends AbstractConnectionStrategy {

    @Override
    public Connection connect(ServerDto server) throws Exception {
        validate(server);
        Assert.equals(server.getType(), ARTEMIS, String.format("Unsupported server type %s", server.getType()));

        ConnectionFactory factory = connectionFactory(server);
        return connect(server, factory);
    }

    @Override
    public Optional<MessageDto> map(Message msg) throws JMSException {
        if (msg instanceof ActiveMQTextMessage) {
            return Optional.ofNullable(mapTextMessage((TextMessage) msg));
        }
        return Optional.empty();
    }

    @Override
    public List<DestinationDto> getDestinations(Connection connection) {
        return Collections.emptyList();
    }

    private ActiveMQJMSConnectionFactory connectionFactory(@NotNull ServerDto server) {
        switch (server.getConnectionType()) {
            case HTTP:
                Map<String, Object> params = new HashMap<>();
                params.put(HTTP_UPGRADE_ENDPOINT_PROP_NAME, "http-acceptor");
                params.put("activemqServerName", "default");
                params.put(HTTP_UPGRADE_ENABLED_PROP_NAME, "true");
                params.put(PORT_PROP_NAME, server.getPort());
                params.put(HOST_PROP_NAME, server.getHost());

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
