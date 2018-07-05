package com.idea.tools.view.action;

import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class ToolBarEditServerAction extends AbstractEditServerAction {

    public ToolBarEditServerAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super(serversBrowseToolPanel);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setEnabled(isServerSelected());
    }

}
