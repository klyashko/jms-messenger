package com.idea.tools.view.action;

import com.idea.tools.dto.Queue;
import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.App.queueService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class PopupRemoveQueueAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();

    public PopupRemoveQueueAction(BrowserPanel browserPanel) {
        super("Remove queue", "", ICON, browserPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        browserPanel.getSelectedValue(Queue.class).ifPresent(queueService()::remove);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(Queue.class));
    }
}
