package com.idea.tools.view;

import com.idea.tools.dto.Server;
import com.idea.tools.view.components.ServerEditPanel;

import javax.swing.*;
import java.util.Optional;

public class ServerEditDialog extends JDialog {

    private ServerEditDialog(Server server) {
        ServerEditPanel panel = new ServerEditPanel(server);
        add(panel);
        panel.getCancelButton().addActionListener(event -> dispose());
        panel.getSaveButton().addActionListener(event -> dispose());
    }

    public static void showDialog(Optional<Server> server) {
        SwingUtilities.invokeLater(() -> {
            ServerEditDialog dialog = new ServerEditDialog(server.orElseGet(Server::new));
            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);
        });
    }
}
