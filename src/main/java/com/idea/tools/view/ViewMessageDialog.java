package com.idea.tools.view;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
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

    protected JButton actionButton;
    protected JButton closeButton;
    protected JCheckBox closeAfterSendCheckBox;
    private JPanel rootPanel;
    private JPanel tabPanel;
    protected JTextField templateNameField;
    protected JButton saveAsTemplateButton;
    private JPanel buttonsPanel;

    private ViewMessageMainPanel mainPanel;
    private ViewMessageHeadersPanel headersPanel;
    private ViewMessagePayloadPanel payloadPanel;

    private Optional<MessageDto> message = Optional.empty();
    private Optional<DestinationDto> destination = Optional.empty();

    protected ViewMessageDialog(DestinationDto destination) {
        this.destination = Optional.of(destination);
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
        mainPanel = mainPanel(destination, message);
        headersPanel = headersPanel(destination, message);
        payloadPanel = payloadPanel(destination, message);

        JBTabsImpl tabs = new JBTabsImpl(getProject());
        tabs.addTab(new TabInfo(mainPanel).setText("Main"));
        tabs.addTab(new TabInfo(headersPanel).setText("Headers"));
        tabs.addTab(new TabInfo(payloadPanel).setText("Payload"));

        tabPanel.add(tabs);

        JRootPane rootPane = new JRootPane();
        IdeGlassPaneImpl pane = new IdeGlassPaneImpl(rootPane);
        setGlassPane(pane);

        actionButton(actionButton);
        closeAfterSendCheckBox(closeAfterSendCheckBox);
        saveAsTemplateButton(saveAsTemplateButton);
        templateNameField(templateNameField);

        add(rootPanel);

        closeButton.addActionListener(event -> dispose());
    }

    protected void actionButton(JButton actionButton) {
        actionButton.setVisible(false);
    }

    protected void closeAfterSendCheckBox(JCheckBox closeAfterSendCheckBox) {
        closeAfterSendCheckBox.setVisible(false);
    }

    protected void saveAsTemplateButton(JButton saveAsTemplateButton) {
        saveAsTemplateButton.setEnabled(false);
        saveAsTemplateButton.addActionListener(event -> {
            TemplateMessageDto template = new TemplateMessageDto();
            fillMessage(template);
            templateService().saveOrUpdate(template);
        });
    }

    protected void templateNameField(JTextField templateNameField) {
        templateNameField.getDocument().addDocumentListener(
                simpleListener(event -> saveAsTemplateButton.setEnabled(isNotBlank(templateNameField.getText())))
        );
    }

    protected void fillMessage(MessageDto msg) {
        mainPanel.fillMessage(msg);
        headersPanel.fillMessage(msg);
        payloadPanel.fillMessage(msg);
        if (msg instanceof TemplateMessageDto) {
            ((TemplateMessageDto) msg).setName(templateNameField.getText());
        }
    }

    protected ViewMessageMainPanel mainPanel(Optional<DestinationDto> destination, Optional<MessageDto> message) {
        return message.map(ViewMessageMainPanel::new)
                .orElseThrow(() -> new IllegalArgumentException("Message must not me empty"));
    }

    protected ViewMessageHeadersPanel headersPanel(Optional<DestinationDto> destination, Optional<MessageDto> message) {
        return message.map(msg -> new ViewMessageHeadersPanel(msg.getHeaders()))
                .orElseThrow(() -> new IllegalArgumentException("Message must not me empty"));
    }

    protected ViewMessagePayloadPanel payloadPanel(Optional<DestinationDto> destination, Optional<MessageDto> message) {
        return message.map(ViewMessagePayloadPanel::new)
                .orElseThrow(() -> new IllegalArgumentException("Message must not me empty"));
    }

}
