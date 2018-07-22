package com.idea.tools.view.components.message;

import com.idea.tools.dto.ContentType;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EnumComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static com.idea.tools.utils.GuiUtils.createNumberInputField;
import static com.intellij.ui.ScrollPaneFactory.createScrollPane;
import static java.awt.BorderLayout.CENTER;

public class ViewMessageMainPanel extends JPanel {

    protected JPanel rootPanel;
    protected JTextField serverField;
    protected JTextField queueField;
    protected JTextField jmsTypeField;
    protected JFormattedTextField timestampField;
    protected JComboBox<ContentType> contentTypeField;
    protected QueueDto queue;
    private Optional<MessageDto> message = Optional.empty();

    public ViewMessageMainPanel(MessageDto message) {
        this(null, message);
    }

    public ViewMessageMainPanel(QueueDto queue) {
        this(queue, null);
    }

    private ViewMessageMainPanel(QueueDto queue, MessageDto message) {
        this.message = Optional.ofNullable(message);
        this.queue = this.message.map(MessageDto::getQueue).orElse(queue);
        render();
    }

    public void fillMessage(MessageDto dto) {
        dto.setTimestamp((Long) timestampField.getValue());
        dto.setJmsType(jmsTypeField.getText());
        dto.setType(contentTypeField.getItemAt(contentTypeField.getSelectedIndex()));
        dto.setQueue(queue);
    }

    private void render() {
        setLayout(new BorderLayout());

        jmsTypeField.setEditable(!isReadOnly());
        add(createScrollPane(rootPanel), CENTER);

        setValues();
    }

    protected boolean isReadOnly() {
        return true;
    }

    private void setValues() {
        serverField.setText(queue.getServer().getName());
        queueField.setText(queue.getName());
        timestampField.setValue(System.currentTimeMillis());
        message.ifPresent(msg -> {
            timestampField.setValue(msg.getTimestamp());
            jmsTypeField.setText(msg.getJmsType());
            contentTypeField.setSelectedItem(msg.getType());
        });
    }

    protected void createUIComponents() {
        contentTypeField = new ComboBox<>(new EnumComboBoxModel<>(ContentType.class));
        timestampField = createNumberInputField();
    }
}
