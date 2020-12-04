package com.idea.tools.settings;

import static com.idea.tools.service.ServerService.credentials;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.dto.TemplateMessageDto;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.MapAnnotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

@State(name = "JmsMessengerSettings", storages = @Storage("JmsMessengerSettings.xml"))
public class Settings implements PersistentStateComponent<Settings> {

	@MapAnnotation(
			keyAttributeName = "id",
			entryTagName = "server",
			surroundWithTag = false,
			surroundValueWithTag = false,
			surroundKeyWithTag = false
	)
	private final Map<String, ServerDto> servers = new HashMap<>();

	public static Settings settings(Project project) {
		Settings settings = ServiceManager.getService(project, Settings.class);
		settings.servers.forEach((id, server) -> {
			credentials(server).ifPresent(credentials -> {
				server.setLogin(credentials.getUserName());
				server.setPassword(credentials.getPasswordAsString());
			});
			server.getDestinations().forEach(destination -> {
				destination.setServer(server);
				destination.getTemplates().forEach(template -> template.setDestination(destination));
			});
		});
		return settings;
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

	public TemplateMessageDto getTemplate(String id) {
		return getServersList().stream()
				.flatMap(s -> s.getDestinations().stream())
				.flatMap(d -> d.getTemplates().stream())
				.filter(t -> t.getId().equals(id))
				.findAny()
				.orElse(null);
	}

	@Override
	public void loadState(@NotNull Settings state) {
		XmlSerializerUtil.copyBean(state, this);
	}

}
