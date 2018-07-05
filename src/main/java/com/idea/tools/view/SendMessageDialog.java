package com.idea.tools.view;

import com.idea.tools.dto.ContentType;
import com.idea.tools.dto.Message;
import com.idea.tools.dto.Queue;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EnumComboBoxModel;

import javax.swing.*;
import java.awt.*;

import static com.idea.tools.App.queueService;
import static com.idea.tools.utils.GuiUtils.createNumberInputField;
import static com.intellij.ui.ScrollPaneFactory.createScrollPane;
import static java.awt.BorderLayout.CENTER;

public class SendMessageDialog extends JDialog {

    private Queue queue;

    private JPanel rootPanel;
    private JTextField serverField;
    private JTextField queueField;
    private JButton sendButton;
    private JButton closeButton;
    private JTextField jmsTypeField;
    private JTextArea payloadField;
    private JFormattedTextField timestampField;
    private JComboBox<ContentType> contentTypeField;

    private SendMessageDialog(Queue queue) {
        this.queue = queue;
        render();
    }

    public static void showDialog(Queue queue) {
        SwingUtilities.invokeLater(() -> {
            SendMessageDialog dialog = new SendMessageDialog(queue);
            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);
        });
    }

    private void render() {
        setLayout(new BorderLayout());
        add(createScrollPane(rootPanel), CENTER);

        setValues();

        sendButton.addActionListener(event -> {
            Message msg = new Message();
            msg.setTimestamp((Long) timestampField.getValue());
            msg.setJmsType(jmsTypeField.getText());
            msg.setType(contentTypeField.getItemAt(contentTypeField.getSelectedIndex()));
            msg.setPayload(payloadField.getText());
            msg.setQueue(queue);

            queueService().send(msg);
        });

        closeButton.addActionListener(event -> dispose());
    }

    private void setValues() {
        serverField.setText(queue.getServer().getName());
        queueField.setText(queue.getName());
        timestampField.setValue(System.currentTimeMillis());
    }

    private void createUIComponents() {
        contentTypeField = new ComboBox<>(new EnumComboBoxModel<>(ContentType.class));
        timestampField = createNumberInputField();
    }
}
