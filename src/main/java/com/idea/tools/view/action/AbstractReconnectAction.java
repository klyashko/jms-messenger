package com.idea.tools.view.action;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.task.LoadQueuesTask;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

import static com.idea.tools.settings.Settings.settings;
import static com.idea.tools.utils.IconUtils.getRefreshIcon;

public abstract class AbstractReconnectAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRefreshIcon();

    private final Project project;

    AbstractReconnectAction(Project project, ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Refresh", "", ICON, serversBrowseToolPanel);
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        List<ServerDto> servers = serversPanel.getSelectedValue(ServerDto.class)
                                              .map(Collections::singletonList)
                .orElseGet(settings(project)::getServersList);
        new LoadQueuesTask(project, servers).queue();
    }

    boolean isServerSelected() {
        return isSelected(ServerDto.class);
    }

}
