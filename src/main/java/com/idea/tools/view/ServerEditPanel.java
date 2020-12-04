package com.idea.tools.view;

import static com.idea.tools.service.ServerService.serverService;
import static com.intellij.ui.JBColor.BLACK;
import static com.intellij.ui.JBColor.GREEN;
import static com.intellij.ui.JBColor.RED;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.task.TestConnectionTask;
import com.idea.tools.view.components.ServerEditDestinationPanel;
import com.idea.tools.view.components.ServerEditKafkaPanel;
import com.idea.tools.view.components.ServerEditMainPanel;
import com.idea.tools.view.components.ServerEditRabbitMQPanel;
import com.idea.tools.view.components.ServerEditSslPanel;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import javax.swing.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class ServerEditPanel extends JPanel {

    private static final String CONNECTION_SUCCESS_TEXT = "Success";
    private static final String CHANGES_HAS_BEEN_SAVED_TEXT = "Changes has been saved";
    private static final String CONNECTION_RUNNING_TEXT = "Test connection is running";
    private static final String CONNECTION_FAIL_TEXT = "Fail";
    private static final JBColor CONNECTION_SUCCESS_COLOR = GREEN;
    private static final JBColor CHANGES_HAS_BEEN_SAVED_COLOR = GREEN;
    private static final JBColor CONNECTION_RUNNING_COLOR = BLACK;
    private static final JBColor CONNECTION_FAIL_COLOR = RED;

    private final Project project;

    @Getter
    private ServerDto server;

    /**
     * Tabs
     */
    private TabInfo destinations;
    @Getter
    private TabInfo kafka;
    @Getter
    private TabInfo rabbitMQ;
    @Getter
    private TabInfo ssl;
    private ServerEditMainPanel mainPanel;
    private ServerEditDestinationPanel destinationPanel;
    private ServerEditKafkaPanel kafkaPanel;
    private ServerEditRabbitMQPanel rabbitMQPanel;
    private ServerEditSslPanel sslPanel;

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

    public ServerEditPanel(Project project) {
        this(project, new ServerDto());
    }

    public ServerEditPanel(Project project, ServerDto server) {
        this.server = server;
        this.project = project;
        render();
    }

    public void setNewValue(ServerDto server) {
        this.server = server;
        setValues();
        updateTabs();
    }

    public void updateTabs() {
        destinations.setEnabled(isNotBlank(server.getId()));
        mainPanel.updateTabs();
    }

    private void render() {
        JBTabsImpl tabs = new JBTabsImpl(project);

        mainPanel = new ServerEditMainPanel(this);
        TabInfo main = new TabInfo(mainPanel);
        main.setText("Main settings");
        tabs.addTab(main);

        sslPanel = new ServerEditSslPanel(server);
        ssl = new TabInfo(sslPanel);
        ssl.setText("SSL");
        tabs.addTab(ssl);

        kafkaPanel = new ServerEditKafkaPanel(server);
        kafka = new TabInfo(kafkaPanel);
        kafka.setText("Kafka settings");
        tabs.addTab(kafka);

        rabbitMQPanel = new ServerEditRabbitMQPanel(server);
        rabbitMQ = new TabInfo(rabbitMQPanel);
        rabbitMQ.setText("RabbitMQ settings");
        tabs.addTab(rabbitMQ);

        destinationPanel = new ServerEditDestinationPanel(project, server);
        destinations = new TabInfo(destinationPanel);
        destinations.setText("Destinations");
        tabs.addTab(destinations);

        updateTabs();

        tabsPanel.add(tabs);

        connectionDetails.setLineWrap(true);
        connectionDetails.setColumns(2);

        saveButton.addActionListener(event -> {
            fillServer(server);
            serverService(project).saveOrUpdate(server);
            updateTabs();
            changesHasBeenSavedStatus();
        });

        cancelButton.addActionListener(event -> setValues());

        testConnectionButton.addActionListener(event -> {
            ServerDto s = new ServerDto();
            fillServer(s);
            new TestConnectionTask(project, s, this::successStatus, this::failStatus).queue();
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
        emptyStatus();
        connectionStatus.setText(CONNECTION_SUCCESS_TEXT);
        connectionStatus.setForeground(CONNECTION_SUCCESS_COLOR);
        connectionStatus.setVisible(true);
    }

    private void changesHasBeenSavedStatus() {
        emptyStatus();
        connectionStatus.setText(CHANGES_HAS_BEEN_SAVED_TEXT);
        connectionStatus.setForeground(CHANGES_HAS_BEEN_SAVED_COLOR);
        connectionStatus.setVisible(true);
    }

    private void failStatus(@NotNull Throwable ex) {
        failStatus(ex.getMessage());
    }

    private void failStatus(String msg) {
        emptyStatus();
        connectionStatus.setText(CONNECTION_FAIL_TEXT);
        connectionStatus.setForeground(CONNECTION_FAIL_COLOR);
        connectionStatus.setVisible(true);

        connectionDetails.setText(msg);
        connectionDetails.setVisible(true);
    }

    private void fillServer(ServerDto server) {
        mainPanel.fillServer(server);
        sslPanel.fillServer(server);
        kafkaPanel.fillServer(server);
        rabbitMQPanel.fillServer(server);
        destinationPanel.fillServer(server);
    }

    private void setValues() {
        mainPanel.setValues(server);
        sslPanel.setValues(server);
        kafkaPanel.setValues(server);
        rabbitMQPanel.setValues(server);
        destinationPanel.setValues(server);
    }
}
