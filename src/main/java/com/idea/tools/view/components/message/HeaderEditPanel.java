package com.idea.tools.view.components.message;

import com.idea.tools.dto.HeaderDto;

import javax.swing.*;
import java.util.function.Consumer;

import static com.idea.tools.utils.GuiUtils.simpleListener;
import static com.idea.tools.utils.Utils.getOrDefault;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HeaderEditPanel extends JPanel {

    private HeaderDto header;
    private Consumer<HeaderDto> handler = p -> {};

    private JPanel rootPanel;
    private JTextField valueField;
    private JTextField nameField;
    private JButton saveButton;
    private JButton clearButton;

    public HeaderEditPanel(Consumer<HeaderDto> handler) {
        this(new HeaderDto("", null), handler);
    }

    public HeaderEditPanel(HeaderDto header, Consumer<HeaderDto> handler) {
        this.header = header;
        this.handler = handler;
        render();
    }

    public void setHeader(HeaderDto header) {
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
            this.header.setName(nameField.getText());
            this.header.setValue(valueField.getText());
            handler.accept(header);
        });

        clearButton.addActionListener(event -> setHeader(new HeaderDto("", null)));
    }

    private void setValues() {
        nameField.setText(header.getName());
        valueField.setText(getOrDefault(header.getValue(), "").toString());
    }

    private void enableButton() {
        boolean requiredFieldsAreFilled = isNotBlank(nameField.getText()) && isNotBlank(valueField.getText());
        saveButton.setEnabled(requiredFieldsAreFilled);
    }

}
