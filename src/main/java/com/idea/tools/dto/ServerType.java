package com.idea.tools.dto;

import lombok.Getter;

import javax.swing.*;

import static com.idea.tools.dto.ConnectionType.ACTIVE_MQ_CONNECTION_TYPES;
import static com.idea.tools.dto.ConnectionType.ARTEMIS_CONNECTION_TYPES;
import static com.idea.tools.utils.IconUtils.getActiveMqIcon;
import static com.idea.tools.utils.IconUtils.getArtemisIcon;

@Getter
public enum ServerType {

    ACTIVE_MQ(getActiveMqIcon(), ACTIVE_MQ_CONNECTION_TYPES),
    ARTEMIS(getArtemisIcon(), ARTEMIS_CONNECTION_TYPES);

    private final Icon icon;
    private final ConnectionType[] connectionTypes;

    ServerType(Icon icon, ConnectionType... connectionTypes) {
        this.icon = icon;
        this.connectionTypes = connectionTypes;
    }

}
