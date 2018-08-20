package com.idea.tools.view.action;

import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import javax.swing.*;

import static com.idea.tools.service.TemplateService.templateService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class PopupRemoveTemplateAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();
    private final Project project;

    public PopupRemoveTemplateAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Remove template", "", ICON, serversBrowseToolPanel);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(TemplateMessageDto.class).ifPresent(templateService(project)::remove);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(TemplateMessageDto.class));
    }
}
