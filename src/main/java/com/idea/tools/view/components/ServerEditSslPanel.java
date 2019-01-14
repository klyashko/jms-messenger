package com.idea.tools.view.components;

import com.idea.tools.dto.SSLConfiguration;
import com.idea.tools.dto.ServerDto;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.MacroAwareTextBrowseFolderListener;

import javax.swing.*;

import static com.intellij.openapi.fileChooser.FileChooserDescriptorFactory.createSingleFileDescriptor;

public class ServerEditSslPanel extends JPanel {

	private JPanel rootPanel;
	private JTextField truststorePasswordField;
	private JTextField keystorePasswordField;
	private TextFieldWithBrowseButton truststoreField;
	private JPanel keystorePanel;
	private JPanel trustStorePanel;
	private TextFieldWithBrowseButton keystoreField;

	public ServerEditSslPanel(ServerDto server) {
		render(server);
	}

	private void render(ServerDto server) {
		keystoreField = new TextFieldWithBrowseButton();
		keystoreField.addBrowseFolderListener(new MacroAwareTextBrowseFolderListener(createSingleFileDescriptor(), null));
		keystorePanel.add(keystoreField);

		truststoreField = new TextFieldWithBrowseButton();
		truststoreField.addBrowseFolderListener(new MacroAwareTextBrowseFolderListener(createSingleFileDescriptor(), null));
		trustStorePanel.add(truststoreField);

		setValues(server);
		add(rootPanel);
	}

	public void fillServer(ServerDto server) {
		SSLConfiguration ssl = server.getSslConfiguration();
		ssl.setTruststore(truststoreField.getText());
		ssl.setTruststorePassword(truststorePasswordField.getText());
		ssl.setKeystore(keystoreField.getText());
		ssl.setKeystorePassword(keystorePasswordField.getText());
	}

	public void setValues(ServerDto server) {
		SSLConfiguration ssl = server.getSslConfiguration();
		truststoreField.setText(ssl.getTruststore());
		truststorePasswordField.setText(ssl.getTruststorePassword());
		keystoreField.setText(ssl.getKeystore());
		keystorePasswordField.setText(ssl.getKeystorePassword());
	}

}
