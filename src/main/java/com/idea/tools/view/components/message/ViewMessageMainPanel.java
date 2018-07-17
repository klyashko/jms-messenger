package com.idea.tools.view.components.message;

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

public class ViewMessageMainPanel extends JPanel {

    protected QueueDto queue;
    protected JPanel rootPanel;
    protected JTextField serverField;
    protected JTextField queueField;
    protected JTextField jmsTypeField;
    protected JFormattedTextField timestampField;
    protected JComboBox<ContentType> contentTypeField;
    private MessageDto message;

    public ViewMessageMainPanel(MessageDto message) {
        this(message.getQueue());
        this.message = message;
        this.queue = message.getQueue();
        setMessageValues();
    }

    public ViewMessageMainPanel(QueueDto queue) {
        this.queue = queue;
        render();
    }

    private void render() {
        setLayout(new BorderLayout());

        jmsTypeField.setEditable(isEditable());
        add(createScrollPane(rootPanel), CENTER);

        setQueueValues();
    }

    protected boolean isEditable() {
        return false;
    }

    private void setMessageValues() {
        timestampField.setValue(message.getTimestamp());
        jmsTypeField.setText(message.getJmsType());
        contentTypeField.setSelectedItem(message.getType());
    }

    private void setQueueValues() {
        serverField.setText(queue.getServer().getName());
        queueField.setText(queue.getName());
        timestampField.setValue(System.currentTimeMillis());
    }

    protected void createUIComponents() {
        contentTypeField = new ComboBox<>(new EnumComboBoxModel<>(ContentType.class));
        timestampField = createNumberInputField();
    }
}
