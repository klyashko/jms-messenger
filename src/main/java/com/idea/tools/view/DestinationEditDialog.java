package com.idea.tools.view;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.DestinationType;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.utils.GuiUtils;
import com.intellij.openapi.project.Project;

import javax.swing.*;

import static com.idea.tools.dto.DestinationType.QUEUE;
import static com.idea.tools.service.DestinationService.destinationService;
import static com.idea.tools.utils.GuiUtils.simpleListener;
import static com.idea.tools.utils.Utils.getOrDefault;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class DestinationEditDialog extends JFrame {

    private final Project project;

    private ServerDto server;
    private DestinationDto destination;

    private JPanel rootPanel;
    private JTextField nameField;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField serverField;
    private JComboBox<DestinationType> typeComboBox;

    private DestinationEditDialog(Project project, ServerDto server) {
        this.destination = new DestinationDto();
        this.server = server;
        this.project = project;
        destination.setServer(server);

        render();
    }

    private DestinationEditDialog(Project project, DestinationDto queue) {
        this.destination = queue;
        this.server = queue.getServer();
        this.project = project;

        render();
    }

    public static void showDialog(Project project, ServerDto server) {
        GuiUtils.showDialog(new DestinationEditDialog(project, server), "Destination settings");
    }

    public static void showDialog(Project project, DestinationDto destination) {
        GuiUtils.showDialog(new DestinationEditDialog(project, destination), "Destination settings");
    }

    private void render() {
        add(rootPanel);

        typeComboBox.setModel(new DefaultComboBoxModel<>(DestinationType.getTypes(server.getType())));

        setValues();

        nameField.getDocument().addDocumentListener(
                simpleListener(event -> saveButton.setEnabled(isNotEmpty(nameField.getText())))
        );

        saveButton.addActionListener(event -> {
            destination.setName(nameField.getText());
            destination.setType(typeComboBox.getItemAt(typeComboBox.getSelectedIndex()));
            destination.setAddedManually(true);
            destinationService(project).saveOrUpdate(destination);
            dispose();
        });
        cancelButton.addActionListener(event -> dispose());
    }

    private void setValues() {
        serverField.setText(server.getName());
        nameField.setText(destination.getName());
        typeComboBox.setSelectedItem(getOrDefault(destination.getType(), QUEUE));
    }

}
