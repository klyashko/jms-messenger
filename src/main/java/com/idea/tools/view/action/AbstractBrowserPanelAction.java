package com.idea.tools.view.action;

import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.DumbAware;

import javax.swing.*;

abstract class AbstractBrowserPanelAction extends AnAction implements DumbAware {

    final ServersBrowseToolPanel serversBrowseToolPanel;

    AbstractBrowserPanelAction(String text, String description, Icon icon, ServersBrowseToolPanel serversBrowseToolPanel) {
        super(text, description, icon);
        this.serversBrowseToolPanel = serversBrowseToolPanel;
    }

    boolean isSelected(Class<?> clazz) {
        return serversBrowseToolPanel.getSelectedValue(clazz).isPresent();
    }

}
