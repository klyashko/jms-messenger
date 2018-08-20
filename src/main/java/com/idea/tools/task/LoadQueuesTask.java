package com.idea.tools.task;

import com.idea.tools.dto.ServerDto;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static com.idea.tools.service.JmsService.jmsService;

public class LoadQueuesTask extends Task.Backgroundable {

    private final List<ServerDto> servers;
    private Optional<Runnable> onSuccess = Optional.empty();

    public LoadQueuesTask(@NotNull Project project, @NotNull List<ServerDto> servers) {
        super(project, "Loading Queues");
        this.servers = servers;
    }

    public LoadQueuesTask(@NotNull Project project, @NotNull List<ServerDto> servers, Runnable onSuccess) {
        this(project, servers);
        this.onSuccess = Optional.of(onSuccess);
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
