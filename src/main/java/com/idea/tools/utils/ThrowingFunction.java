package com.idea.tools.utils;

import lombok.SneakyThrows;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {

    R applyThrowing(T value) throws Throwable;

    @Override
    @SneakyThrows
    default R apply(T t) {
        return applyThrowing(t);
    }

    default Optional<R> toOptional(T t) {
        try {
            return Optional.of(applyThrowing(t));
        } catch (Throwable throwable) {
            return Optional.empty();
        }
    }

}
