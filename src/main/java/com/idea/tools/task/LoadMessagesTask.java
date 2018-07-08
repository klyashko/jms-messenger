package com.idea.tools.task;

import com.idea.tools.App;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.idea.tools.App.jmsService;

public class LoadMessagesTask extends Task.Backgroundable {

    private static final Logger LOGGER = Logger.getInstance(LoadMessagesTask.class);

    private final QueueBrowserTable table;
    private List<MessageDto> messages;

    public LoadMessagesTask(@NotNull QueueBrowserTable table) {
        super(App.getProject(), "Loading Messages");
        this.table = table;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        try {
            messages = jmsService().receive(table.getQueue());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("An exception han been thrown during loading messages from a queue", e, table.getQueue().getName());
            messages = Collections.emptyList();
        }
    }

    @Override
    public void onSuccess() {
        table.setData(messages);
    }

}
