package com.idea.tools.utils.function;

import java.util.function.Consumer;
import lombok.SneakyThrows;

@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {

    void acceptThrowing(T item) throws Throwable;

    @Override
    @SneakyThrows
    default void accept(T t) {
        acceptThrowing(t);
    }
}
