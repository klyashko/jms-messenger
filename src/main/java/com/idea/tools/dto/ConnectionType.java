package com.idea.tools.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum ConnectionType {

    JNDI,
    TCP("tcp");

    public static final ConnectionType[] WILDFLY_CONNECTION_TYPES = {JNDI};
    public static final ConnectionType[] ACTIVE_MQ_CONNECTION_TYPES = {TCP};

    private String extension;

    ConnectionType(String extension) {
        this.extension = extension;
    }

}
