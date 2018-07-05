package com.idea.tools.view.action;

import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.DumbAware;

import javax.swing.*;

public abstract class AbstractBrowserPanelAction extends AnAction implements DumbAware {

    final BrowserPanel browserPanel;

    AbstractBrowserPanelAction(String text, String description, Icon icon, BrowserPanel browserPanel) {
        super(text, description, icon);
        this.browserPanel = browserPanel;
    }

    boolean isSelected(Class<?> clazz) {
        return browserPanel.getSelectedValue(clazz).isPresent();
    }

}
