package com.idea.tools.settings;

import com.idea.tools.dto.Queue;
import com.idea.tools.dto.Server;
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

    public List<Server> getServersList() {
        return getState().getServersList();
    }

    public Stream<Server> getServersStream() {
        return getState().getServersStream();
    }

    @Override
    public void loadState(@NotNull State state) {
        XmlSerializerUtil.copyBean(state, this.state);
    }

    public static class State {
        private Map<Integer, Server> servers = new HashMap<>();

        public void put(Server server) {
            if (server != null) {
                servers.put(server.getId(), server);
            }
        }

        public void put(Queue queue) {
            Server server = queue.getServer();
            List<Queue> queues = server.getQueues();
            for (int i = 0; i < queues.size(); i++) {
                Queue q = queues.get(i);
                if (q.getId().equals(queue.getId())) {
                    queues.set(i, queue);
                    return;
                }
            }
            queues.add(queue);
        }

        public void putAll(Collection<Server> servers) {
            servers.forEach(this::put);
        }

        public void remove(Server server) {
            if (server != null) {
                servers.remove(server.getId());
            }
        }

        public void remove(Queue queue) {
            Server server = queue.getServer();
            List<Queue> queues = server.getQueues();
            for (int i = 0; i < queues.size(); i++) {
                Queue q = queues.get(i);
                if (q.getId().equals(queue.getId())) {
                    queues.remove(i);
                    return;
                }
            }
        }

        public List<Server> getServersList() {
            return new ArrayList<>(servers.values());
        }

        public Stream<Server> getServersStream() {
            return getServersList().stream();
        }
    }

}
