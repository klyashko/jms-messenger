package com.idea.tools.view;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
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

    private SendMessageDialog(DestinationDto destination) {
        super(destination);
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

    public static void showDialog(DestinationDto queue) {
        GuiUtils.showDialog(new SendMessageDialog(queue), "Send message");
    }

    public static void showDialog(MessageDto message) {
        GuiUtils.showDialog(new SendMessageDialog(message), "Send message");
    }

    @Override
    protected ViewMessageMainPanel mainPanel(Optional<DestinationDto> destination, Optional<MessageDto> message) {
        Supplier<RuntimeException> runtimeException =
                () -> new IllegalArgumentException("MainPanel may not be initialized neither message or destination is present");

        return message.map(SendMessageMainPanel::new)
                .orElseGet(() -> destination.map(SendMessageMainPanel::new)
                        .orElseThrow(runtimeException));
    }

    @Override
    protected ViewMessageHeadersPanel headersPanel(Optional<DestinationDto> destination, Optional<MessageDto> message) {
        return message.map(msg -> new SendMessageHeadersPanel(msg.getHeaders()))
                .orElseGet(() -> new SendMessageHeadersPanel(new ArrayList<>()));
    }

    @Override
    protected ViewMessagePayloadPanel payloadPanel(Optional<DestinationDto> destination, Optional<MessageDto> message) {
        return message.map(SendMessagePayloadPanel::new).orElseGet(SendMessagePayloadPanel::new);
    }

}
