package com.idea.tools.settings;

import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.dto.TemplateMessageDto;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.MapAnnotation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@State(name = "JmsMessengerSettings", storages = @Storage("JmsMessengerSettings.xml"))
public class Settings implements PersistentStateComponent<Settings> {

    private static Optional<Settings> settings = Optional.empty();

    @MapAnnotation(
            keyAttributeName = "id",
            entryTagName = "server",
            surroundWithTag = false,
            surroundValueWithTag = false,
            surroundKeyWithTag = false
    )
    private Map<String, ServerDto> servers = new HashMap<>();

    public static Settings getOrCreate(Project project) {
        return settings.orElseGet(() -> {
            Settings settings = ServiceManager.getService(project, Settings.class);
            settings.servers.forEach((id, server) -> server.getQueues().forEach(queue -> queue.setServer(server)));
            Settings.settings = Optional.of(settings);
            return settings;
        });
    }

    @NotNull
    @Override
    public Settings getState() {
        return this;
    }

    public List<ServerDto> getServersList() {
        return new ArrayList<>(servers.values());
    }

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

    public void put(TemplateMessageDto template) {
        QueueDto queue = template.getQueue();
        List<TemplateMessageDto> templates = queue.getTemplates();

        for (int i = 0; i < templates.size(); i++) {
            TemplateMessageDto t = templates.get(i);
            if (t.getId().equals(template.getId())) {
                templates.set(i, template);
                return;
            }
        }
        templates.add(template);
    }

    public void remove(ServerDto server) {
        if (server != null) {
            servers.remove(server.getId());
        }
    }

    public void remove(QueueDto queue) {
        queue.getServer().getQueues().removeIf(q -> q.getId().equals(queue.getId()));
    }

    public void remove(TemplateMessageDto template) {
        template.getQueue().getTemplates().removeIf(t -> t.getId().equals(template.getId()));
    }

    @Override
    public void loadState(@NotNull Settings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
