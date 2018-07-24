package com.idea.tools.jms;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.utils.Assert;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.client.impl.ServerLocatorImpl;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.hornetq.jms.client.HornetQTextMessage;
import org.jetbrains.annotations.NotNull;

import javax.jms.*;
import java.util.*;

import static com.idea.tools.dto.ConnectionType.HTTP;
import static com.idea.tools.dto.ServerType.HORNETQ;
import static org.hornetq.core.remoting.impl.netty.TransportConstants.*;

public class HornetConnectionStrategy extends AbstractConnectionStrategy {

    @Override
    public Connection connect(ServerDto server) throws Exception {
        validate(server);
        Assert.equals(server.getType(), HORNETQ, String.format("Unsupported server type %s", server.getType()));
        Assert.equals(server.getConnectionType(), HTTP, String.format("Unsupported connection type %s", server.getConnectionType()));

        ConnectionFactory factory = connectionFactory(server);
        return connect(server, factory);
    }

    @Override
    public Optional<MessageDto> map(Message msg) throws JMSException {
        if (msg instanceof HornetQTextMessage) {
            return Optional.ofNullable(mapTextMessage((TextMessage) msg));
        }
        return Optional.empty();
    }

    @Override
    public List<DestinationDto> getDestinations(Connection connection) {
        return Collections.emptyList();
    }

    private HornetQConnectionFactory connectionFactory(@NotNull ServerDto server) {
        Map<String, Object> params = new HashMap<>();
        params.put(HTTP_UPGRADE_ENABLED_PROP_NAME, "true");
        params.put(HTTP_UPGRADE_ENDPOINT_PROP_NAME, "http-acceptor");
        params.put(PORT_PROP_NAME, server.getPort());
        params.put(HOST_PROP_NAME, server.getHost());

        TransportConfiguration tc = new TransportConfiguration(
                "org.hornetq.core.remoting.impl.netty.NettyConnectorFactory",
                params
        );
        ServerLocatorImpl locator = new ServerLocatorImpl(false, tc);
        return new HornetQConnectionFactory(locator);
    }
}
