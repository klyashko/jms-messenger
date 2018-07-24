package com.idea.tools.settings;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.DestinationType;
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
            settings.servers.forEach((id, server) -> {
                        //TODO delete after migration
                        server.getQueues().forEach(queue -> {
                            DestinationDto destination = new DestinationDto();
                            destination.setId(queue.getId());
                            destination.setName(queue.getName());
                            destination.setAddedManually(queue.isAddedManually());
                            destination.setTemplates(queue.getTemplates());
                            destination.setType(DestinationType.QUEUE);
                            server.getDestinations().add(destination);
                        });
                        server.getQueues().clear();
                        server.getDestinations().forEach(destination -> {
                            destination.setServer(server);
                            destination.getTemplates().forEach(template -> template.setDestination(destination));
                        });
                    }
            );
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

    public void put(DestinationDto destination) {
        ServerDto server = destination.getServer();
        List<DestinationDto> destinations = server.getDestinations();
        for (int i = 0; i < destinations.size(); i++) {
            DestinationDto d = destinations.get(i);
            if (d.getId().equals(destination.getId())) {
                destinations.set(i, destination);
                return;
            }
        }
        destinations.add(destination);
    }

    public void put(TemplateMessageDto template) {
        DestinationDto destination = template.getDestination();
        List<TemplateMessageDto> templates = destination.getTemplates();

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

    public void remove(DestinationDto destination) {
        destination.getServer().getDestinations().removeIf(q -> q.getId().equals(destination.getId()));
    }

    public void remove(TemplateMessageDto template) {
        template.getDestination().getTemplates().removeIf(t -> t.getId().equals(template.getId()));
    }

    @Override
    public void loadState(@NotNull Settings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
