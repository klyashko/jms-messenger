package com.idea.tools.utils.function;

import lombok.SneakyThrows;

import java.util.Optional;
import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {

    T getThrowing() throws Throwable;

    @Override
    @SneakyThrows
    default T get() {
        return getThrowing();
    }

    default Optional<T> getOptional() {
        try {
            return Optional.ofNullable(getThrowing());
        } catch (Throwable throwable) {
            return Optional.empty();
        }
    }
}
