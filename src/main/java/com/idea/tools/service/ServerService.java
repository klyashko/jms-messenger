package com.idea.tools.service;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.markers.Listener;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;

import java.util.Optional;

import static com.idea.tools.settings.Settings.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static java.util.UUID.randomUUID;

public class ServerService extends AbstractPersistedService<ServerDto> {

	public ServerService(Project project) {
		Listener<ServerDto> listener = Listener.<ServerDto>builder()
				.add(s -> {
					settings(project).put(s);
					storeCredentials(s);
				})
				.edit(s -> {
					settings(project).put(s);
					storeCredentials(s);
				})
				.remove(s -> {
					settings(project).remove(s);
					s.setLogin("");
					storeCredentials(s);
				})
				.build();

		addListener(listener);
	}

	@Deprecated
	public static void updateCredentials(ServerDto dto) {
		storeCredentials(dto);
	}

	public static Optional<Credentials> credentials(ServerDto dto) {
		PasswordSafe passwordSafe = PasswordSafe.getInstance();
		Credentials credentials = passwordSafe.get(credentialAttributes(dto));
		return Optional.ofNullable(credentials);
	}

	public static ServerService serverService(Project project) {
		return ServiceManager.getService(project, ServerService.class);
	}

	private static void storeCredentials(ServerDto dto) {
		CredentialAttributes attributes = credentialAttributes(dto);
		Credentials credentials = null;
		if (StringUtils.isNotBlank(dto.getLogin())) {
			credentials = new Credentials(dto.getLogin(), dto.getPassword());
		}
		PasswordSafe.getInstance().set(attributes, credentials);
	}

	private static CredentialAttributes credentialAttributes(ServerDto dto) {
		return new CredentialAttributes(dto.getId());
	}

	@Override
	protected void persist(ServerDto server) {
		server.setId(randomUUID().toString());
	}

	@Override
	protected boolean isNew(ServerDto server) {
		return server.getId() == null;
	}

	@Override
	protected boolean confirmRemove(ServerDto server) {
		return showYesNoDialog(String.format("Do you want to delete server %s", server.getName()));
	}

}
