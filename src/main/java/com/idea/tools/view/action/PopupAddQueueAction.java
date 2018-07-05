package com.idea.tools.view.action;

import com.idea.tools.dto.Server;
import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.intellij.util.IconUtil.getAddIcon;

public class PopupAddQueueAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getAddIcon();

    public PopupAddQueueAction(BrowserPanel browserPanel) {
        super("Add queue", "", ICON, browserPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {

    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(Server.class));
    }
}
