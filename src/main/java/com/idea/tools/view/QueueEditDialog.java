package com.idea.tools.view;

import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.utils.GuiUtils;

import javax.swing.*;

import static com.idea.tools.App.queueService;

public class QueueEditDialog extends JFrame {

    private ServerDto server;
    private QueueDto queue;

    private JPanel rootPanel;
    private JTextField nameField;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField serverField;

    private QueueEditDialog(ServerDto server) {
        this.queue = new QueueDto();
        this.server = server;
        queue.setServer(server);

        render();
    }

    private QueueEditDialog(QueueDto queue) {
        this.queue = queue;
        this.server = queue.getServer();

        render();
    }

    public static void showDialog(ServerDto server) {
        GuiUtils.showDialog(new QueueEditDialog(server), "Queue settings");
    }

    public static void showDialog(QueueDto queue) {
        GuiUtils.showDialog(new QueueEditDialog(queue), "Queue settings");
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
