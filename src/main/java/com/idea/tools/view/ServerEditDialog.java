package com.idea.tools.view;

import com.idea.tools.dto.ServerDto;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;

public class ServerEditDialog extends JFrame {

    private ServerEditDialog(ServerDto server) {
        render(server);
    }

    public static void showDialog() {
        showDialog(new ServerDto());
    }

    public static void showDialog(ServerDto server) {
        SwingUtilities.invokeLater(() -> {
            ServerEditDialog dialog = new ServerEditDialog(server);
            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);
        });
    }

    private void render(ServerDto server) {
        ServerEditPanel panel = new ServerEditPanel(server);
        panel.getCancelButton().addActionListener(event -> dispose());
        panel.getSaveButton().addActionListener(event -> dispose());

        JRootPane rootPane = new JRootPane();
        IdeGlassPaneImpl pane = new IdeGlassPaneImpl(rootPane);
        setGlassPane(pane);

        add(new JBScrollPane(panel));
    }
}
