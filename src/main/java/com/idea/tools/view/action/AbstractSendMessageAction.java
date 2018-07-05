package com.idea.tools.view.action;

import com.idea.tools.dto.Queue;
import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.utils.IconUtils.getSettingsIcon;

public abstract class AbstractSendMessageAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getSettingsIcon();

    AbstractSendMessageAction(BrowserPanel browserPanel) {
        super("Send message", "", ICON, browserPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
    }

    boolean isQueueSelected() {
        return isSelected(Queue.class);
    }

}
