/*
 * Copyright (c) 2013 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idea.tools.view.action;


import com.idea.tools.JmsMessengerComponent;
import com.idea.tools.utils.ActionUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;

import static com.idea.tools.utils.GuiUtils.icon;

public class OpenPluginSettingsAction extends AnAction implements DumbAware {


    private static final Icon SETTINGS_ICON = UIUtil.isUnderDarcula() ? icon("settings_dark.png") : icon("settings.png");

    public OpenPluginSettingsAction() {
        super("Messenger Settings", "Edit the Messenger settings for the current project", SETTINGS_ICON);
    }

    private static void showSettingsFor(Project project) {
        System.out.println("before");
        ShowSettingsUtil.getInstance().showSettingsDialog(project, JmsMessengerComponent.class);
        System.out.println("after");
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        showSettingsFor(ActionUtils.getProject(event));
    }
}
