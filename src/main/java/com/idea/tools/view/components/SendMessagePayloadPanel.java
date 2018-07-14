package com.idea.tools.view.components;

import com.idea.tools.dto.MessageDto;

import javax.swing.*;

public class SendMessagePayloadPanel extends JPanel {

    private JPanel rootPanel;
    private JTextArea payloadField;

    public SendMessagePayloadPanel() {
        render();
    }

    private void render() {
        add(rootPanel);
    }

    public void fillMessage(MessageDto dto) {
        dto.setPayload(payloadField.getText());
    }

}
