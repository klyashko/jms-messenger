package com.idea.tools.settings;

import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.service.ServerService;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

@State(
        name = "Messenger.Application.Settings",
        storages = {
                @Storage(file = "$PROJECT_FILE$"),
                @Storage(file = "$PROJECT_CONFIG_DIR$/jenkinsSettings.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public class Settings implements PersistentStateComponent<Settings.State> {

    private static Optional<Settings> settings = Optional.empty();

    private State state;

    private Settings() {
        State state = new State();
        state.putAll(ServerService.getDummies());
        this.state = state;
    }

    public static Settings getOrCreate(Project project) {
        return settings.orElseGet(() -> {
            Settings settings = ServiceManager.getService(project, Settings.class);
            settings = settings != null ? settings : new Settings();
            Settings.settings = Optional.of(settings);
            return settings;
        });
    }

    @NotNull
    @Override
    public State getState() {
        return state;
    }

    public List<ServerDto> getServersList() {
        return getState().getServersList();
    }

    @Override
    public void loadState(@NotNull State state) {
        XmlSerializerUtil.copyBean(state, this.state);
    }

    public static class State {
        private Map<UUID, ServerDto> servers = new HashMap<>();

        public void put(ServerDto server) {
            if (server != null) {
                servers.put(server.getId(), server);
            }
        }

        public void put(QueueDto queue) {
            ServerDto server = queue.getServer();
            List<QueueDto> queues = server.getQueues();
            for (int i = 0; i < queues.size(); i++) {
                QueueDto q = queues.get(i);
                if (q.getId().equals(queue.getId())) {
                    queues.set(i, queue);
                    return;
                }
            }
            queues.add(queue);
        }

        public void putAll(Collection<ServerDto> servers) {
            servers.forEach(this::put);
        }

        public void remove(ServerDto server) {
            if (server != null) {
                servers.remove(server.getId());
            }
        }

        public void remove(QueueDto queue) {
            ServerDto server = queue.getServer();
            List<QueueDto> queues = server.getQueues();
            for (int i = 0; i < queues.size(); i++) {
                QueueDto q = queues.get(i);
                if (q.getId().equals(queue.getId())) {
                    queues.remove(i);
                    return;
                }
            }
        }

        public List<ServerDto> getServersList() {
            return new ArrayList<>(servers.values());
        }

        public Stream<ServerDto> getServersStream() {
            return getServersList().stream();
        }
    }

}
