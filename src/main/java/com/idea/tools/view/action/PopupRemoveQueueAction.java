package com.idea.tools.view.action;

import com.idea.tools.dto.QueueDto;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.App.queueService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class PopupRemoveQueueAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();

    public PopupRemoveQueueAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Remove queue", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(QueueDto.class).ifPresent(queueService()::remove);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(QueueDto.class));
    }
}
