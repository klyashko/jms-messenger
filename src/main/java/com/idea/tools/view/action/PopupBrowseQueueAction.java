package com.idea.tools.view.action;

import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class PopupBrowseQueueAction extends AbstractBrowseQueueAction {

    public PopupBrowseQueueAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super(serversBrowseToolPanel);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(isQueueSelected());
    }
}
