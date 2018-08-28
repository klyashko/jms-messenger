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

    private JTextArea payloadField;

    private Optional<MessageDto> message = Optional.empty();

    public ViewMessagePayloadPanel() {
        this(null);
    }

    public ViewMessagePayloadPanel(MessageDto message) {
        this.message = Optional.ofNullable(message);
        render();
        setValues();
    }

    public void fillMessage(MessageDto dto) {
        dto.setPayload(payloadField.getText());
    }

    private void render() {
        setLayout(new BorderLayout());
        JPanel toolbarPanel = new JPanel(new BorderLayout());

        JCheckBox wrapLinesCheckBox = new JCheckBox();
        wrapLinesCheckBox.setText("Wrap lines");
        wrapLinesCheckBox.setSelected(true);
        wrapLinesCheckBox.addActionListener(event -> payloadField.setLineWrap(wrapLinesCheckBox.isSelected()));

        payloadField = new JTextArea();
        payloadField.setLineWrap(wrapLinesCheckBox.isSelected());
        payloadField.setEditable(isEditable());

        DefaultActionGroup actions = new DefaultActionGroup("CopyPasteToolbar", false);
        actions.add(new PasteButton(payloadField, isEditable()));
        actions.add(new CopyButton(payloadField));

        toolbarPanel.add(toolbar(actions, "PayloadPanel", true), BorderLayout.EAST);
        toolbarPanel.add(wrapLinesCheckBox, BorderLayout.WEST);

        JBScrollPane pane = new JBScrollPane(payloadField);
        pane.setPreferredSize(new Dimension(450, 300));

        add(toolbarPanel, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
    }

    private void setValues() {
        message.ifPresent(m -> payloadField.setText(m.getPayload()));
    }

    protected boolean isEditable() {
        return false;
    }

}
