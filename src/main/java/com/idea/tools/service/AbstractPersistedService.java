package com.idea.tools.service;

import com.idea.tools.markers.Listener;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPersistedService<T> {

    private List<Listener<T>> listeners = new LinkedList<>();

    protected abstract void persist(T dto);

    protected abstract boolean isNew(T dto);

    protected abstract boolean confirmRemove(T dto);

    public void saveOrUpdate(T dto) {
        if (isNew(dto)) {
            persist(dto);
            listeners.forEach(listener -> listener.add(dto));
        } else {
            listeners.forEach(listener -> listener.edit(dto));
        }
    }

    public boolean remove(T dto) {
        if (dto == null) {
            return false;
        }
        boolean delete = confirmRemove(dto);
        if (delete) {
            listeners.forEach(listener -> listener.remove(dto));
        }
        return delete;
    }

    public void addListener(Listener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener<T> listener) {
        listeners.remove(listener);
    }

}
