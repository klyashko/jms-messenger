package com.idea.tools.view.components;

import com.idea.tools.dto.ServerDto;

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
        server.setClientId(clientIdField.getText());
        server.setZookeeperHost(zookeeperHostField.getText());
        server.setZookeeperPort(toInteger(zookeeperPortField.getText()));
    }

    public void setValues(ServerDto server) {
        clientIdField.setText(getOrDefault(server.getClientId(), "jms-messenger"));
        zookeeperHostField.setText(getOrDefault(server.getZookeeperHost(), "localhost"));
        zookeeperPortField.setValue(getOrDefault(server.getZookeeperPort(), 2181));
    }

    private void createUIComponents() {
        zookeeperPortField = createNumberInputField();
    }
}
