package com.idea.tools.view.action;

import static com.idea.tools.utils.IconUtils.getSendMessageIcon;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.view.SendMessageDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.util.Optional;
import javax.swing.*;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class AbstractSendMessageAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getSendMessageIcon();
    @Getter(AccessLevel.PROTECTED)
    private final Project project;

    AbstractSendMessageAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Send message", "", ICON, serversBrowseToolPanel);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Optional<DestinationDto> destination = serversPanel.getSelectedValue(DestinationDto.class);
        if (destination.isPresent()) {
            destination.ifPresent(d -> SendMessageDialog.showDialog(project, d));
        } else {
            serversPanel.getSelectedValue(TemplateMessageDto.class).ifPresent(t -> SendMessageDialog.showDialog(project, t));
        }
    }

    boolean isDestinationOrTemplateSelected() {
        return isSelected(DestinationDto.class) || isSelected(TemplateMessageDto.class);
    }

}
