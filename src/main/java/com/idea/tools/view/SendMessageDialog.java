package com.idea.tools.view;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.view.components.message.*;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;

import javax.swing.*;
import java.util.ArrayList;

import static com.idea.tools.App.jmsService;

public class SendMessageDialog extends ViewMessageDialog {

    private static final Logger LOGGER = Logger.getInstance(SendMessageDialog.class);

    private static final String SEND_SUCCESS_TEMPLATE = "Message has been successfully sent to queue %s";
    private static final String SEND_FAIL_TEMPLATE = "Message hasn't been sent. Reason: \n%s";

    private SendMessageMainPanel mainPanel;
    private SendMessagePayloadPanel payloadPanel;
    private SendMessageHeadersPanel headersPanel;

    private SendMessageDialog(QueueDto queue) {
        super(queue);
        render();
    }

    public static void showDialog(QueueDto queue) {
        SwingUtilities.invokeLater(() -> {
            ViewMessageDialog dialog = new SendMessageDialog(queue);
            dialog.setLocationRelativeTo(null);
            dialog.setTitle("Send message");
            dialog.pack();
            dialog.setVisible(true);
        });
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
                ex.printStackTrace();
            }
        });
    }

    protected ViewMessageMainPanel mainPanel(QueueDto queue) {
        mainPanel = new SendMessageMainPanel(queue);
        return mainPanel;
    }

    protected ViewMessageHeadersPanel headersPanel() {
        headersPanel = new SendMessageHeadersPanel(new ArrayList<>());
        return headersPanel;
    }

    protected ViewMessagePayloadPanel payloadPanel() {
        payloadPanel = new SendMessagePayloadPanel();
        return payloadPanel;
    }
}
