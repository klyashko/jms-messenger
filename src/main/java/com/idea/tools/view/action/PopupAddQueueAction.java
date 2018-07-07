package com.idea.tools.view.action;

import com.idea.tools.dto.Server;
import com.idea.tools.view.QueueEditDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.intellij.util.IconUtil.getAddIcon;

public class PopupAddQueueAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getAddIcon();

    public PopupAddQueueAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Add queue", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(Server.class).ifPresent(QueueEditDialog::showDialog);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(Server.class));
    }
}
