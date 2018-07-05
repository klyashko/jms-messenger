package com.idea.tools.dto;

import javax.swing.*;

import static com.idea.tools.utils.IconUtils.getActiveMqIcon;
import static com.idea.tools.utils.IconUtils.getWildflyIcon;

public enum ServerType {

    ACTIVE_MQ(getActiveMqIcon()),
    WILDFLY_11(getWildflyIcon());

    private Icon icon;

    ServerType(Icon icon) {
        this.icon = icon;
    }

    public Icon getIcon() {
        return icon;
    }
}
