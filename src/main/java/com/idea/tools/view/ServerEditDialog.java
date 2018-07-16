package com.idea.tools.view;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.view.components.ServerEditPanel;

import javax.swing.*;
import java.util.Optional;

public class ServerEditDialog extends JDialog {

    private ServerEditDialog(ServerDto server) {
        ServerEditPanel panel = new ServerEditPanel(server);
        add(panel);
        panel.getCancelButton().addActionListener(event -> dispose());
        panel.getSaveButton().addActionListener(event -> dispose());
    }

    public static void showDialog(Optional<ServerDto> server) {
        SwingUtilities.invokeLater(() -> {
            ServerEditDialog dialog = new ServerEditDialog(server.orElseGet(ServerDto::new));
            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);
        });
    }
}
