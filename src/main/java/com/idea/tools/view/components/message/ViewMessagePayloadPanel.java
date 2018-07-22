package com.idea.tools.view.components.message;

import com.idea.tools.dto.MessageDto;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.util.Optional;

public class ViewMessagePayloadPanel extends JPanel {

    protected JTextArea payloadField;

    private JPanel rootPanel;
    private Optional<MessageDto> message = Optional.empty();

    public ViewMessagePayloadPanel() {
        this(null);
    }

    public ViewMessagePayloadPanel(MessageDto message) {
        this.message = Optional.ofNullable(message);
        render();
    }

    private void render() {
        setValues();
        payloadField.setLineWrap(true);
        payloadField.setEditable(isEditable());
        add(new JBScrollPane(rootPanel));
    }

    private void setValues() {
        message.ifPresent(m -> payloadField.setText(m.getPayload()));
    }

    protected boolean isEditable() {
        return false;
    }

}
