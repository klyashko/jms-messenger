package com.idea.tools.service;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.markers.Listener;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import static com.idea.tools.settings.Settings.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static java.util.UUID.randomUUID;

public class ServerService extends AbstractPersistedService<ServerDto> {

    public ServerService(Project project) {
        Listener<ServerDto> listener = Listener.<ServerDto>builder()
                .add(settings(project)::put)
                .edit(settings(project)::put)
                .remove(settings(project)::remove)
                .build();

        addListener(listener);
    }

    @Override
    protected void persist(ServerDto server) {
        server.setId(randomUUID().toString());
    }

    @Override
    protected boolean isNew(ServerDto server) {
        return server.getId() == null;
    }

    @Override
    protected boolean confirmRemove(ServerDto server) {
        return showYesNoDialog(String.format("Do you want to delete server %s", server.getName()));
    }

    public static ServerService serverService(Project project) {
        return ServiceManager.getService(project, ServerService.class);
    }

}
