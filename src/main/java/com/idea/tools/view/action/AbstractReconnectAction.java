package com.idea.tools.view.action;

import com.idea.tools.dto.Server;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;
import java.util.Collections;
import java.util.Optional;

import static com.idea.tools.App.serverService;
import static com.idea.tools.App.settings;
import static com.idea.tools.utils.IconUtils.getRefreshIcon;

public abstract class AbstractReconnectAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getRefreshIcon();

    AbstractReconnectAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Refresh", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Optional<Server> server = serversBrowseToolPanel.getSelectedValue(Server.class);
        if (server.isPresent()) {
            server.ifPresent(s -> serverService().refresh(Collections.singletonList(s)));
        } else {
            serverService().refresh(settings().getServersList());
        }
    }

    boolean isServerSelected() {
        return isSelected(Server.class);
    }

}
