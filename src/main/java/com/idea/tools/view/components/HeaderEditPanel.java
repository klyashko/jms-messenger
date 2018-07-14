package com.idea.tools.view.components;

import org.apache.commons.lang3.tuple.MutablePair;

import javax.swing.*;
import java.util.function.Consumer;

import static com.idea.tools.utils.GuiUtils.simpleListener;
import static com.idea.tools.utils.Utils.getOrDefault;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HeaderEditPanel extends JPanel {

    private MutablePair<String, Object> header;
    private Consumer<MutablePair<String, Object>> handler = p -> {};

    private JPanel rootPanel;
    private JTextField valueField;
    private JTextField nameField;
    private JButton saveButton;
    private JButton cancelButton;

    public HeaderEditPanel(Consumer<MutablePair<String, Object>> handler) {
        this(MutablePair.of("", null), handler);
    }

    public HeaderEditPanel(MutablePair<String, Object> header, Consumer<MutablePair<String, Object>> handler) {
        this.header = header;
        this.handler = handler;
        render();
    }

    public void setHeader(MutablePair<String, Object> header) {
        this.header = header;
        setValues();
        enableButton();
    }

    private void render() {
        setValues();
        enableButton();
        add(rootPanel);

        nameField.getDocument().addDocumentListener(simpleListener(event -> enableButton()));
        valueField.getDocument().addDocumentListener(simpleListener(event -> enableButton()));

        saveButton.addActionListener(event -> {
            this.header.setLeft(nameField.getText());
            this.header.setRight(valueField.getText());
            handler.accept(header);
        });

        cancelButton.addActionListener(event -> setHeader(MutablePair.of("", null)));
    }

    private void setValues() {
        nameField.setText(header.getKey());
        valueField.setText(getOrDefault(header.getValue(), "").toString());
    }

    private void enableButton() {
        boolean requiredFieldsAreFilled = isNotBlank(nameField.getText()) && isNotBlank(valueField.getText());
        saveButton.setEnabled(requiredFieldsAreFilled);
    }

}
