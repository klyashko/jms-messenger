package com.idea.tools.service;

import com.idea.tools.dto.QueueDto;
import com.idea.tools.markers.Listener;

import static com.idea.tools.App.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static java.util.UUID.randomUUID;

public class QueueService extends AbstractPersistedService<QueueDto> {

    public QueueService() {
        Listener<QueueDto> listener = Listener.<QueueDto>builder()
                .add(settings()::put)
                .edit(settings()::put)
                .remove(settings()::remove)
                .build();

        addListener(listener);
    }

    protected void persist(QueueDto queue) {
        queue.setId(randomUUID().toString());
    }

    @Override
    protected boolean isNew(QueueDto queue) {
        return queue.getId() == null;
    }

    @Override
    protected boolean confirmRemove(QueueDto queue) {
        return showYesNoDialog(String.format("Do you want to delete queue %s", queue.getName()));
    }

}
