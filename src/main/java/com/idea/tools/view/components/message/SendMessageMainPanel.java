package com.idea.tools.view.components.message;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;

public class SendMessageMainPanel extends ViewMessageMainPanel {

    public SendMessageMainPanel(MessageDto message) {
        super(message);
    }

    public SendMessageMainPanel(QueueDto queue) {
        super(queue);
    }

    @Override
    protected boolean isReadOnly() {
        return false;
    }
}
