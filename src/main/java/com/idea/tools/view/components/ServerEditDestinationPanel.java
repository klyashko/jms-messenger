package com.idea.tools.view.components;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.DestinationType;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.task.LoadQueuesTask;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EnumComboBoxModel;

import javax.swing.*;

import static com.idea.tools.App.destinationService;
import static com.idea.tools.dto.ServerType.ACTIVE_MQ;
import static com.idea.tools.utils.GuiUtils.simpleListener;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class ServerEditDestinationPanel extends JPanel {

    private ServerDto server;
    private DestinationTable table;
    private DestinationDto currentDestination;

    private JPanel rootPanel;
    private JPanel tablePanel;
    private JComboBox<DestinationType> typeCombobox;
    private JTextField nameField;
    private JButton saveButton;
    private JButton clearButton;
    private JButton loadFromServerButton;

    public ServerEditDestinationPanel(ServerDto server) {
        currentDestination = new DestinationDto();
        this.server = server;
        render(server);
    }

    private void render(ServerDto server) {
        table = new DestinationTable(this, server.getDestinations());
        tablePanel.add(table);

        nameField.getDocument().addDocumentListener(simpleListener(event -> enableSaveButton()));
        typeCombobox.addActionListener(event -> enableSaveButton());

        saveButton.addActionListener(event -> {
            fillValues();
            if (currentDestination.getServer() == null) {
                currentDestination.setServer(this.server);
            }
            destinationService().saveOrUpdate(currentDestination);
            table.setData(server.getDestinations());
        });

        clearButton.addActionListener(event -> {
            currentDestination = new DestinationDto();
            setValues();
            table.clearSelection();
        });

        loadFromServerButton.setEnabled(ACTIVE_MQ.equals(server.getType()));
        loadFromServerButton.addActionListener(event -> new LoadQueuesTask(singletonList(server), () -> setValues(server)));

        setValues();

        add(rootPanel);
    }

    public void fillServer(ServerDto server) {
        server.setDestinations(table.getData());
    }

    public void setValues(ServerDto server) {
        this.server = server;
        table.setData(server.getDestinations());
        this.currentDestination = new DestinationDto();
    }

    public void setOnEdit(DestinationDto destination) {
        this.currentDestination = DestinationDto.copy(destination);
        setValues();
    }

    private void setValues() {
        nameField.setText(currentDestination.getName());
        typeCombobox.setSelectedItem(currentDestination.getType());
        enableSaveButton();
    }

    private void fillValues() {
        currentDestination.setName(nameField.getText());
        currentDestination.setType(typeCombobox.getItemAt(typeCombobox.getSelectedIndex()));
    }

    private void enableSaveButton() {
        saveButton.setEnabled(isNotBlank(nameField.getText()) && typeCombobox.getSelectedIndex() != -1);
    }

    private void createUIComponents() {
        typeCombobox = new ComboBox<>(new EnumComboBoxModel<>(DestinationType.class));
    }
}
