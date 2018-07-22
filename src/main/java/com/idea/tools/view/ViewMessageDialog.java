package com.idea.tools.view;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.utils.GuiUtils;
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
    private JPanel rootPanel;
    private JPanel tabPanel;

    private Optional<MessageDto> message = Optional.empty();
    private Optional<QueueDto> queue = Optional.empty();

    protected ViewMessageDialog(QueueDto queue) {
        this.queue = Optional.of(queue);
        render();
    }

    protected ViewMessageDialog(MessageDto message) {
        this.message = Optional.of(message);
        render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public static void showDialog(MessageDto message) {
        GuiUtils.showDialog(new ViewMessageDialog(message), "Message");
    }

    private void render() {
        JBTabsImpl tabs = new JBTabsImpl(getProject());
        tabs.addTab(new TabInfo(mainPanel(queue, message)).setText("Main"));
        tabs.addTab(new TabInfo(headersPanel(queue, message)).setText("Headers"));
        tabs.addTab(new TabInfo(payloadPanel(queue, message)).setText("Payload"));

        tabPanel.add(tabs);

        JRootPane rootPane = new JRootPane();
        IdeGlassPaneImpl pane = new IdeGlassPaneImpl(rootPane);
        setGlassPane(pane);

        sendButton.setVisible(false);
        closeAfterSendCheckBox.setVisible(false);

        add(rootPanel);

        closeButton.addActionListener(event -> dispose());
    }

    protected ViewMessageMainPanel mainPanel(Optional<QueueDto> queue, Optional<MessageDto> message) {
        return message.map(ViewMessageMainPanel::new)
                .orElseThrow(() -> new IllegalArgumentException("Message must not me empty"));
    }

    protected ViewMessageHeadersPanel headersPanel(Optional<QueueDto> queue, Optional<MessageDto> message) {
        return message.map(msg -> new ViewMessageHeadersPanel(msg.getHeaders()))
                .orElseThrow(() -> new IllegalArgumentException("Message must not me empty"));
    }

    protected ViewMessagePayloadPanel payloadPanel(Optional<QueueDto> queue, Optional<MessageDto> message) {
        return message.map(ViewMessagePayloadPanel::new)
                .orElseThrow(() -> new IllegalArgumentException("Message must not me empty"));
    }

}
