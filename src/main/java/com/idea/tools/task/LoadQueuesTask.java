package com.idea.tools.task;

import static com.idea.tools.service.JmsService.jmsService;

import com.idea.tools.dto.ServerDto;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.project.Project;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class LoadQueuesTask extends Backgroundable {

	private final List<ServerDto> servers;
	private Optional<Runnable> onSuccess = Optional.empty();

	public LoadQueuesTask(@NotNull Project project, @NotNull List<ServerDto> servers) {
		super(project, "Loading Queues");
		this.servers = servers;
	}

	private LoadQueuesTask(@NotNull Project project, @NotNull List<ServerDto> servers, Runnable onSuccess) {
		this(project, servers);
		this.onSuccess = Optional.of(onSuccess);
	}

	public static void createAndQueue(@NotNull Project project, @NotNull List<ServerDto> servers, Runnable onSuccess) {
		new LoadQueuesTask(project, servers, onSuccess).queue();
	}

	@Override
	public void run(@NotNull ProgressIndicator indicator) {
		jmsService(getProject()).refresh(servers);
	}

	@Override
	public void onSuccess() {
		onSuccess.ifPresent(Runnable::run);
	}

}
