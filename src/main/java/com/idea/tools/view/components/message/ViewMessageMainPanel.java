package com.idea.tools.view.components.message;

import com.idea.tools.dto.ContentType;
import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
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
    protected JTextField destinationField;
    protected JTextField jmsTypeField;
    protected JFormattedTextField timestampField;
    protected JComboBox<ContentType> contentTypeField;
    protected DestinationDto destination;
    private Optional<MessageDto> message = Optional.empty();

    public ViewMessageMainPanel(MessageDto message) {
        this(null, message);
    }

    public ViewMessageMainPanel(DestinationDto destination) {
        this(destination, null);
    }

    private ViewMessageMainPanel(DestinationDto queue, MessageDto message) {
        this.message = Optional.ofNullable(message);
        this.destination = this.message.map(MessageDto::getDestination).orElse(queue);
        render();
    }

    public void fillMessage(MessageDto dto) {
        dto.setTimestamp((Long) timestampField.getValue());
        dto.setJmsType(jmsTypeField.getText());
        dto.setType(contentTypeField.getItemAt(contentTypeField.getSelectedIndex()));
        dto.setDestination(destination);
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
        serverField.setText(destination.getServer().getName());
        destinationField.setText(destination.getName());
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
