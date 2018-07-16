package com.idea.tools.view;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.view.components.MessageHeadersPanel;
import com.idea.tools.view.components.MessageMainPanel;
import com.idea.tools.view.components.MessagePayloadPanel;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;

import javax.swing.*;
import java.util.ArrayList;

import static com.idea.tools.App.getProject;
import static com.idea.tools.App.jmsService;

public class SendMessageDialog extends JFrame {

    private static final Logger LOGGER = Logger.getInstance(SendMessageDialog.class);

    private static final String SEND_SUCCESS_TEMPLATE = "Message has been successfully sent to queue %s";
    private static final String SEND_FAIL_TEMPLATE = "Message hasn't been sent. Reason: \n%s";

    private QueueDto queue;

    private JPanel rootPanel;
    private JButton sendButton;
    private JButton closeButton;
    private JPanel tabPanel;
    private JCheckBox closeAfterSendCheckBox;

    private MessageMainPanel mainPanel;
    private MessagePayloadPanel payloadPanel;
    private MessageHeadersPanel headersPanel;

    private SendMessageDialog(QueueDto queue) {
        this.queue = queue;
        render();
    }

    public static void showDialog(QueueDto queue) {
        SwingUtilities.invokeLater(() -> {
            SendMessageDialog dialog = new SendMessageDialog(queue);
            dialog.setLocationRelativeTo(null);
            dialog.setTitle("Send message");
            dialog.pack();
            dialog.setVisible(true);
        });
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void render() {
        mainPanel = new MessageMainPanel(queue);
        payloadPanel = new MessagePayloadPanel();
        headersPanel = new MessageHeadersPanel(new ArrayList<>());

        JBTabsImpl tabs = new JBTabsImpl(getProject());
        tabs.addTab(new TabInfo(mainPanel).setText("Main"));
        tabs.addTab(new TabInfo(headersPanel).setText("Headers"));
        tabs.addTab(new TabInfo(payloadPanel).setText("Payload"));

        tabPanel.add(tabs);

        JRootPane rootPane = new JRootPane();
        IdeGlassPaneImpl pane = new IdeGlassPaneImpl(rootPane);
        setGlassPane(pane);

        add(rootPanel);

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
        closeButton.addActionListener(event -> dispose());
    }

    private void createUIComponents() {
    }
}
