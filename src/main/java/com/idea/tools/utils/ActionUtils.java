package com.idea.tools.utils;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

public class ActionUtils {

    public static Project getProject(AnActionEvent event) {
        DataContext dataContext = event.getDataContext();
        return PlatformDataKeys.PROJECT.getData(dataContext);
    }

}
