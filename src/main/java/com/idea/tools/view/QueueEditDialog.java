package com.idea.tools.view;

import com.idea.tools.dto.Queue;
import com.idea.tools.dto.Server;

import javax.swing.*;

import static com.idea.tools.App.queueService;

public class QueueEditDialog extends JDialog {

    private Server server;
    private Queue queue;

    private JPanel rootPanel;
    private JTextField nameField;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField serverField;

    private QueueEditDialog(Server server) {
        this.queue = new Queue();
        this.server = server;
        queue.setServer(server);

        render();
    }

    private QueueEditDialog(Queue queue) {
        this.queue = queue;
        this.server = queue.getServer();

        render();
    }

    public static void showDialog(Server server) {
        showDialog(new QueueEditDialog(server));
    }

    public static void showDialog(Queue queue) {
        showDialog(new QueueEditDialog(queue));
    }

    private static void showDialog(QueueEditDialog dialog) {
        SwingUtilities.invokeLater(() -> {
            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);
        });
    }

    private void render() {
        add(rootPanel);

        setValues();

        saveButton.addActionListener(event -> {
            queue.setName(nameField.getText());
            queue.setAddedManually(true);
            queueService().saveOrUpdate(queue);
            dispose();
        });
        cancelButton.addActionListener(event -> dispose());
    }

    private void setValues() {
        serverField.setText(server.getName());
        nameField.setText(queue.getName());
    }

}
