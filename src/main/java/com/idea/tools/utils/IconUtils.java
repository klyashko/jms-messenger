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

    public static Icon getSettingsIcon() {
        return isUnderDarcula() ? icon("settings_dark.png") : icon("settings.png");
    }

    public static Icon getWildflyIcon() {
        return icon("wildfly.png");
    }

    public static Icon getActiveMqIcon() {
        return icon("activemq.png");
    }

    public static Icon getArtemisIcon() {
        return icon("artemis.png");
    }

    public static Icon getJmsIcon() {
        return icon("jms.png");
    }

    public static Icon getSendMessageIcon() {
        return icon("send_message.png");
    }

    public static Icon getBrowseIcon() {
        return icon("browse.png");
    }

    public static Icon getRefreshIcon() {
        return isUnderDarcula() ? icon("refresh_dark.png") : icon("refresh.png");
    }

    public static Icon getRightShiftIcon() {
        return icon("shift_right.png");
    }

    public static Icon getLeftShiftIcon() {
        return icon("shift_left.png");
    }

    public static Icon getHornetIcon() {
        return icon("hornet.png");
    }

    public static Icon getReadMessageIcon() {
        return icon("read_message.png");
    }

    public static Icon getTemplateIcon() {
        return icon("template.png");
    }

}
