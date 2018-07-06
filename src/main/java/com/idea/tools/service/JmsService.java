package com.idea.tools.service;

import com.idea.tools.dto.Server;
import com.idea.tools.dto.ServerType;
import com.idea.tools.jms.ActiveMQConnectionStrategy;
import com.idea.tools.jms.ConnectionStrategy;

import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.utils.Checked.function;

public class JmsService {

    private static final Map<ServerType, ConnectionStrategy> STRATEGIES = new HashMap<>();

    static {
        STRATEGIES.put(ACTIVE_MQ, new ActiveMQConnectionStrategy());
    }

    public boolean testConnection(Server server) {
        return Optional.ofNullable(STRATEGIES.get(server.getType()))
                       .flatMap(strategy -> strategy.getConnectionFactory(server))
                       .map(function(ConnectionFactory::createConnection))
                       .isPresent();
    }

}
