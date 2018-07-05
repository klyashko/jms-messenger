package com.idea.tools.utils;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.IconUtil;

import javax.swing.*;

import static com.intellij.util.ui.UIUtil.isUnderDarcula;

public class IconUtils extends IconUtil {

    private static final String ICON_FOLDER = "/images/";

    public static Icon icon(String iconFilename) {
        return IconLoader.findIcon(ICON_FOLDER + iconFilename);
    }


    public static Icon icon(String parentPath, String iconFilename) {
        return IconLoader.findIcon(parentPath + iconFilename);
    }

    public static Icon getSettingsIcon() {
        return isUnderDarcula() ? icon("settings_dark.png") : icon("settings.png");
    }

    public static Icon getWildflyIcon() {
        return icon("wildfly.png");
    }

    public static Icon getActiveMqIcon() {
        return icon("activemq.png");
    }

    public static Icon getJmsIcon() {
        return icon("jms.png");
    }

    public static Icon getSendMessageIcon() {
        return icon("send_message.png");
    }
}
