package com.idea.tools.dto;

import javax.swing.*;

import static com.idea.tools.utils.GuiUtils.icon;

public enum ServerType {

    ACTIVE_MQ("activemq.png"),
    WILDFLY_11("wildfly.png");

    private Icon icon;

    ServerType(String icon) {
        this.icon = icon(icon);
    }

    public Icon getIcon() {
        return icon;
    }
}
