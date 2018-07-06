package com.idea.tools.view.action;

import com.idea.tools.dto.QueueDto;
import com.idea.tools.view.QueueEditDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.intellij.util.IconUtil.getEditIcon;

public class PopupEditQueueAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getEditIcon();

    public PopupEditQueueAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Edit queue", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversBrowseToolPanel.getSelectedValue(QueueDto.class).ifPresent(QueueEditDialog::showDialog);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(QueueDto.class));
    }
}
