package com.idea.tools.service;

import com.idea.tools.dto.Queue;
import com.idea.tools.dto.Server;
import com.idea.tools.dto.ServerType;
import com.idea.tools.markers.Listener;
import com.idea.tools.settings.StateServerListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.idea.tools.App.settings;
import static javax.swing.JOptionPane.*;

public class ServerService {

    private List<Listener<Server>> listeners = new LinkedList<>();
    private AtomicInteger generator;

    public ServerService() {
        int value = settings().getState()
                              .getServersList()
                              .stream()
                              .map(Server::getId)
                              .max(Integer::compareTo)
                              .orElse(0);
        generator = new AtomicInteger(value);
        listeners.add(new StateServerListener());
    }

    public static List<Server> getDummies() {

        Server wildfly = new Server();
        wildfly.setId(0);
        wildfly.setName("Wildfly 1");
        wildfly.setQueues(Arrays.asList(new Queue("Q1"), new Queue("Q2")));
        wildfly.setType(ServerType.WILDFLY_11);

        Server activeMq = new Server();
        activeMq.setId(1);
        activeMq.setName("Active MQ 1");
        activeMq.setQueues(Arrays.asList(new Queue("Q1"), new Queue("Q2")));
        activeMq.setType(ServerType.ACTIVE_MQ);

        return Arrays.asList(wildfly, activeMq);
    }

    public void saveOrUpdate(Server server) {
        if (server.getId() == null) {
            server.setId(generator.incrementAndGet());
            listeners.forEach(listener -> listener.add(server));
        } else {
            listeners.forEach(listener -> listener.edit(server));
        }
    }

    public boolean remove(Server server) {
        if (server == null) {
            return false;
        }
        String msg = String.format("Do you want to delete server %s", server.getName());
        int response = showConfirmDialog(null, msg, "Confirm", YES_NO_OPTION, WARNING_MESSAGE);
        boolean delete = response == YES_OPTION;
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
