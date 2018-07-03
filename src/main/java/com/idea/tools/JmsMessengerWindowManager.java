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

package com.idea.tools;

import com.idea.tools.view.BrowserPanel;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;

import static com.idea.tools.ApplicationManager.*;
import static com.idea.tools.utils.GuiUtils.icon;
import static com.intellij.openapi.wm.ToolWindowAnchor.RIGHT;

public class JmsMessengerWindowManager {

    public static final String JMS_MESSENGER_WINDOW_ID = "Jms Messenger";
    private static final Icon JMS_MESSENGER_ICON = icon("jms.png");

    public JmsMessengerWindowManager(final Project project) {
        ApplicationManager.setProject(project);

        final BrowserPanel browserPanel = BrowserPanel.of();

        Content content = ContentFactory.SERVICE.getInstance().createContent(browserPanel, null, false);
        ToolWindowManager toolWindowManager = toolWindowManager();
        ToolWindow toolWindow = toolWindowManager.registerToolWindow(JMS_MESSENGER_WINDOW_ID, false, RIGHT);
        toolWindow.setIcon(JMS_MESSENGER_ICON);
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(content);

        final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
//        final JenkinsWidget jenkinsWidget = JenkinsWidget.of(project);
//        statusBar.addWidget(jenkinsWidget);
//        jenkinsWidget.install(statusBar);
//
//        final RssLogic rssLogic = RssLogic.of(project);

        startupManager().registerPostStartupActivity((DumbAwareRunnable) browserPanel::init);
    }

    public static JmsMessengerWindowManager of() {
        return fetch(JmsMessengerWindowManager.class);
    }

    public void unregister() {
        BrowserPanel.of().dispose();
    }

}
