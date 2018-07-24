package com.idea.tools.view.action;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.view.SendMessageDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;
import java.util.Optional;

import static com.idea.tools.utils.IconUtils.getSendMessageIcon;

public abstract class AbstractSendMessageAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getSendMessageIcon();

    AbstractSendMessageAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Send message", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Optional<DestinationDto> destination = serversPanel.getSelectedValue(DestinationDto.class);
        if (destination.isPresent()) {
            destination.ifPresent(SendMessageDialog::showDialog);
        } else {
            serversPanel.getSelectedValue(TemplateMessageDto.class).ifPresent(SendMessageDialog::showDialog);
        }
    }

    boolean isDestinationOrTemplateSelected() {
        return isSelected(DestinationDto.class) || isSelected(TemplateMessageDto.class);
    }

}
