package com.idea.tools.view.components.message;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;

public class SendMessageMainPanel extends ViewMessageMainPanel {

    public SendMessageMainPanel(QueueDto queue) {
        super(queue);
    }

    public void fillMessage(MessageDto dto) {
        dto.setTimestamp((Long) timestampField.getValue());
        dto.setJmsType(jmsTypeField.getText());
        dto.setType(contentTypeField.getItemAt(contentTypeField.getSelectedIndex()));
        dto.setQueue(queue);
    }

    @Override
    protected boolean isEditable() {
        return true;
    }
}
