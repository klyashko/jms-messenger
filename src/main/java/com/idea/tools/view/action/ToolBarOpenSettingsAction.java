package com.idea.tools.view.action;


import com.idea.tools.JmsMessengerComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;

import javax.swing.*;

import static com.idea.tools.utils.IconUtils.getSettingsIcon;

public class ToolBarOpenSettingsAction extends AnAction implements DumbAware {

    private static final Icon SETTINGS_ICON = getSettingsIcon();
    private final Project project;

    public ToolBarOpenSettingsAction(Project project) {
        super("Messenger Settings", "Edit the Messenger settings for the current project", SETTINGS_ICON);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        ShowSettingsUtil.getInstance().showSettingsDialog(project, JmsMessengerComponent.class);
    }
}
