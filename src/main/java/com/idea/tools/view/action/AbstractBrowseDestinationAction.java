package com.idea.tools.view.action;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.DestinationType;
import com.idea.tools.task.LoadMessagesTask;
import com.idea.tools.view.QueueBrowseToolPanel;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import java.util.Optional;

import static com.idea.tools.utils.GuiUtils.contentFactory;
import static com.idea.tools.utils.GuiUtils.toolWindowManager;
import static com.idea.tools.utils.IconUtils.getBrowseIcon;
import static com.idea.tools.view.QueueBrowseToolPanel.JMS_MESSENGER_BROWSER_ICON;
import static com.idea.tools.view.QueueBrowseToolPanel.JMS_MESSENGER_BROWSER_WINDOW_ID;
import static com.intellij.openapi.wm.ToolWindowAnchor.BOTTOM;

public abstract class AbstractBrowseDestinationAction extends AbstractBrowserPanelAction {

	private static final Icon ICON = getBrowseIcon();
	private final Project project;
	private Optional<QueueBrowseToolPanel> panel = Optional.empty();

	AbstractBrowseDestinationAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
		super("Browse queue", "", ICON, serversBrowseToolPanel);
		this.project = project;
	}

	@Override
	public void actionPerformed(AnActionEvent e) {
		serversPanel.getSelectedValue(DestinationDto.class)
				.filter(d -> DestinationType.QUEUE.equals(d.getType()))
				.ifPresent(queue -> {
					QueueBrowseToolPanel panel = getOrCreate();
					QueueBrowserTable table = panel.addQueueToBrowse(queue);
					new LoadMessagesTask(project, table).queue();
				});
	}

	private QueueBrowseToolPanel getOrCreate() {
		return panel.orElseGet(() -> {
			QueueBrowseToolPanel panel = QueueBrowseToolPanel.of(project);
			Content content = contentFactory().createContent(panel, null, false);
			ToolWindowManager toolWindowManager = toolWindowManager(project);
			ToolWindow toolWindow = toolWindowManager.getToolWindow(JMS_MESSENGER_BROWSER_WINDOW_ID);

			if (toolWindow == null) {
				toolWindow = toolWindowManager.registerToolWindow(JMS_MESSENGER_BROWSER_WINDOW_ID, false, BOTTOM);
				toolWindow.setIcon(JMS_MESSENGER_BROWSER_ICON);
			}

			ContentManager contentManager = toolWindow.getContentManager();
			contentManager.addContent(content);
			this.panel = Optional.of(panel);
			return panel;
		});
	}

	boolean isQueueSelected() {
		return serversPanel.getSelectedValue(DestinationDto.class)
				.filter(d -> DestinationType.QUEUE.equals(d.getType()))
				.isPresent();
	}

}
