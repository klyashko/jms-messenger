package com.idea.tools.view.action;

import static com.intellij.util.IconUtil.getAddIcon;

import com.idea.tools.view.ServerEditDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class ToolBarAddServerAction extends AnAction implements DumbAware {

    private static final Icon ICON = getAddIcon();
    private final Project project;

    public ToolBarAddServerAction(Project project) {
        super("Add Server", "", ICON);
        this.project = project;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        ServerEditDialog.showDialog(project);
    }

}
