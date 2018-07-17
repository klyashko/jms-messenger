package com.idea.tools.view.components.message;

import com.idea.tools.dto.MessageDto;

public class SendMessagePayloadPanel extends ViewMessagePayloadPanel {

    public SendMessagePayloadPanel() {
        super();
    }

    @Override
    protected boolean isEditable() {
        return true;
    }

    public void fillMessage(MessageDto dto) {
        dto.setPayload(payloadField.getText());
    }
}
