package com.idea.tools.dto;

public enum ConnectionType {

    JNDI, TCP, VM;

    public static final ConnectionType[] WILDFLY_CONNECTION_TYPES = {JNDI};
    public static final ConnectionType[] ACTIVE_MQ_CONNECTION_TYPES = {TCP, VM};

}
