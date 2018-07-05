package com.idea.tools.view;

import com.idea.tools.dto.Server;
import com.idea.tools.view.components.ServerEditPanel;

import javax.swing.*;

public class ServerEditDialog extends JDialog {

    private final ServerEditPanel panel;

    private ServerEditDialog() {
        this(new Server());
    }

    private ServerEditDialog(Server server) {
        this.panel = new ServerEditPanel(server);
        add(panel);
        panel.getCancelButton().addActionListener(event -> dispose());
        panel.getSaveButton().addActionListener(event -> dispose());
    }

    public static void showDialog(Server server) {
        SwingUtilities.invokeLater(() -> {
            ServerEditDialog dialog = new ServerEditDialog(server);
            dialog.setLocationRelativeTo(null);
//                dialog.setMaximumSize(new Dimension(300, 200));
            dialog.pack();
            dialog.setVisible(true);
        });
    }
}
