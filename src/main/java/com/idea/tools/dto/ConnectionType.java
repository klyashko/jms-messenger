package com.idea.tools.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum ConnectionType {

    HTTP("http"),
    TCP("tcp");

    public static final ConnectionType[] ARTEMIS_CONNECTION_TYPES = {HTTP, TCP};
    public static final ConnectionType[] HORNETQ_CONNECTION_TYPES = {HTTP};
    public static final ConnectionType[] ACTIVE_MQ_CONNECTION_TYPES = {TCP};

    private String extension;

    ConnectionType(String extension) {
        this.extension = extension;
    }

}
