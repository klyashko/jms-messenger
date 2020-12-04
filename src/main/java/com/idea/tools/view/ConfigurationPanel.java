package com.idea.tools.view;

import static com.idea.tools.service.ServerService.serverService;
import static com.idea.tools.settings.Settings.settings;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.view.components.ServiceConfigTable;
import com.intellij.openapi.project.Project;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class ConfigurationPanel {

    private final Project project;

    private JPanel rootPanel;
    private ServiceConfigTable table;
    private ServerEditPanel panel;

    public ConfigurationPanel(Project project) {
        this.project = project;
        render();
    }

    private void render() {
        this.panel = new ServerEditPanel(project);
        rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(renderTable(), BorderLayout.WEST);
        rootPanel.add(panel, BorderLayout.CENTER);
    }

    @NotNull
    private ServiceConfigTable renderTable() {
        List<ServerDto> data = settings(project).getServersList();
        Collections.sort(data);
        table = new ServiceConfigTable(project, data, this);
        serverService(project).addListener(table);
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
        serverService(project).removeListener(table);
    }
}
