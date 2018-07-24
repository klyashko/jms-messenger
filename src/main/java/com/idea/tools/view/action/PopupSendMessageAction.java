package com.idea.tools.view.action;

import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class PopupSendMessageAction extends AbstractSendMessageAction {

    public PopupSendMessageAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super(serversBrowseToolPanel);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isDestinationOrTemplateSelected());
    }

}
