package com.idea.tools.utils;

import lombok.SneakyThrows;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {

    void acceptThrowing(T item) throws Throwable;

    @Override
    @SneakyThrows
    default void accept(T t) {
        acceptThrowing(t);
    }
}
