package com.idea.tools.view.components.message;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.view.button.CopyButton;
import com.idea.tools.view.button.PasteButton;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static com.idea.tools.utils.GuiUtils.toolbar;

public class ViewMessagePayloadPanel extends JPanel {

    protected JTextArea payloadField;

    private JPanel rootPanel;
    private JPanel toolbarPanel;
    private JCheckBox wrapLinesCheckBox;
    private Optional<MessageDto> message = Optional.empty();

    public ViewMessagePayloadPanel() {
        this(null);
    }

    public ViewMessagePayloadPanel(MessageDto message) {
        this.message = Optional.ofNullable(message);
        render();
    }

    public void fillMessage(MessageDto dto) {
        dto.setPayload(payloadField.getText());
    }

    private void render() {
        setValues();

        DefaultActionGroup actions = new DefaultActionGroup("CopyPasteToolbar", false);
        actions.add(new PasteButton(payloadField, isEditable()));
        actions.add(new CopyButton(payloadField));

        toolbarPanel.add(toolbar(actions, "PayloadPanel", true), BorderLayout.EAST);

        wrapLinesCheckBox.addActionListener(event -> payloadField.setLineWrap(wrapLinesCheckBox.isSelected()));

        payloadField.setLineWrap(wrapLinesCheckBox.isSelected());
        payloadField.setEditable(isEditable());
        add(new JBScrollPane(rootPanel));
    }

    private void setValues() {
        message.ifPresent(m -> payloadField.setText(m.getPayload()));
    }

    protected boolean isEditable() {
        return false;
    }

}
