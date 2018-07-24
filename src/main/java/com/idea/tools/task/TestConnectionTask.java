package com.idea.tools.task;

import com.idea.tools.App;
import com.idea.tools.dto.ServerDto;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.idea.tools.App.jmsService;

public class TestConnectionTask extends Task.Backgroundable {

    private final ServerDto server;
    private final Runnable onSuccess;
    private final Consumer<Throwable> onFail;

    public TestConnectionTask(ServerDto server, Runnable onSuccess, Consumer<Throwable> onFail) {
        super(App.getProject(), "Test Connection");
        this.server = server;
        this.onSuccess = onSuccess;
        this.onFail = onFail;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        try {
            jmsService().testConnection(server);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onSuccess() {
        onSuccess.run();
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        onFail.accept(error);
    }
}
