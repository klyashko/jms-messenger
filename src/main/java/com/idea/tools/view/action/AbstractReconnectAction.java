package com.idea.tools.view.action;

import static com.idea.tools.settings.Settings.settings;
import static com.idea.tools.utils.IconUtils.getRefreshIcon;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.task.LoadQueuesTask;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractReconnectAction extends AbstractBrowserPanelAction {

	private static final Icon ICON = getRefreshIcon();

	private final Project project;

	AbstractReconnectAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
		super("Refresh", "", ICON, serversBrowseToolPanel);
		this.project = project;
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		List<ServerDto> servers = serversPanel.getSelectedValue(ServerDto.class)
				.map(Collections::singletonList)
				.orElseGet(settings(project)::getServersList);
		LoadQueuesTask task = new LoadQueuesTask(project, servers);
		task.queue();
	}

	boolean isServerSelected() {
		return isSelected(ServerDto.class);
	}

}
