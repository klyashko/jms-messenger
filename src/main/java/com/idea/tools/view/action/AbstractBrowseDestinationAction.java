package com.idea.tools.view.action;

import static com.idea.tools.utils.IconUtils.getBrowseIcon;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.DestinationType;
import com.idea.tools.task.LoadMessagesTask;
import com.idea.tools.view.QueueBrowseToolPanel;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.util.Optional;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBrowseDestinationAction extends AbstractBrowserPanelAction {

	private static final Icon ICON = getBrowseIcon();
	private final Project project;
	private Optional<QueueBrowseToolPanel> panel = Optional.empty();

	AbstractBrowseDestinationAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
		super("Browse queue", "", ICON, serversBrowseToolPanel);
		this.project = project;
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
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
