package com.idea.tools.view.action;

import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;

import static com.idea.tools.App.templateService;
import static com.intellij.util.IconUtil.getRemoveIcon;

public class PopupRemoveTemplateAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRemoveIcon();

    public PopupRemoveTemplateAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Remove template", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversPanel.getSelectedValue(TemplateMessageDto.class).ifPresent(templateService()::remove);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isSelected(TemplateMessageDto.class));
    }
}
