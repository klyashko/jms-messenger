package com.idea.tools.view.action;

import com.idea.tools.dto.Queue;
import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.intellij.util.IconUtil.getEditIcon;

public class PopupEditQueueAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getEditIcon();

    public PopupEditQueueAction(BrowserPanel browserPanel) {
        super("Edit queue", "", ICON, browserPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {

    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(Queue.class));
    }
}
