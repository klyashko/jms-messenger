package com.idea.tools.view.action;

import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.App.queueService;
import static com.idea.tools.App.serverService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class ToolBarRemoveAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();

    public ToolBarRemoveAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Remove", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (isSelected(Server.class)) {
            serversPanel.getSelectedValue(Server.class).ifPresent(serverService()::remove);
        } else if (isSelected(QueueDto.class)) {
            serversPanel.getSelectedValue(QueueDto.class).ifPresent(queueService()::remove);
        }
    }

    @Override
    public void update(AnActionEvent event) {
        boolean enable = isSelected(Server.class) || isSelected(QueueDto.class);
        event.getPresentation().setEnabled(enable);
    }
}
