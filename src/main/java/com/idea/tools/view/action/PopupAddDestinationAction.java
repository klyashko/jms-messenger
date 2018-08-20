package com.idea.tools.view.action;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.view.DestinationEditDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import javax.swing.*;

import static com.intellij.util.IconUtil.getAddIcon;

public class PopupAddDestinationAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getAddIcon();

    private final Project project;

    public PopupAddDestinationAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Add destination", "", ICON, serversBrowseToolPanel);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(ServerDto.class).ifPresent(s -> DestinationEditDialog.showDialog(project, s));
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(ServerDto.class));
    }
}
