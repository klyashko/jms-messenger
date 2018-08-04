package com.idea.tools.view;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.dto.TemplateMessageDto;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.MutableCollectionComboBoxModel;

import javax.swing.*;
import java.util.List;

import static com.idea.tools.App.settings;
import static com.idea.tools.utils.GuiUtils.label;
import static com.idea.tools.utils.Utils.filter;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public class JmsSettingsEditorPanel extends JPanel {
    private JComboBox<ServerDto> serverComboBox;
    private JPanel rootPanel;
    private JComboBox<DestinationDto> destinationComboBox;
    private JComboBox<TemplateMessageDto> messageComboBox;

    public JmsSettingsEditorPanel(String templateId) {
        this();
//        System.out.println("JmsSettingsEditorPanel is created with id " + templateId);
        setSelectedTemplateId(templateId);
    }

    public JmsSettingsEditorPanel() {
        render();
//        System.out.println("JmsSettingsEditorPanel is created without id ");
    }

    public String getSelectedTemplateId() {
        int idx = messageComboBox.getSelectedIndex();
        if (idx > -1) {
            return messageComboBox.getItemAt(idx).getId();
        }
        return null;
    }

    public void setSelectedTemplateId(String id) {
//        System.out.println("JmsSettingsEditorPanel id " + id + " is set");
        TemplateMessageDto template = settings().getServersList()
                .stream()
                .flatMap(s -> s.getDestinations().stream())
                .flatMap(d -> d.getTemplates().stream())
                .filter(t -> t.getId().equals(id))
                .findAny()
                .orElse(null);
        if (template != null) {
            DestinationDto destination = template.getDestination();
            List<TemplateMessageDto> templates = destination.getTemplates();

            serverComboBox.setSelectedItem(destination.getServer());

            List<DestinationDto> destinations = filter(destination.getServer().getDestinations(), this::hasTemplate);

            destinationComboBox.setModel(new MutableCollectionComboBoxModel<>(destinations));
            destinationComboBox.setSelectedItem(destination);

            messageComboBox.setModel(new MutableCollectionComboBoxModel<>(templates));
            messageComboBox.setSelectedItem(template);
        } else {
            destinationComboBox.setSelectedIndex(-1);
            messageComboBox.setSelectedIndex(-1);
        }
    }

    private void render() {
        add(rootPanel);

        serverComboBox.addActionListener(event -> updateDestinationModel());
        destinationComboBox.addActionListener(event -> updateTemplateModel());
    }

    private void updateDestinationModel() {
        int idx = serverComboBox.getSelectedIndex();
        if (idx > -1) {
            List<DestinationDto> destinations = filter(serverComboBox.getItemAt(idx).getDestinations(), this::hasTemplate);
            destinationComboBox.setModel(new MutableCollectionComboBoxModel<>(destinations));
        } else {
            destinationComboBox.setModel(new MutableCollectionComboBoxModel<>());
        }
        updateTemplateModel();
    }

    private void updateTemplateModel() {
        int idx = destinationComboBox.getSelectedIndex();
        if (idx > -1) {
            messageComboBox.setModel(new MutableCollectionComboBoxModel<>(destinationComboBox.getItemAt(idx).getTemplates()));
        } else {
            messageComboBox.setModel(new MutableCollectionComboBoxModel<>());
        }
    }

    private void createUIComponents() {
        List<ServerDto> servers = filter(settings().getServersList(), this::hasTemplate);

        serverComboBox = new ComboBox<>(new DefaultComboBoxModel<>(servers.toArray(new ServerDto[0])));
        serverComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> label(value, ServerDto::getName));

        destinationComboBox = new ComboBox<>(new MutableCollectionComboBoxModel<>());
        destinationComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> label(value, DestinationDto::getName));

        messageComboBox = new ComboBox<>(new MutableCollectionComboBoxModel<>());
        messageComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> label(value, TemplateMessageDto::getName));
        updateDestinationModel();
    }

    private boolean hasTemplate(ServerDto server) {
        return server.getDestinations().stream().anyMatch(this::hasTemplate);
    }

    private boolean hasTemplate(DestinationDto destination) {
        return isNotEmpty(destination.getTemplates());
    }

}
