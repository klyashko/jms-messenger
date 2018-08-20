package com.idea.tools;

import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;

import static com.idea.tools.utils.GuiUtils.*;
import static com.idea.tools.utils.IconUtils.getJmsIcon;
import static com.intellij.openapi.wm.ToolWindowAnchor.RIGHT;

public class JmsMessengerWindowManager {

    private static final String JMS_MESSENGER_WINDOW_ID = "Jms Messenger";
    private static final Icon JMS_MESSENGER_ICON = getJmsIcon();

    private final Project project;

    public JmsMessengerWindowManager(Project project) {
        this.project = project;
        ServersBrowseToolPanel serversBrowseToolPanel = ServersBrowseToolPanel.of(project);

        Content content = contentFactory().createContent(serversBrowseToolPanel, null, false);
        ToolWindowManager toolWindowManager = toolWindowManager(project);
        ToolWindow toolWindow = toolWindowManager.registerToolWindow(JMS_MESSENGER_WINDOW_ID, false, RIGHT);
        toolWindow.setIcon(JMS_MESSENGER_ICON);
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(content);

        startupManager(project).registerPostStartupActivity((DumbAwareRunnable) serversBrowseToolPanel::init);
    }

    public static JmsMessengerWindowManager of(Project project) {
        return ServiceManager.getService(project, JmsMessengerWindowManager.class);
    }

    public void unregister() {
        ServersBrowseToolPanel.of(project).dispose();
    }

}
