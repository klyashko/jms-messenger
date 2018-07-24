package com.idea.tools.task;

import com.idea.tools.App;
import com.idea.tools.dto.MessageDto;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import static com.idea.tools.App.jmsService;

public class SendMessageTask extends Task.Backgroundable {

    private static final Logger LOGGER = Logger.getInstance(SendMessageTask.class);

    private static final String SEND_SUCCESS_TEMPLATE = "Message has been successfully sent to destination %s";
    private static final String SEND_FAIL_TEMPLATE = "Message hasn't been sent. Reason: \n%s";

    private final MessageDto message;
    private final Runnable onSuccess;

    public SendMessageTask(MessageDto message, Runnable onSuccess) {
        super(App.getProject(), "Sending Message");
        this.message = message;
        this.onSuccess = onSuccess;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        try {
            jmsService().send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSuccess() {
        String content = String.format(SEND_SUCCESS_TEMPLATE, message.getDestination().getName());
        Notifications.Bus.notify(new Notification("jms", "Success", content, NotificationType.INFORMATION));
        onSuccess.run();
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        String content = String.format(SEND_FAIL_TEMPLATE, error.getMessage());
        Notifications.Bus.notify(new Notification("jms", "Failure", content, NotificationType.ERROR));
        LOGGER.error("An exception has been thrown during a message sending", error);
    }
}
