package com.idea.tools.view.action;

import com.idea.tools.dto.Server;
import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.App.serverService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public abstract class AbstractRemoveServerAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();

    AbstractRemoveServerAction(BrowserPanel browserPanel) {
        super("Remove server", "", ICON, browserPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        browserPanel.getSelectedValue(Server.class).ifPresent(serverService()::remove);
    }

    boolean isServerSelected() {
        return isSelected(Server.class);
    }

}
