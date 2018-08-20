package com.idea.tools.service;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.markers.Listener;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import static com.idea.tools.settings.Settings.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static java.util.UUID.randomUUID;

public class DestinationService extends AbstractPersistedService<DestinationDto> {

    public DestinationService(Project project) {
        Listener<DestinationDto> listener = Listener.<DestinationDto>builder()
                .add(settings(project)::put)
                .edit(settings(project)::put)
                .remove(settings(project)::remove)
                .build();

        addListener(listener);
    }

    protected void persist(DestinationDto destination) {
        destination.setId(randomUUID().toString());
    }

    @Override
    protected boolean isNew(DestinationDto destination) {
        return destination.getId() == null;
    }

    @Override
    protected boolean confirmRemove(DestinationDto destination) {
        return showYesNoDialog(String.format("Do you want to delete queue %s", destination.getName()));
    }

    public static DestinationService destinationService(Project project) {
        return ServiceManager.getService(project, DestinationService.class);
    }

}
