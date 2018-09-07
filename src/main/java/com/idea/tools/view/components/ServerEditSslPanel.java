package com.idea.tools.view.components;

import com.idea.tools.dto.SSLConfiguration;
import com.idea.tools.dto.ServerDto;

import javax.swing.*;

public class ServerEditSslPanel extends JPanel {

	private JPanel rootPanel;
	private JTextField truststorePasswordField;
	private JTextField keystorePasswordField;
	private JTextField truststoreField;
	private JTextField keystoreField;

	public ServerEditSslPanel(ServerDto server) {
		render(server);
	}

	private void render(ServerDto server) {
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
