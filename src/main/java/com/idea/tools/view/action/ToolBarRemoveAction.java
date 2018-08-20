package com.idea.tools.view.action;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import javax.swing.*;

import static com.idea.tools.service.DestinationService.destinationService;
import static com.idea.tools.service.ServerService.serverService;
import static com.idea.tools.service.TemplateService.templateService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class ToolBarRemoveAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();
    private final Project project;

    public ToolBarRemoveAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Remove", "", ICON, serversBrowseToolPanel);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (isSelected(ServerDto.class)) {
            serversPanel.getSelectedValue(ServerDto.class).ifPresent(serverService(project)::remove);
        } else if (isSelected(DestinationDto.class)) {
            serversPanel.getSelectedValue(DestinationDto.class).ifPresent(destinationService(project)::remove);
        } else if (isSelected(TemplateMessageDto.class)) {
            serversPanel.getSelectedValue(TemplateMessageDto.class).ifPresent(templateService(project)::remove);
        }
    }

    @Override
    public void update(AnActionEvent event) {
        boolean enable = isSelected(ServerDto.class) || isSelected(DestinationDto.class) || isSelected(TemplateMessageDto.class);
        event.getPresentation().setEnabled(enable);
    }
}
