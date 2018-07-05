package com.idea.tools.view.action;

import com.idea.tools.dto.Queue;
import com.idea.tools.dto.Server;
import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.App.queueService;
import static com.idea.tools.App.serverService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class ToolBarRemoveAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();

    public ToolBarRemoveAction(BrowserPanel browserPanel) {
        super("Remove", "", ICON, browserPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (isSelected(Server.class)) {
            browserPanel.getSelectedValue(Server.class).ifPresent(serverService()::remove);
        } else if (isSelected(Queue.class)) {
            browserPanel.getSelectedValue(Queue.class).ifPresent(queueService()::remove);
        }
    }

    @Override
    public void update(AnActionEvent event) {
        boolean enable = isSelected(Server.class) || isSelected(Queue.class);
        event.getPresentation().setEnabled(enable);
    }
}
