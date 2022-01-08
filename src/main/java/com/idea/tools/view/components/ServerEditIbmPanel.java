package com.idea.tools.view.components;

import com.idea.tools.dto.IbmConfiguration;
import com.idea.tools.dto.ServerDto;

import javax.swing.*;

import static com.idea.tools.utils.Utils.getOrDefault;

public class ServerEditIbmPanel extends JPanel {
    private JPanel rootPanel;
    private JTextField queueManagerField;
    private JTextField channelField;

    public ServerEditIbmPanel(ServerDto server) {
        render(server);
    }

    private void render(ServerDto server) {
        setValues(server);
        add(rootPanel);
    }

    public void fillServer(ServerDto server) {
        IbmConfiguration ibm = server.getIbmConfiguration();
        ibm.setChannel(channelField.getText());
        ibm.setQueueManager(queueManagerField.getText());
    }

    public void setValues(ServerDto server) {
        IbmConfiguration ibm = server.getIbmConfiguration();
        channelField.setText(getOrDefault(ibm.getChannel(), ""));
        queueManagerField.setText(getOrDefault(ibm.getQueueManager(), ""));
    }

}
