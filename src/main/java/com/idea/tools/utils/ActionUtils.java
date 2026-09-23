package com.idea.tools.utils;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;

public class ActionUtils {

    public static Project getProject(AnActionEvent event) {
        DataContext dataContext = event.getDataContext();
        return CommonDataKeys.PROJECT.getData(dataContext);
    }

}
