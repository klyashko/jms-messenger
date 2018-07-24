package com.idea.tools.view.action;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.App.destinationService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class PopupRemoveDestinationAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();

    public PopupRemoveDestinationAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Remove destination", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(DestinationDto.class).ifPresent(destinationService()::remove);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(DestinationDto.class));
    }
}
