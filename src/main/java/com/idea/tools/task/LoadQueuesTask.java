package com.idea.tools.task;

import com.idea.tools.App;
import com.idea.tools.dto.ServerDto;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static com.idea.tools.App.jmsService;

public class LoadQueuesTask extends Task.Backgroundable {

    private final List<ServerDto> servers;
    private Optional<Runnable> onSuccess = Optional.empty();

    public LoadQueuesTask(@NotNull List<ServerDto> servers) {
        super(App.getProject(), "Loading Queues");
        this.servers = servers;
    }

    public LoadQueuesTask(@NotNull List<ServerDto> servers, Runnable onSuccess) {
        this(servers);
        this.onSuccess = Optional.of(onSuccess);
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        jmsService().refresh(servers);
    }

    @Override
    public void onSuccess() {
        onSuccess.ifPresent(Runnable::run);
    }
}
