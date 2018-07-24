package com.idea.tools.task;

import com.idea.tools.App;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.idea.tools.App.jmsService;
import static com.idea.tools.utils.Checked.consumer;

public class RemoveMessageTask extends Task.Backgroundable {

    private final QueueDto queue;
    private final List<MessageDto> messages;
    private final QueueBrowserTable table;

    public RemoveMessageTask(QueueDto queue, List<MessageDto> messages, QueueBrowserTable table) {
        super(App.getProject(), "Remove Messages");
        this.queue = queue;
        this.messages = messages;
        this.table = table;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        messages.forEach(consumer(msg -> jmsService().removeFromQueue(msg, queue)));
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
