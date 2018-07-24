package com.idea.tools.view.action;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.App.*;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class ToolBarRemoveAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();

    public ToolBarRemoveAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Remove", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (isSelected(ServerDto.class)) {
            serversPanel.getSelectedValue(ServerDto.class).ifPresent(serverService()::remove);
        } else if (isSelected(DestinationDto.class)) {
            serversPanel.getSelectedValue(DestinationDto.class).ifPresent(destinationService()::remove);
        } else if (isSelected(TemplateMessageDto.class)) {
            serversPanel.getSelectedValue(TemplateMessageDto.class).ifPresent(templateService()::remove);
        }
    }

    @Override
    public void update(AnActionEvent event) {
        boolean enable = isSelected(ServerDto.class) || isSelected(DestinationDto.class) || isSelected(TemplateMessageDto.class);
        event.getPresentation().setEnabled(enable);
    }
}
