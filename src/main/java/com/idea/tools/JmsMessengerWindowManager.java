package com.idea.tools;

import static com.idea.tools.utils.GuiUtils.contentFactory;
import static com.idea.tools.utils.GuiUtils.startupManager;
import static com.idea.tools.utils.IconUtils.getJmsIcon;

import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class JmsMessengerWindowManager implements ToolWindowFactory {

    private static final Icon JMS_MESSENGER_ICON = getJmsIcon();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ServersBrowseToolPanel serversBrowseToolPanel = ServersBrowseToolPanel.of(project);

        Content content = contentFactory().createContent(serversBrowseToolPanel, null, false);

        toolWindow.setIcon(JMS_MESSENGER_ICON);
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(content);

        startupManager(project).registerPostStartupActivity((DumbAwareRunnable) serversBrowseToolPanel::init);
    }
}
