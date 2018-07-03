package com.idea.tools.view.action;

import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;

import javax.swing.*;

import static com.idea.tools.utils.GuiUtils.icon;

public class RemoveServerAction extends AnAction implements DumbAware {

    private static final Icon ICON = icon("minus.png");
    private final BrowserPanel browserPanel;

    public RemoveServerAction(BrowserPanel browserPanel) {
        super("Remove server", "", ICON);
        this.browserPanel = browserPanel;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {

    }
}
