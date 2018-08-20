package com.idea.tools.view.action;

import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.idea.tools.view.TemplateEditMessageDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import javax.swing.*;

import static com.intellij.util.IconUtil.getEditIcon;

public class PopupEditTemplateAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getEditIcon();
    private final Project project;

    public PopupEditTemplateAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Edit template", "", ICON, serversBrowseToolPanel);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(TemplateMessageDto.class).ifPresent(t -> TemplateEditMessageDialog.showDialog(project, t));
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(TemplateMessageDto.class));
    }
}
