package com.idea.tools.view;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.task.TestConnectionTask;
import com.idea.tools.view.components.ServerEditDestinationPanel;
import com.idea.tools.view.components.ServerEditMainPanel;
import com.intellij.ui.JBColor;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.idea.tools.App.getProject;
import static com.idea.tools.App.serverService;
import static com.intellij.ui.JBColor.*;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class ServerEditPanel extends JPanel {

    private static final String CONNECTION_SUCCESS_TEXT = "Success";
    private static final String CONNECTION_RUNNING_TEXT = "Test connection is running";
    private static final String CONNECTION_FAIL_TEXT = "Fail";
    private static final JBColor CONNECTION_SUCCESS_COLOR = GREEN;
    private static final JBColor CONNECTION_RUNNING_COLOR = BLACK;
    private static final JBColor CONNECTION_FAIL_COLOR = RED;

    @Getter
    private ServerDto server;

    private JBTabsImpl tabs;
    private TabInfo destinations;
    private ServerEditMainPanel mainPanel;
    private ServerEditDestinationPanel destinationPanel;
    private JPanel rootPanel;
    private JPanel tabsPanel;
    @Getter
    private JButton saveButton;
    @Getter
    private JButton cancelButton;
    @Getter
    private JButton testConnectionButton;
    private JLabel connectionStatus;
    private JTextArea connectionDetails;

    public ServerEditPanel() {
        this(new ServerDto());
    }

    public ServerEditPanel(ServerDto server) {
        this.server = server;
        render();
    }

    public void setNewValue(ServerDto server) {
        this.server = server;
        setValues();
        enableDestinationPanel();
    }

    private void render() {
        tabs = new JBTabsImpl(getProject());

        mainPanel = new ServerEditMainPanel(this);
        TabInfo main = new TabInfo(mainPanel);
        main.setText("Main settings");
        tabs.addTab(main);

        destinationPanel = new ServerEditDestinationPanel(server);
        destinations = new TabInfo(destinationPanel);
        destinations.setText("Destinations");
        enableDestinationPanel();
        tabs.addTab(destinations);

        tabsPanel.add(tabs);

        connectionDetails.setLineWrap(true);
        connectionDetails.setColumns(2);

        saveButton.addActionListener(event -> {
            fillServer(server);
            serverService().saveOrUpdate(server);
            enableDestinationPanel();
        });

        cancelButton.addActionListener(event -> setValues());

        testConnectionButton.addActionListener(event -> {
            ServerDto s = new ServerDto();
            fillServer(s);
            emptyStatus();
            new TestConnectionTask(s, this::successStatus, this::failStatus).queue();
        });

        add(rootPanel);
    }

    private void emptyStatus() {
        connectionStatus.setText(CONNECTION_RUNNING_TEXT);
        connectionStatus.setForeground(CONNECTION_RUNNING_COLOR);
        connectionStatus.setVisible(true);

        connectionDetails.setText("");
        connectionDetails.setVisible(false);
    }

    private void successStatus() {
        connectionStatus.setText(CONNECTION_SUCCESS_TEXT);
        connectionStatus.setForeground(CONNECTION_SUCCESS_COLOR);
        connectionStatus.setVisible(true);
    }

    private void failStatus(@NotNull Throwable ex) {
        connectionStatus.setText(CONNECTION_FAIL_TEXT);
        connectionStatus.setForeground(CONNECTION_FAIL_COLOR);
        connectionStatus.setVisible(true);

        connectionDetails.setText(ex.getMessage());
        connectionDetails.setVisible(true);
    }

    private void fillServer(ServerDto server) {
        mainPanel.fillServer(server);
        destinationPanel.fillServer(server);
    }

    private void setValues() {
        mainPanel.setValues(server);
        destinationPanel.setValues(server);
    }

    private void enableDestinationPanel() {
        destinations.setEnabled(isNotBlank(server.getId()));
    }
}
