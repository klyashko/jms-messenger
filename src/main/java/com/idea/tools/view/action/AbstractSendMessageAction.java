package com.idea.tools.view.action;

import com.idea.tools.dto.QueueDto;
import com.idea.tools.view.SendMessageDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.utils.IconUtils.getSendMessageIcon;

public abstract class AbstractSendMessageAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getSendMessageIcon();

    AbstractSendMessageAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Send message", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(QueueDto.class).ifPresent(SendMessageDialog::showDialog);
    }

    boolean isQueueSelected() {
        return isSelected(QueueDto.class);
    }

}
