package com.idea.tools;

import static com.idea.tools.utils.GuiUtils.contentFactory;
import static com.idea.tools.view.QueueBrowseToolPanel.JMS_MESSENGER_BROWSER_ICON;

import com.idea.tools.view.QueueBrowseToolPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

public class JmsMessengerBrowserWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        toolWindow.setIcon(JMS_MESSENGER_BROWSER_ICON);

        QueueBrowseToolPanel panel = QueueBrowseToolPanel.of(project);
        Content content = contentFactory().createContent(panel, null, false);
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(content);
    }
}
