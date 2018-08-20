package com.idea.tools.view.action;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.view.DestinationEditDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import javax.swing.*;

import static com.intellij.util.IconUtil.getEditIcon;

public class PopupEditDestinationAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getEditIcon();

    private final Project project;

    public PopupEditDestinationAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Edit destination", "", ICON, serversBrowseToolPanel);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(DestinationDto.class).ifPresent(d -> DestinationEditDialog.showDialog(project, d));
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(DestinationDto.class));
    }
}
