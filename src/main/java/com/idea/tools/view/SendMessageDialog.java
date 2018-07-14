package com.idea.tools.view;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.view.components.MessageMainPanel;
import com.idea.tools.view.components.MessagePayloadPanel;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;

import javax.swing.*;
import java.awt.*;

import static com.idea.tools.App.getProject;
import static com.idea.tools.App.jmsService;

public class SendMessageDialog extends JDialog {

    private static final Logger LOGGER = Logger.getInstance(SendMessageDialog.class);

    private static final String SEND_SUCCESS_TEMPLATE = "Message has been successfully sent to queue %s";
    private static final String SEND_FAIL_TEMPLATE = "Message hasn't been sent. Reason: \n%s";

    private QueueDto queue;

    private JPanel rootPanel;
    private JBTabsImpl tabs;
    private JButton sendButton;
    private JButton closeButton;
    private JPanel tabPanel;

    private MessageMainPanel mainPanel;
    private MessagePayloadPanel payloadPanel;

    private SendMessageDialog(QueueDto queue) {
        this.queue = queue;
        render();
    }

    private void render() {
        tabs = new JBTabsImpl(getProject());

        mainPanel = new MessageMainPanel(queue);
        payloadPanel = new MessagePayloadPanel();

        TabInfo main = new TabInfo(mainPanel).setText("Mail");
        tabs.addTab(main);

        TabInfo payload = new TabInfo(payloadPanel).setText("Payload");
        tabs.addTab(payload);

        tabPanel.setLayout(new BorderLayout());
        tabPanel.add(tabs);
        add(rootPanel);

        sendButton.addActionListener(event -> {
            MessageDto msg = new MessageDto();
            mainPanel.fillMessage(msg);
            payloadPanel.fillMessage(msg);

            try {
                jmsService().send(msg);
                String content = String.format(SEND_SUCCESS_TEMPLATE, msg.getQueue().getName());
                Notifications.Bus.notify(new Notification("jms", "Success", content, NotificationType.INFORMATION));
            } catch (Exception ex) {
                String content = String.format(SEND_FAIL_TEMPLATE, ex.getMessage());
                Notifications.Bus.notify(new Notification("jms", "Failure", content, NotificationType.ERROR));
                LOGGER.error("An exception has been thrown during a message sending", ex);
                ex.printStackTrace();
            }
        });
        closeButton.addActionListener(event -> dispose());
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public static void showDialog(QueueDto queue) {
        SwingUtilities.invokeLater(() -> {
            SendMessageDialog dialog = new SendMessageDialog(queue);
            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);
        });
    }

    private void createUIComponents() {
    }
}
