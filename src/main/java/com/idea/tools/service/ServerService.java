package com.idea.tools.service;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.markers.Listener;

import static com.idea.tools.App.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static java.util.UUID.randomUUID;

public class ServerService extends AbstractPersistedService<ServerDto> {

    public ServerService() {
        Listener<ServerDto> listener = Listener.<ServerDto>builder()
                .add(settings()::put)
                .edit(settings()::put)
                .remove(settings()::remove)
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

}
