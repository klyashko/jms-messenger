package com.idea.tools.service;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.markers.Listener;

import java.util.LinkedList;
import java.util.List;

import static com.idea.tools.App.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static java.util.UUID.randomUUID;

public class QueueService {

    private List<Listener<QueueDto>> listeners = new LinkedList<>();

    public QueueService() {
        Listener<QueueDto> listener = Listener.<QueueDto>builder()
                .add(settings().getState()::put)
                .edit(settings().getState()::put)
                .remove(settings().getState()::remove)
                .build();

        listeners.add(listener);
    }

    public boolean removeFromQueue(MessageDto messageDto, QueueDto queue) {
        return false;
    }

    public void persist(QueueDto dto) {
        if (dto.getId() == null) {
            dto.setId(randomUUID());
        }
    }

    public void saveOrUpdate(QueueDto queue) {
        if (queue.getId() == null) {
            persist(queue);
            listeners.forEach(listener -> listener.add(queue));
        } else {
            listeners.forEach(listener -> listener.edit(queue));
        }
    }

    public boolean remove(QueueDto queue) {
        if (queue == null) {
            return false;
        }
        boolean delete = showYesNoDialog(String.format("Do you want to delete queue %s", queue.getName()));
        if (delete) {
            listeners.forEach(listener -> listener.remove(queue));
        }
        return delete;
    }

    public void addListener(Listener<QueueDto> listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener<QueueDto> listener) {
        listeners.remove(listener);
    }

}
