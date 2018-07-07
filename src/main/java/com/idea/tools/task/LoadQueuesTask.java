package com.idea.tools.task;

import com.idea.tools.App;
import com.idea.tools.dto.Server;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.idea.tools.App.jmsService;

public class LoadQueuesTask extends Task.Backgroundable {

    private final List<Server> servers;

    public LoadQueuesTask(@NotNull List<Server> servers) {
        super(App.getProject(), "Loading Queues");
        this.servers = servers;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        jmsService().refresh(servers);
    }

}
