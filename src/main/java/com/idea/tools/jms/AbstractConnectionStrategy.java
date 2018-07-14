package com.idea.tools.jms;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.Server;
import com.idea.tools.utils.Assert;

import javax.jms.*;
import java.lang.IllegalStateException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.idea.tools.dto.ContentType.TEXT;
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

    protected MessageDto mapTextMessage(TextMessage msg) throws JMSException {
        MessageDto dto = new MessageDto();
        dto.setMessageID(msg.getJMSMessageID());
        dto.setCorrelationId(msg.getJMSCorrelationID());
        dto.setJmsType(msg.getJMSType());
        dto.setTimestamp(msg.getJMSTimestamp());
        dto.setType(TEXT);
        dto.setPayload(msg.getText());
        dto.setHeaders(mapProperties(msg));
        dto.setPriority(msg.getJMSPriority());
        dto.setExpiration(msg.getJMSExpiration());
        dto.setDeliveryMode(msg.getJMSDeliveryMode());
        return dto;
    }

    protected Map<String, Object> mapProperties(Message msg) throws JMSException {
        Map<String, Object> properties = new HashMap<>();
        Enumeration srcProperties = msg.getPropertyNames();
        while (srcProperties.hasMoreElements()) {
            String propertyName = (String) srcProperties.nextElement();
            properties.put(propertyName, msg.getObjectProperty(propertyName));
        }
        return properties;
    }

    protected void setMessageProperties(Message msg, Map<String, Object> properties) throws JMSException {
        for (String name : properties.keySet()) {
            msg.setObjectProperty(name, properties.get(name));
        }
    }

}
