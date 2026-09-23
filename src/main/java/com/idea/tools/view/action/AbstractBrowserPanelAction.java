package com.idea.tools.view.action;

import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.DumbAware;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

abstract class AbstractBrowserPanelAction extends AnAction implements DumbAware {

    final ServersBrowseToolPanel serversPanel;

    AbstractBrowserPanelAction(String text, String description, Icon icon, ServersBrowseToolPanel serversPanel) {
        super(text, description, icon);
        this.serversPanel = serversPanel;
    }

    boolean isSelected(Class<?> clazz) {
        return serversPanel.getSelectedValue(clazz).isPresent();
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

}
