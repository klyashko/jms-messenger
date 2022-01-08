package com.idea.tools.jms;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.idea.tools.dto.IbmConfiguration;
import com.idea.tools.dto.ServerDto;

import javax.jms.Connection;
import javax.jms.JMSException;

public class IbmConnectionStrategy extends AbstractConnectionStrategy {

    private static JmsConnectionFactory createJMSConnectionFactory(ServerDto server) throws JMSException {
        IbmConfiguration ibm = server.getIbmConfiguration();
        JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        JmsConnectionFactory cf = ff.createConnectionFactory();
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, server.getHost());
        cf.setIntProperty(WMQConstants.WMQ_PORT, server.getPort());
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, ibm.getChannel());
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, ibm.getQueueManager());
        cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "jms-messenger");
        return cf;
    }

    @Override
    public Connection connect(ServerDto server) throws Exception {
        return connect(server, createJMSConnectionFactory(server));
    }

}
