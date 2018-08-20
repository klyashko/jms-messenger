package com.idea.tools.view.action;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import javax.swing.*;

import static com.idea.tools.service.DestinationService.destinationService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class PopupRemoveDestinationAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();
    private final Project project;

    public PopupRemoveDestinationAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Remove destination", "", ICON, serversBrowseToolPanel);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(DestinationDto.class).ifPresent(destinationService(project)::remove);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(DestinationDto.class));
    }
}
