package com.idea.tools.task;

import static com.idea.tools.service.JmsService.jmsService;
import static com.idea.tools.utils.GuiUtils.runInSwingThread;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class LoadMessagesTask extends Task.Backgroundable {

	private static final Logger LOGGER = Logger.getInstance(LoadMessagesTask.class);

	private final QueueBrowserTable table;
	private List<MessageDto> messages;

	public LoadMessagesTask(@NotNull Project project, @NotNull QueueBrowserTable table) {
		super(project, "Loading Messages");
		this.table = table;
	}

	@Override
	public void run(@NotNull ProgressIndicator indicator) {
		try {
			messages = jmsService(getProject()).receive(table.getDestination());
			Collections.sort(messages);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("An exception han been thrown during loading messages from a queue", e, table.getDestination().getName());
			messages = Collections.emptyList();
		}
	}

	@Override
	public void onSuccess() {
		runInSwingThread(() -> table.setData(messages));
	}

}
