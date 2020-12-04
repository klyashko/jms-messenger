package com.idea.tools.view.action;

import static com.intellij.util.IconUtil.getEditIcon;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.view.ServerEditDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import javax.swing.*;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class AbstractEditServerAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getEditIcon();
    @Getter(AccessLevel.PROTECTED)
    private final Project project;

    AbstractEditServerAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Edit server", "", ICON, serversBrowseToolPanel);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(ServerDto.class).ifPresent(s -> ServerEditDialog.showDialog(project, s));
    }

    boolean isServerSelected() {
        return isSelected(ServerDto.class);
    }

}
