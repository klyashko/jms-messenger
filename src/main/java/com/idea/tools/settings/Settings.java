/*
 * Copyright (c) 2013 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idea.tools.settings;

import com.idea.tools.dto.Server;
import com.idea.tools.markers.Listener;
import com.idea.tools.service.ServerService;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.idea.tools.App.settings;

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

        public void putAll(Collection<Server> servers) {
            servers.forEach(this::put);
        }

        public void remove(Server server) {
            if (server != null) {
                servers.remove(server.getId());
            }
        }

        public List<Server> getServersList() {
            return new ArrayList<>(servers.values());
        }
    }

    public static class StateServerListener implements Listener<Server> {

        @Override
        public void add(Server item) {
            settings().getState().put(item);
        }

        @Override
        public void edit(Server item) {
            settings().getState().put(item);
        }

        @Override
        public void remove(Server item) {
            settings().getState().remove(item);
        }
    }
}
