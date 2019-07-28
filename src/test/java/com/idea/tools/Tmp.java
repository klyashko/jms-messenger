package com.idea.tools;

import com.idea.tools.dto.ServerDto;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.client.impl.ServerLocatorImpl;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hornetq.core.remoting.impl.netty.TransportConstants.*;

public class Tmp {

    @Test
	public void tmp(){
		short s1 = 5;
		short s0 = (short)0xaced;
		System.out.println(String.format("invalid stream header: %04X%04X", s0, s1));
	}

    @Test
    public void wildflyConnection() throws JMSException {
        Connection connection = connection();
        System.out.println(connection);
        connection.start();
        connection.stop();
        System.out.println("done");
    }

    private Connection connection() throws JMSException {
        Map<String, Object> params = new HashMap<>();
        params.put(PORT_PROP_NAME, "8080");
        params.put(HOST_PROP_NAME, "127.0.0.1");
        params.put(HTTP_UPGRADE_ENABLED_PROP_NAME, "true");
        params.put(HTTP_UPGRADE_ENDPOINT_PROP_NAME, "http-acceptor");

        TransportConfiguration tc = new TransportConfiguration("org.hornetq.core.remoting.impl.netty.NettyConnectorFactory", params, "http-connector");

        ServerLocatorImpl locator = new ServerLocatorImpl(false, tc);
        HornetQConnectionFactory factory = new HornetQConnectionFactory(locator);

        return factory.createConnection();
    }

    private ServerDto server(String name) {
        ServerDto s = new ServerDto();
        s.setName(name);
        return s;
    }

}
