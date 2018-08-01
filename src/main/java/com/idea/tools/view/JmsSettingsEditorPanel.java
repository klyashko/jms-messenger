package com.idea.tools.view;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.markers.Listener;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.MutableCollectionComboBoxModel;

import javax.swing.*;
import java.util.List;

import static com.idea.tools.App.*;
import static com.idea.tools.utils.GuiUtils.label;
import static com.idea.tools.utils.Utils.filter;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public class JmsSettingsEditorPanel extends JPanel {
    private JComboBox<ServerDto> serverComboBox;
    private JPanel rootPanel;
    private JComboBox<DestinationDto> destinationComboBox;
    private JComboBox<TemplateMessageDto> messageComboBox;
    private Listener<ServerDto> serverListener;
    private Listener<DestinationDto> destinationListener;
    private Listener<TemplateMessageDto> templateMessageListener;

    public JmsSettingsEditorPanel() {
        render();
        serverListener = Listener.<ServerDto>builder()
                .add(this::addServer)
                .remove(this::removeServer)
                .build();
        destinationListener = Listener.<DestinationDto>builder()
                .add(this::addDestination)
                .remove(this::removeDestination)
                .build();
        templateMessageListener = Listener.<TemplateMessageDto>builder()
                .add(this::addTemplate)
                .remove(this::removeTemplate)
                .build();

        serverService().addListener(serverListener);
        destinationService().addListener(destinationListener);
        templateService().addListener(templateMessageListener);
    }

    public void dispose() {
        serverService().removeListener(serverListener);
        destinationService().removeListener(destinationListener);
        templateService().removeListener(templateMessageListener);
    }

    public String getSelectedTemplateId() {
        int idx = messageComboBox.getSelectedIndex();
        if (idx > -1) {
            return messageComboBox.getItemAt(idx).getId();
        }
        return null;
    }

    public void setSelectedTemplateId(String id) {
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
            messageComboBox.setModel(new MutableCollectionComboBoxModel<>(templates));
            messageComboBox.setSelectedItem(template);

            List<DestinationDto> destinations = filter(destination.getServer().getDestinations(), this::hasTemplate);

            destinationComboBox.setModel(new MutableCollectionComboBoxModel<>(destinations));
            destinationComboBox.setSelectedItem(destination);
        } else {
            messageComboBox.setSelectedIndex(-1);
            destinationComboBox.setSelectedIndex(-1);
        }
    }

    private void addServer(ServerDto server) {
        if (hasTemplate(server)) {
            DefaultComboBoxModel<ServerDto> model = (DefaultComboBoxModel<ServerDto>) serverComboBox.getModel();
            if (model.getIndexOf(server) == -1) {
                serverComboBox.addItem(server);
//                serverComboBox.repaint();
            }
        }
    }

    private void removeServer(ServerDto server) {
        serverComboBox.removeItem(server);
//        MutableCollectionComboBoxModel<ServerDto> model = (MutableCollectionComboBoxModel<ServerDto>) serverComboBox.getModel();
//        model.remove(server);
//        serverComboBox.repaint();
    }

    private void addDestination(DestinationDto destination) {
        if (hasTemplate(destination)) {
            if (destination.getServer().equals(serverComboBox.getSelectedItem())) {
                MutableCollectionComboBoxModel<DestinationDto> model = (MutableCollectionComboBoxModel<DestinationDto>) destinationComboBox.getModel();
                if (!model.contains(destination)) {
                    model.add(destination);
                    destinationComboBox.repaint();
                }
            } else {
                addServer(destination.getServer());
            }
        }
    }

    private void removeDestination(DestinationDto destination) {
        if (destination.getServer().equals(serverComboBox.getSelectedItem())) {
            MutableCollectionComboBoxModel<DestinationDto> model = (MutableCollectionComboBoxModel<DestinationDto>) destinationComboBox.getModel();
            model.remove(destination);
            destinationComboBox.repaint();
        }
        if (!hasTemplate(destination.getServer())) {
            removeServer(destination.getServer());
        }
    }

    private void addTemplate(TemplateMessageDto template) {
        if (template.getDestination().equals(destinationComboBox.getSelectedItem())) {
            MutableCollectionComboBoxModel<TemplateMessageDto> model = (MutableCollectionComboBoxModel<TemplateMessageDto>) messageComboBox.getModel();
            model.add(template);
            messageComboBox.repaint();
        }
        addDestination(template.getDestination());
    }

    private void removeTemplate(TemplateMessageDto template) {
        if (template.getDestination().equals(destinationComboBox.getSelectedItem())) {
            MutableCollectionComboBoxModel<TemplateMessageDto> model = (MutableCollectionComboBoxModel<TemplateMessageDto>) messageComboBox.getModel();
            model.remove(template);
            messageComboBox.repaint();
        }
        if (!hasTemplate(template.getDestination())) {
            removeDestination(template.getDestination());
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
