package com.idea.tools.view.action;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.view.DestinationEditDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.intellij.util.IconUtil.getAddIcon;

public class PopupAddDestinationAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getAddIcon();

    public PopupAddDestinationAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Add destination", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(ServerDto.class).ifPresent(DestinationEditDialog::showDialog);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(ServerDto.class));
    }
}
