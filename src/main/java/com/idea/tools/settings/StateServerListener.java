package com.idea.tools.settings;

import com.idea.tools.dto.Server;
import com.idea.tools.markers.Listener;

import static com.idea.tools.App.settings;

public class StateServerListener implements Listener<Server> {

    @Override
    public void add(Server item) {
        settings().getState().put(item);
    }

    @Override
    public void edit(Server item) {
        settings().getState().put(item);
    }

    @Override
    public void remove(Server item) {
        settings().getState().remove(item);
    }
}
