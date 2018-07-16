package com.idea.tools.jms;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
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
    public List<QueueDto> getQueues(Connection connection) {
        return Collections.emptyList();
    }

    private HornetQConnectionFactory connectionFactory(@NotNull ServerDto server) {
        Map<String, Object> params = new HashMap<>();
        params.put("http-upgrade-enabled", "true");
        params.put("http-upgrade-endpoint", "http-acceptor");
        params.put("port", server.getPort());
        params.put("host", server.getHost());

        TransportConfiguration tc = new TransportConfiguration(
                "org.hornetq.core.remoting.impl.netty.NettyConnectorFactory",
                params
        );
        ServerLocatorImpl locator = new ServerLocatorImpl(false, tc);
        return new HornetQConnectionFactory(locator);
    }
}
