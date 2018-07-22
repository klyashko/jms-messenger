package com.idea.tools.view;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.TemplateMessageDto;
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
import static com.idea.tools.App.templateService;
import static com.idea.tools.utils.GuiUtils.simpleListener;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ViewMessageDialog extends JFrame {

    protected JButton sendButton;
    protected JButton closeButton;
    protected JCheckBox closeAfterSendCheckBox;
    private JPanel rootPanel;
    private JPanel tabPanel;
    private JTextField templateNameField;
    private JButton saveAsTemplateButton;
    private JPanel buttonsPanel;

    private ViewMessageMainPanel mainPanel;
    private ViewMessageHeadersPanel headersPanel;
    private ViewMessagePayloadPanel payloadPanel;

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
        mainPanel = mainPanel(queue, message);
        headersPanel = headersPanel(queue, message);
        payloadPanel = payloadPanel(queue, message);

        JBTabsImpl tabs = new JBTabsImpl(getProject());
        tabs.addTab(new TabInfo(mainPanel).setText("Main"));
        tabs.addTab(new TabInfo(headersPanel).setText("Headers"));
        tabs.addTab(new TabInfo(payloadPanel).setText("Payload"));

        tabPanel.add(tabs);

        JRootPane rootPane = new JRootPane();
        IdeGlassPaneImpl pane = new IdeGlassPaneImpl(rootPane);
        setGlassPane(pane);

        sendButton.setVisible(false);
        closeAfterSendCheckBox.setVisible(false);
        saveAsTemplateButton.setEnabled(false);
        templateNameField.getDocument().addDocumentListener(
                simpleListener(event -> saveAsTemplateButton.setEnabled(isNotBlank(templateNameField.getText())))
        );
        saveAsTemplateButton.addActionListener(event -> {
            TemplateMessageDto template = new TemplateMessageDto();
            fillMessage(template);
            template.setName(templateNameField.getText());
            templateService().saveOrUpdate(template);
        });

        add(rootPanel);

        closeButton.addActionListener(event -> dispose());
    }

    protected void fillMessage(MessageDto msg) {
        mainPanel.fillMessage(msg);
        headersPanel.fillMessage(msg);
        payloadPanel.fillMessage(msg);
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
