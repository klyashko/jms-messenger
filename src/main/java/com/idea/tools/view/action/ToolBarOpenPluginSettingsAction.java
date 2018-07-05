package com.idea.tools.view.action;


import com.idea.tools.JmsMessengerComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;

import javax.swing.*;

import static com.idea.tools.App.showSettingsDialog;
import static com.idea.tools.utils.IconUtils.getSettingsIcon;

public class ToolBarOpenPluginSettingsAction extends AnAction implements DumbAware {


    private static final Icon SETTINGS_ICON = getSettingsIcon();

    public ToolBarOpenPluginSettingsAction() {
        super("Messenger Settings", "Edit the Messenger settings for the current project", SETTINGS_ICON);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        showSettingsDialog(JmsMessengerComponent.class);
    }
}
