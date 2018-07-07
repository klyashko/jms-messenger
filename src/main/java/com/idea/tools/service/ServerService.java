package com.idea.tools.service;

import com.idea.tools.dto.ConnectionType;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.Server;
import com.idea.tools.dto.ServerType;
import com.idea.tools.markers.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.idea.tools.App.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static java.util.UUID.randomUUID;

public class ServerService {

    private List<Listener<Server>> listeners = new LinkedList<>();

    public ServerService() {
        Listener<Server> listener = Listener.<Server>builder()
                .add(settings().getState()::put)
                .edit(settings().getState()::put)
                .remove(settings().getState()::remove)
                .build();

        listeners.add(listener);
    }

    public static List<Server> getDummies() {

        Server wildfly = new Server();
        wildfly.setId(randomUUID());
        wildfly.setName("Wildfly 1");
        wildfly.setQueues(new ArrayList<>(Arrays.asList(new QueueDto(randomUUID(), "Q1", wildfly), new QueueDto(randomUUID(), "Q2", wildfly))));
        wildfly.setType(ServerType.WILDFLY_11);

        Server activeMq = new Server();
        activeMq.setId(randomUUID());
        activeMq.setName("Active MQ 1");
        activeMq.setHost("localhost");
        activeMq.setPort(61616);
        activeMq.setConnectionType(ConnectionType.TCP);
        activeMq.setQueues(new ArrayList<>(Arrays.asList(new QueueDto(randomUUID(), "Q1", activeMq, true), new QueueDto(randomUUID(), "Q2", activeMq))));
        activeMq.setType(ServerType.ACTIVE_MQ);

        return Arrays.asList(wildfly, activeMq);
    }

    public void saveOrUpdate(Server server) {
        if (server.getId() == null) {
            server.setId(randomUUID());
            listeners.forEach(listener -> listener.add(server));
        } else {
            listeners.forEach(listener -> listener.edit(server));
        }
    }

    public boolean remove(Server server) {
        if (server == null) {
            return false;
        }
        boolean delete = showYesNoDialog(String.format("Do you want to delete server %s", server.getName()));

        if (delete) {
            listeners.forEach(listener -> listener.remove(server));
        }
        return delete;
    }

    public void addListener(Listener<Server> listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener<Server> listener) {
        listeners.remove(listener);
    }

}
