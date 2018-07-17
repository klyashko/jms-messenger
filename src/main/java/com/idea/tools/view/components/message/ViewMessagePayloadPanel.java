package com.idea.tools.view.components.message;

import com.idea.tools.dto.MessageDto;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.util.Optional;

public class ViewMessagePayloadPanel extends JPanel {

    protected JTextArea payloadField;

    private JPanel rootPanel;
    private Optional<MessageDto> messageDto = Optional.empty();

    public ViewMessagePayloadPanel() {
        this(Optional.empty());
    }

    public ViewMessagePayloadPanel(Optional<MessageDto> messageDto) {
        this.messageDto = messageDto;
        render();
    }

    private void render() {
        setValues();
        payloadField.setLineWrap(true);
        payloadField.setEditable(isEditable());
        add(new JBScrollPane(rootPanel));
    }

    private void setValues() {
        messageDto.ifPresent(m -> payloadField.setText(m.getPayload()));
    }

    protected boolean isEditable() {
        return false;
    }

}
