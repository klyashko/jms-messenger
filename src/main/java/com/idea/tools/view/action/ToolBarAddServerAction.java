package com.idea.tools.view.action;

import com.idea.tools.view.ServerEditDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;

import javax.swing.*;
import java.util.Optional;

import static com.intellij.util.IconUtil.getAddIcon;

public class ToolBarAddServerAction extends AnAction implements DumbAware {

    private static final Icon ICON = getAddIcon();

    public ToolBarAddServerAction() {
        super("Add server", "", ICON);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        ServerEditDialog.showDialog(Optional.empty());
    }

}
