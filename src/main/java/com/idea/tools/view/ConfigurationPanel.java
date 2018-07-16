package com.idea.tools.view;

import com.idea.tools.App;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.view.components.ServerEditPanel;
import com.idea.tools.view.components.ServiceConfigTable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.idea.tools.App.serverService;
import static com.idea.tools.App.settings;

public class ConfigurationPanel {

    private JPanel rootPanel;
    private ServiceConfigTable table;
    private ServerEditPanel panel;

    public ConfigurationPanel(Project project) {
        App.setProject(project);
        render();
    }

    private void render() {
        this.panel = new ServerEditPanel();
        rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(renderTable(), BorderLayout.WEST);
        rootPanel.add(panel, BorderLayout.CENTER);
    }

    @NotNull
    private ServiceConfigTable renderTable() {
        List<ServerDto> data = settings().getServersList();
        table = new ServiceConfigTable(data, this);
        serverService().addListener(table);
        return table;
    }

    public void editServer(ServerDto server) {
        if (server != null) {
            panel.setNewValue(server);
        } else {
            panel.setNewValue(new ServerDto());
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
    }

    public void dispose() {
        serverService().removeListener(table);
    }
}
