package com.idea.tools.view.action;

import com.idea.tools.dto.Server;
import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;

import javax.swing.*;

import static com.idea.tools.App.serverService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public abstract class AbstractRemoveServerAction extends AnAction implements DumbAware {

    private static final Icon ICON = getRemoveIcon();
    final BrowserPanel browserPanel;

    AbstractRemoveServerAction(BrowserPanel browserPanel) {
        super("Remove server", "", ICON);
        this.browserPanel = browserPanel;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        browserPanel.getSelectedValue(Server.class).ifPresent(serverService()::remove);
    }

    boolean isServerSelected() {
        return browserPanel.getSelectedValue(Server.class).isPresent();
    }

}
