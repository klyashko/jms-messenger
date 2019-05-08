package com.idea.tools.view.components.message;

import com.idea.tools.dto.HeaderDto;

import javax.swing.*;
import java.util.function.Consumer;

import static com.idea.tools.utils.GuiUtils.addSimpleListener;
import static com.idea.tools.utils.Utils.getOrDefault;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public class HeaderEditPanel extends JPanel {

	private HeaderDto header;
	private Consumer<HeaderDto> handler;

	private JPanel rootPanel;
	private JTextField valueField;
	private JTextField nameField;
	private JButton saveButton;
	private JButton clearButton;
	private JCheckBox trimEmptyHeaderToCheckBox;

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

		addSimpleListener(nameField, event -> enableButton());

		saveButton.addActionListener(event -> {
			this.header.setName(nameField.getText());
			if (trimEmptyHeaderToCheckBox.isSelected()) {
				this.header.setValue(trimToNull(valueField.getText()));
			} else {
				this.header.setValue(valueField.getText());
			}
			handler.accept(header);
		});

		clearButton.addActionListener(event -> setHeader(new HeaderDto("", null)));
	}

	private void setValues() {
		nameField.setText(header.getName());
		valueField.setText(getOrDefault(header.getValue(), ""));
	}

	private void enableButton() {
		boolean requiredFieldsAreFilled = isNotBlank(nameField.getText());
		saveButton.setEnabled(requiredFieldsAreFilled);
	}

}
