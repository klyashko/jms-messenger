package com.idea.tools.service;

import com.idea.tools.dto.MessageEntity;
import com.idea.tools.dto.Queue;
import com.idea.tools.markers.Listener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.idea.tools.App.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;

public class QueueService {

    private List<Listener<Queue>> listeners = new LinkedList<>();
    private AtomicInteger generator;

    public QueueService() {
        int value = settings().getServersStream()
                              .flatMap(s -> s.getQueues().stream())
                              .map(Queue::getId)
                              .max(Integer::compareTo)
                              .orElse(0);
        generator = new AtomicInteger(value);

        Listener<Queue> listener = Listener.<Queue>builder()
                .add(settings().getState()::put)
                .edit(settings().getState()::put)
                .remove(settings().getState()::remove)
                .build();

        listeners.add(listener);
    }

    public List<MessageEntity> receive(Queue queue) {
        //TODO implement receiving messages
        return Collections.emptyList();
    }

    public boolean removeFromQueue(MessageEntity messageEntity, Queue queue) {
        return false;
    }

    public void saveOrUpdate(Queue queue) {
        if (queue.getId() == null) {
            queue.setId(generator.incrementAndGet());
            listeners.forEach(listener -> listener.add(queue));
        } else {
            listeners.forEach(listener -> listener.edit(queue));
        }
    }

    public boolean remove(Queue queue) {
        if (queue == null) {
            return false;
        }
        boolean delete = showYesNoDialog(String.format("Do you want to delete queue %s", queue.getName()));
        if (delete) {
            listeners.forEach(listener -> listener.remove(queue));
        }
        return delete;
    }

    public void addListener(Listener<Queue> listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener<Queue> listener) {
        listeners.remove(listener);
    }

}
