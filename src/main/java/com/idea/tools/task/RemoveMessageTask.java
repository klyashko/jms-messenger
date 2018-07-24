package com.idea.tools.task;

import com.idea.tools.App;
import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.idea.tools.App.jmsService;
import static com.idea.tools.utils.Checked.consumer;

public class RemoveMessageTask extends Task.Backgroundable {

    private final DestinationDto destination;
    private final List<MessageDto> messages;
    private final QueueBrowserTable table;

    public RemoveMessageTask(DestinationDto destination, List<MessageDto> messages, QueueBrowserTable table) {
        super(App.getProject(), "Remove Messages");
        this.destination = destination;
        this.messages = messages;
        this.table = table;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        messages.forEach(consumer(msg -> jmsService().removeFromQueue(msg, destination)));
    }

    @Override
    public void onSuccess() {
        new LoadMessagesTask(table).queue();
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        new LoadMessagesTask(table).queue();
    }
}
