package com.idea.tools.view;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.task.SendMessageTask;
import com.idea.tools.utils.GuiUtils;
import com.idea.tools.view.components.message.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

public class SendMessageDialog extends ViewMessageDialog {

    protected SendMessageDialog(MessageDto message) {
        super(message);
        render();
    }

    private SendMessageDialog(QueueDto queue) {
        super(queue);
        render();
    }

    private void render() { }

    @Override
    protected void actionButton(JButton actionButton) {
        actionButton.addActionListener(event -> {
            MessageDto msg = new MessageDto();
            fillMessage(msg);
            new SendMessageTask(msg, () -> {
                if (closeAfterSendCheckBox.isSelected()) {
                    dispose();
                }
            }).queue();
        });
    }

    @Override
    protected void closeAfterSendCheckBox(JCheckBox closeAfterSendCheckBox) {
    }

    public static void showDialog(QueueDto queue) {
        GuiUtils.showDialog(new SendMessageDialog(queue), "Send message");
    }

    public static void showDialog(MessageDto message) {
        GuiUtils.showDialog(new SendMessageDialog(message), "Send message");
    }

    @Override
    protected ViewMessageMainPanel mainPanel(Optional<QueueDto> queue, Optional<MessageDto> message) {
        Supplier<RuntimeException> runtimeException =
                () -> new IllegalArgumentException("MainPanel may not be initialized neither message or queue is present");

        return message.map(SendMessageMainPanel::new)
                .orElseGet(() -> queue.map(SendMessageMainPanel::new)
                        .orElseThrow(runtimeException));
    }

    @Override
    protected ViewMessageHeadersPanel headersPanel(Optional<QueueDto> queue, Optional<MessageDto> message) {
        return message.map(msg -> new SendMessageHeadersPanel(msg.getHeaders()))
                .orElseGet(() -> new SendMessageHeadersPanel(new ArrayList<>()));
    }

    @Override
    protected ViewMessagePayloadPanel payloadPanel(Optional<QueueDto> queue, Optional<MessageDto> message) {
        return message.map(SendMessagePayloadPanel::new).orElseGet(SendMessagePayloadPanel::new);
    }

}
