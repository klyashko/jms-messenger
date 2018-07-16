package com.idea.tools.view.action;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.App.serverService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class PopupRemoveServerAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();

    public PopupRemoveServerAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Remove server", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(ServerDto.class).ifPresent(serverService()::remove);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(ServerDto.class));
    }
}
