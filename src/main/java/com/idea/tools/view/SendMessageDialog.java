package com.idea.tools.view;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.utils.GuiUtils;
import com.idea.tools.view.components.message.*;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

import static com.idea.tools.App.jmsService;

public class SendMessageDialog extends ViewMessageDialog {

    private static final Logger LOGGER = Logger.getInstance(SendMessageDialog.class);

    private static final String SEND_SUCCESS_TEMPLATE = "Message has been successfully sent to queue %s";
    private static final String SEND_FAIL_TEMPLATE = "Message hasn't been sent. Reason: \n%s";

    private SendMessageMainPanel mainPanel;
    private SendMessagePayloadPanel payloadPanel;
    private SendMessageHeadersPanel headersPanel;

    private SendMessageDialog(MessageDto message) {
        super(message);
        render();
    }

    private SendMessageDialog(QueueDto queue) {
        super(queue);
        render();
    }

    private void render() {
        sendButton.setVisible(true);
        closeAfterSendCheckBox.setVisible(true);

        sendButton.addActionListener(event -> {
            MessageDto msg = new MessageDto();
            mainPanel.fillMessage(msg);
            headersPanel.fillMessage(msg);
            payloadPanel.fillMessage(msg);

            try {
                jmsService().send(msg);
                String content = String.format(SEND_SUCCESS_TEMPLATE, msg.getQueue().getName());
                Notifications.Bus.notify(new Notification("jms", "Success", content, NotificationType.INFORMATION));
                if (closeAfterSendCheckBox.isSelected()) {
                    dispose();
                }
            } catch (Exception ex) {
                String content = String.format(SEND_FAIL_TEMPLATE, ex.getMessage());
                Notifications.Bus.notify(new Notification("jms", "Failure", content, NotificationType.ERROR));
                LOGGER.error("An exception has been thrown during a message sending", ex);
            }
        });
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

        mainPanel = message.map(SendMessageMainPanel::new)
                .orElseGet(() -> queue.map(SendMessageMainPanel::new)
                        .orElseThrow(runtimeException));
        return mainPanel;
    }

    @Override
    protected ViewMessageHeadersPanel headersPanel(Optional<QueueDto> queue, Optional<MessageDto> message) {
        headersPanel = message.map(msg -> new SendMessageHeadersPanel(msg.getHeaders()))
                .orElseGet(() -> new SendMessageHeadersPanel(new ArrayList<>()));
        return headersPanel;
    }

    @Override
    protected ViewMessagePayloadPanel payloadPanel(Optional<QueueDto> queue, Optional<MessageDto> message) {
        payloadPanel = new SendMessagePayloadPanel();
        return payloadPanel;
    }

}
