package com.idea.tools.service;

import com.idea.tools.dto.ConnectionType;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.ServerDto;
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

    private List<Listener<ServerDto>> listeners = new LinkedList<>();

    public ServerService() {
        Listener<ServerDto> listener = Listener.<ServerDto>builder()
                .add(settings().getState()::put)
                .edit(settings().getState()::put)
                .remove(settings().getState()::remove)
                .build();

        listeners.add(listener);
    }

    public static List<ServerDto> getDummies() {

        ServerDto artemis = new ServerDto();
        artemis.setId(randomUUID());
        artemis.setName("Artemis 1");
        artemis.setHost("localhost");
        artemis.setPort(8080);
        artemis.setConnectionType(ConnectionType.HTTP);
        artemis.setQueues(new ArrayList<>(Arrays.asList(
                new QueueDto(randomUUID(), "Q1", artemis),
                new QueueDto(randomUUID(), "Q2", artemis))
        ));
        artemis.setType(ServerType.ARTEMIS);

        ServerDto hornet = new ServerDto();
        hornet.setId(randomUUID());
        hornet.setName("Hornet 1");
        hornet.setHost("localhost");
        hornet.setPort(8080);
        hornet.setConnectionType(ConnectionType.HTTP);
        hornet.setQueues(new ArrayList<>(Arrays.asList(
                new QueueDto(randomUUID(), "tre.priv.module.mifirt1.requestQueue", hornet),
                new QueueDto(randomUUID(), "regrep.DTCCResponseQueue", hornet))
        ));
        hornet.setType(ServerType.HORNETQ);

        ServerDto activeMq = new ServerDto();
        activeMq.setId(randomUUID());
        activeMq.setName("Active MQ 1");
        activeMq.setHost("localhost");
        activeMq.setPort(61616);
        activeMq.setConnectionType(ConnectionType.TCP);
        activeMq.setQueues(new ArrayList<>(Arrays.asList(
                new QueueDto(randomUUID(), "Q1", activeMq, true),
                new QueueDto(randomUUID(), "Q2", activeMq))
        ));
        activeMq.setType(ServerType.ACTIVE_MQ);

        return Arrays.asList(artemis, hornet, activeMq);
    }

    public void saveOrUpdate(ServerDto server) {
        if (server.getId() == null) {
            server.setId(randomUUID());
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
