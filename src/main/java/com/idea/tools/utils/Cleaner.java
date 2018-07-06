package com.idea.tools.utils;

import java.util.HashMap;
import java.util.Map;

public class Cleaner implements AutoCloseable {

    private Map<Object, ThrowingConsumer> toCleanUp = new HashMap<>();

    public <T> T register(ThrowingSupplier<T> creator, ThrowingConsumer<T> cleaner) {
        T value = creator.get();
        toCleanUp.put(value, cleaner);
        return value;
    }

    @Override
    public void close() {
        Throwable throwable = null;
        for (Object obj : toCleanUp.keySet()) {
            try {
                //noinspection unchecked
                toCleanUp.get(obj).acceptThrowing(obj);
            } catch (Throwable t) {
                t.printStackTrace();
                throwable = t;
            }
        }
        if (throwable != null) {
            throw new RuntimeException(throwable);
        }
    }

}
