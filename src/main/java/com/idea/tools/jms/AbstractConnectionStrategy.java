package com.idea.tools.jms;

import com.idea.tools.dto.Server;
import com.idea.tools.utils.Assert;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public abstract class AbstractConnectionStrategy implements ConnectionStrategy {

    protected Connection connect(Server server, ConnectionFactory factory) throws JMSException {
        Connection connection;
        if (isNotEmpty(server.getLogin())) {
            connection = factory.createConnection(server.getLogin(), server.getPassword());
        } else {
            connection = factory.createConnection();
        }
        Assert.notNull(connection, "Connection may not be obtained", IllegalStateException::new);
        return connection;
    }

    protected void validate(Server server) {
        Assert.notNull(server.getConnectionType(), "Connection type must not be null");
        Assert.notNull(server.getHost(), "Host must not be null");
        Assert.notNull(server.getPort(), "Port must not be null");
    }

    protected String getUrlString(Server server) {
        return String.format("%s://%s:%s", server.getConnectionType().getExtension(), server.getHost(), server.getPort());
    }

}
