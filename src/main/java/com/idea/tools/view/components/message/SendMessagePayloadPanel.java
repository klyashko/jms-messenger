package com.idea.tools.view.components.message;

import com.idea.tools.dto.MessageDto;

public class SendMessagePayloadPanel extends ViewMessagePayloadPanel {

    public SendMessagePayloadPanel(MessageDto message) {
        super(message);
    }

    public SendMessagePayloadPanel() { }

    @Override
    protected boolean isEditable() {
        return true;
    }

}
