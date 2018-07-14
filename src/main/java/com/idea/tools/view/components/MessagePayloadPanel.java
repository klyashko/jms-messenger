package com.idea.tools.view.components;

import com.idea.tools.dto.MessageDto;

import javax.swing.*;

public class MessagePayloadPanel extends JPanel {

    private JPanel rootPanel;
    private JTextArea payloadField;

    public MessagePayloadPanel() {
        render();
    }

    private void render() {
        add(rootPanel);
    }

    public void fillMessage(MessageDto dto) {
        dto.setPayload(payloadField.getText());
    }

}
