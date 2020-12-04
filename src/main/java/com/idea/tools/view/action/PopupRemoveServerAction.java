package com.idea.tools.view.action;

import static com.idea.tools.service.ServerService.serverService;
import static com.intellij.util.IconUtil.getRemoveIcon;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import javax.swing.*;

public class PopupRemoveServerAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();
    private final Project project;

    public PopupRemoveServerAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Remove server", "", ICON, serversBrowseToolPanel);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(ServerDto.class).ifPresent(serverService(project)::remove);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(ServerDto.class));
    }
}
