package com.idea.tools.view.action;

import com.idea.tools.dto.QueueDto;
import com.idea.tools.task.LoadMessagesTask;
import com.idea.tools.view.QueueBrowseToolPanel;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import java.util.Optional;

import static com.idea.tools.App.contentFactory;
import static com.idea.tools.App.toolWindowManager;
import static com.idea.tools.utils.IconUtils.getBrowseIcon;
import static com.idea.tools.view.QueueBrowseToolPanel.JMS_MESSENGER_BROWSER_ICON;
import static com.idea.tools.view.QueueBrowseToolPanel.JMS_MESSENGER_BROWSER_WINDOW_ID;
import static com.intellij.openapi.wm.ToolWindowAnchor.BOTTOM;

public abstract class AbstractBrowseQueueAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getBrowseIcon();
    private Optional<QueueBrowseToolPanel> panel = Optional.empty();

    AbstractBrowseQueueAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Browse queue", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversBrowseToolPanel.getSelectedValue(QueueDto.class)
                              .ifPresent(queue -> {
                                  QueueBrowseToolPanel panel = getOrCreate();
                                  QueueBrowserTable table = panel.addQueueToBrowse(queue);
                                  new LoadMessagesTask(table, queue).queue();
                              });
    }

    private QueueBrowseToolPanel getOrCreate() {
        return panel.orElseGet(() -> {
            QueueBrowseToolPanel panel = QueueBrowseToolPanel.of();
            Content content = contentFactory().createContent(panel, null, false);
            ToolWindowManager toolWindowManager = toolWindowManager();
            ToolWindow toolWindow = toolWindowManager.registerToolWindow(JMS_MESSENGER_BROWSER_WINDOW_ID, false, BOTTOM);
            toolWindow.setIcon(JMS_MESSENGER_BROWSER_ICON);
            ContentManager contentManager = toolWindow.getContentManager();
            contentManager.addContent(content);
            this.panel = Optional.of(panel);
            return panel;
        });
    }

    boolean isQueueSelected() {
        return isSelected(QueueDto.class);
    }

}
