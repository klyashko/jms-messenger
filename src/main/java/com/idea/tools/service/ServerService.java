package com.idea.tools.service;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.markers.Listener;

import java.util.LinkedList;
import java.util.List;

import static com.idea.tools.App.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static java.util.UUID.randomUUID;

public class ServerService {

    private List<Listener<ServerDto>> listeners = new LinkedList<>();

    public ServerService() {
        Listener<ServerDto> listener = Listener.<ServerDto>builder()
                .add(settings()::put)
                .edit(settings()::put)
                .remove(settings()::remove)
                .build();

        listeners.add(listener);
    }

    public void saveOrUpdate(ServerDto server) {
        if (server.getId() == null) {
            server.setId(randomUUID().toString());
            listeners.forEach(listener -> listener.add(server));
        } else {
            listeners.forEach(listener -> listener.edit(server));
        }
    }

    public boolean remove(ServerDto server) {
        if (server == null) {
            return false;
        }
        boolean delete = showYesNoDialog(String.format("Do you want to delete server %s", server.getName()));

        if (delete) {
            listeners.forEach(listener -> listener.remove(server));
        }
        return delete;
    }

    public void addListener(Listener<ServerDto> listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener<ServerDto> listener) {
        listeners.remove(listener);
    }

}
