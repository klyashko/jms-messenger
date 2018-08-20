package com.idea.tools.task;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.idea.tools.service.JmsService.jmsService;
import static com.idea.tools.utils.Checked.consumer;

public class RemoveMessageTask extends Task.Backgroundable {

    private final DestinationDto destination;
    private final List<MessageDto> messages;
    private final QueueBrowserTable table;

    public RemoveMessageTask(Project project, DestinationDto destination, List<MessageDto> messages, QueueBrowserTable table) {
        super(project, "Remove Messages");
        this.destination = destination;
        this.messages = messages;
        this.table = table;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        messages.forEach(consumer(msg -> jmsService(getProject()).removeFromQueue(msg, destination)));
    }

    @Override
    public void onSuccess() {
        new LoadMessagesTask(getProject(), table).queue();
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        new LoadMessagesTask(getProject(), table).queue();
    }
}
