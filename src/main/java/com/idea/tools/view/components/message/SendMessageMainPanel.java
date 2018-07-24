package com.idea.tools.view.components.message;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;

public class SendMessageMainPanel extends ViewMessageMainPanel {

    public SendMessageMainPanel(MessageDto message) {
        super(message);
    }

    public SendMessageMainPanel(DestinationDto destination) {
        super(destination);
    }

    @Override
    protected boolean isReadOnly() {
        return false;
    }
}
