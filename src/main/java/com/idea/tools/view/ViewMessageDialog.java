package com.idea.tools.view;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.view.components.message.ViewMessageHeadersPanel;
import com.idea.tools.view.components.message.ViewMessageMainPanel;
import com.idea.tools.view.components.message.ViewMessagePayloadPanel;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;

import javax.swing.*;
import java.util.Optional;

import static com.idea.tools.App.getProject;

public class ViewMessageDialog extends JFrame {

    protected JButton sendButton;
    protected JButton closeButton;
    protected JCheckBox closeAfterSendCheckBox;
    private MessageDto message;
    private QueueDto queue;
    private JPanel rootPanel;
    private JPanel tabPanel;

    protected ViewMessageDialog(QueueDto queue) {
        this.queue = queue;
        render();
    }

    private ViewMessageDialog(MessageDto message) {
        this.message = message;
        render();
    }

    public static void showDialog(MessageDto message) {
        SwingUtilities.invokeLater(() -> {
            ViewMessageDialog dialog = new ViewMessageDialog(message);
            dialog.setLocationRelativeTo(null);
            dialog.setTitle("Message");
            dialog.pack();
            dialog.setVisible(true);
        });
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void render() {
        JBTabsImpl tabs = new JBTabsImpl(getProject());
        tabs.addTab(new TabInfo(mainPanel(queue)).setText("Main"));
        tabs.addTab(new TabInfo(headersPanel()).setText("Headers"));
        tabs.addTab(new TabInfo(payloadPanel()).setText("Payload"));

        tabPanel.add(tabs);

        JRootPane rootPane = new JRootPane();
        IdeGlassPaneImpl pane = new IdeGlassPaneImpl(rootPane);
        setGlassPane(pane);

        sendButton.setVisible(false);
        closeAfterSendCheckBox.setVisible(false);

        add(rootPanel);

        closeButton.addActionListener(event -> dispose());
    }

    protected ViewMessageMainPanel mainPanel(QueueDto queue) {
        return new ViewMessageMainPanel(message);
    }

    protected ViewMessageHeadersPanel headersPanel() {
        return new ViewMessageHeadersPanel(message.getHeaders());
    }

    protected ViewMessagePayloadPanel payloadPanel() {
        return new ViewMessagePayloadPanel(Optional.of(message));
    }

}
