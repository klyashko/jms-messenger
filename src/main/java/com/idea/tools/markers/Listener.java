package com.idea.tools.markers;

import java.util.function.Consumer;

public interface Listener<T> {

    static <T> Listener<T> simple(Consumer<T> consumer) {
        return new Listener<T>() {
            @Override
            public void add(T item) {
                consumer.accept(item);
            }

            @Override
            public void edit(T item) {
                consumer.accept(item);
            }

            @Override
            public void remove(T item) {
                consumer.accept(item);
            }
        };
    }

    void add(T item);

    default void edit(T item) {}

    default void remove(T item) {}

}
