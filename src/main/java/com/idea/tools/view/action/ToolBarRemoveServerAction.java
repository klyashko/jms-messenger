package com.idea.tools.view.action;

import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class ToolBarRemoveServerAction extends AbstractRemoveServerAction {

    public ToolBarRemoveServerAction(BrowserPanel browserPanel) {
        super(browserPanel);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setEnabled(isServerSelected());
    }
}
