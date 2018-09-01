package com.idea.tools.view.components;

import com.idea.tools.dto.ServerDto;

import javax.swing.*;

import static com.idea.tools.utils.Utils.getOrDefault;

public class ServerEditRabbitMQPanel extends JPanel {
	private JPanel rootPanel;
	private JTextField virtualHostField;

	public ServerEditRabbitMQPanel(ServerDto server) {
		render(server);
	}

	private void render(ServerDto server) {
		setValues(server);
		add(rootPanel);
	}

	public void fillServer(ServerDto server) {
		server.setVirtualHost(virtualHostField.getText());
	}

	public void setValues(ServerDto server) {
		virtualHostField.setText(getOrDefault(server.getVirtualHost(), "/"));
	}
}
