package com.idea.tools.view.components;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.dto.ZookeeperConfiguration;

import javax.swing.*;

import static com.idea.tools.utils.GuiUtils.createNumberInputField;
import static com.idea.tools.utils.Utils.getOrDefault;
import static com.idea.tools.utils.Utils.toInteger;

public class ServerEditKafkaPanel extends JPanel {

	private JTextField clientIdField;
	private JPanel rootPanel;
	private JTextField zookeeperHostField;
	private JFormattedTextField zookeeperPortField;

	public ServerEditKafkaPanel(ServerDto server) {
		render(server);
	}

	private void render(ServerDto server) {
		setValues(server);
		add(rootPanel);
	}

	public void fillServer(ServerDto server) {
		ZookeeperConfiguration zookeeper = server.getZookeeperConfiguration();
		zookeeper.setClientId(clientIdField.getText());
		zookeeper.setZookeeperHost(zookeeperHostField.getText());
		zookeeper.setZookeeperPort(toInteger(zookeeperPortField.getText()));
	}

	public void setValues(ServerDto server) {
		ZookeeperConfiguration zookeeper = server.getZookeeperConfiguration();
		clientIdField.setText(getOrDefault(zookeeper.getClientId(), "jms-messenger"));
		zookeeperHostField.setText(getOrDefault(zookeeper.getZookeeperHost(), "localhost"));
		zookeeperPortField.setValue(getOrDefault(zookeeper.getZookeeperPort(), 2181));
	}

	private void createUIComponents() {
		zookeeperPortField = createNumberInputField();
	}
}
