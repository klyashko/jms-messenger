package com.idea.tools.view.components;

import com.idea.tools.dto.ContentType;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EnumComboBoxModel;

import javax.swing.*;
import java.awt.*;

import static com.idea.tools.utils.GuiUtils.createNumberInputField;
import static com.intellij.ui.ScrollPaneFactory.createScrollPane;
import static java.awt.BorderLayout.CENTER;

public class MessageMainPanel extends JPanel {

    private QueueDto queue;

    private JPanel rootPanel;
    private JTextField serverField;
    private JTextField queueField;
    private JTextField jmsTypeField;
    private JFormattedTextField timestampField;
    private JComboBox<ContentType> contentTypeField;

    public MessageMainPanel(QueueDto queue) {
        this.queue = queue;
        render();
    }

    private void render() {
        setLayout(new BorderLayout());
        add(createScrollPane(rootPanel), CENTER);

        setValues();
    }

    public void fillMessage(MessageDto dto) {
        dto.setTimestamp((Long) timestampField.getValue());
        dto.setJmsType(jmsTypeField.getText());
        dto.setType(contentTypeField.getItemAt(contentTypeField.getSelectedIndex()));
        dto.setQueue(queue);
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
