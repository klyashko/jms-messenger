package com.idea.tools.view;

import static com.idea.tools.settings.Settings.settings;
import static com.idea.tools.utils.GuiUtils.label;
import static com.idea.tools.utils.Utils.filter;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.dto.TemplateMessageDto;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.MutableCollectionComboBoxModel;
import java.util.List;
import javax.swing.*;

public class JmsSettingsEditorPanel extends JPanel {

    private final Project project;

    private JComboBox<ServerDto> serverComboBox;
    private JPanel rootPanel;
    private JComboBox<DestinationDto> destinationComboBox;
    private JComboBox<TemplateMessageDto> messageComboBox;

    public JmsSettingsEditorPanel(Project project, String templateId) {
        this(project);
        setSelectedTemplateId(templateId);
    }

    public JmsSettingsEditorPanel(Project project) {
        this.project = project;
        render();
    }

    public String getSelectedTemplateId() {
        int idx = messageComboBox.getSelectedIndex();
        if (idx > -1) {
            return messageComboBox.getItemAt(idx).getId();
        }
        return null;
    }

    public void setSelectedTemplateId(String id) {
        TemplateMessageDto template = settings(project).getTemplate(id);
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
        List<ServerDto> servers = filter(settings(project).getServersList(), this::hasTemplate);

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
