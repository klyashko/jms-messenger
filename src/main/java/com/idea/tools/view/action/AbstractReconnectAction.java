package com.idea.tools.view.action;

import com.idea.tools.dto.Server;
import com.idea.tools.task.LoadQueuesTask;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

import static com.idea.tools.App.settings;
import static com.idea.tools.utils.IconUtils.getRefreshIcon;

public abstract class AbstractReconnectAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRefreshIcon();

    AbstractReconnectAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Refresh", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        List<Server> servers = serversPanel.getSelectedValue(Server.class)
                                           .map(Collections::singletonList)
                                           .orElseGet(settings()::getServersList);
        new LoadQueuesTask(servers).queue();
    }

    boolean isServerSelected() {
        return isSelected(Server.class);
    }

}
