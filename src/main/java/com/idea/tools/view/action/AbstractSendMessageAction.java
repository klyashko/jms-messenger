package com.idea.tools.view.action;

import com.idea.tools.dto.Queue;
import com.idea.tools.view.BrowserPanel;
import com.idea.tools.view.SendMessageDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.utils.IconUtils.getSendMessageIcon;

public abstract class AbstractSendMessageAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getSendMessageIcon();

    AbstractSendMessageAction(BrowserPanel browserPanel) {
        super("Send message", "", ICON, browserPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        browserPanel.getSelectedValue(Queue.class).ifPresent(SendMessageDialog::showDialog);
    }

    boolean isQueueSelected() {
        return isSelected(Queue.class);
    }

}
