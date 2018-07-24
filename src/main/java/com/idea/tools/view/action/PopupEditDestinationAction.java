package com.idea.tools.view.action;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.view.DestinationEditDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.intellij.util.IconUtil.getEditIcon;

public class PopupEditDestinationAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getEditIcon();

    public PopupEditDestinationAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Edit destination", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(DestinationDto.class).ifPresent(DestinationEditDialog::showDialog);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(DestinationDto.class));
    }
}
