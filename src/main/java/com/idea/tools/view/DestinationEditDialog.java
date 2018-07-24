package com.idea.tools.view;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.DestinationType;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.utils.GuiUtils;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EnumComboBoxModel;

import javax.swing.*;

import static com.idea.tools.App.destinationService;
import static com.idea.tools.dto.DestinationType.QUEUE;
import static com.idea.tools.utils.GuiUtils.simpleListener;
import static com.idea.tools.utils.Utils.getOrDefault;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class DestinationEditDialog extends JFrame {

    private ServerDto server;
    private DestinationDto destination;

    private JPanel rootPanel;
    private JTextField nameField;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField serverField;
    private JComboBox<DestinationType> typeComboBox;

    private DestinationEditDialog(ServerDto server) {
        this.destination = new DestinationDto();
        this.server = server;
        destination.setServer(server);

        render();
    }

    private DestinationEditDialog(DestinationDto queue) {
        this.destination = queue;
        this.server = queue.getServer();

        render();
    }

    public static void showDialog(ServerDto server) {
        GuiUtils.showDialog(new DestinationEditDialog(server), "Destination settings");
    }

    public static void showDialog(DestinationDto destination) {
        GuiUtils.showDialog(new DestinationEditDialog(destination), "Destination settings");
    }

    private void render() {
        add(rootPanel);

        setValues();

        nameField.getDocument().addDocumentListener(
                simpleListener(event -> saveButton.setEnabled(isNotEmpty(nameField.getText())))
        );

        saveButton.addActionListener(event -> {
            destination.setName(nameField.getText());
            destination.setType(typeComboBox.getItemAt(typeComboBox.getSelectedIndex()));
            destination.setAddedManually(true);
            destinationService().saveOrUpdate(destination);
            dispose();
        });
        cancelButton.addActionListener(event -> dispose());
    }

    private void setValues() {
        serverField.setText(server.getName());
        nameField.setText(destination.getName());
        typeComboBox.setSelectedItem(getOrDefault(destination.getType(), QUEUE));
    }

    private void createUIComponents() {
        typeComboBox = new ComboBox<>(new EnumComboBoxModel<>(DestinationType.class));
    }
}
