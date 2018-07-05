/*
 * Copyright (c) 2013 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idea.tools.view;

import com.idea.tools.App;
import com.idea.tools.dto.Server;
import com.idea.tools.view.components.ServerEditPanel;
import com.idea.tools.view.components.ServiceConfigTable;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AddEditRemovePanel;
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
        List<Server> data = settings().getServersList();
        AddEditRemovePanel.TableModel<Server> model = new ServiceConfigTable.MyTableModel();
        ServiceConfigTable table = new ServiceConfigTable(model, data, this);
        this.table = table;
        serverService().addListener(table);
        return table;
    }

    public void editServer(Server server) {
        if (server != null) {
            panel.setNewValue(server);
        } else {
            panel.setNewValue(new Server());
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void dispose() {
        serverService().removeListener(table);
    }
}
