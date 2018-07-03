package com.idea.tools.dto;

public enum ConnectionType {

    JNDI, TCP;

    private static final ConnectionType[] WILDFLY_CONNECTION_TYPES = {JNDI};
    private static final ConnectionType[] ACTIVE_MQ_CONNECTION_TYPES = {TCP};
    private static final ConnectionType[] EMPTY = {};

    public static ConnectionType[] getConnectionTypes(ServerType type) {
        if (type != null)
            switch (type) {
                case WILDFLY_11:
                    return WILDFLY_CONNECTION_TYPES;
                case ACTIVE_MQ:
                    return ACTIVE_MQ_CONNECTION_TYPES;
            }
        return EMPTY;
    }

}
