package com.idea.tools.utils;

import lombok.SneakyThrows;

import java.util.Optional;
import java.util.function.BiFunction;

public interface ThrowingBiFunction<T, U, R> extends BiFunction<T, U, R> {

    R applyThrowing(T t, U u) throws Throwable;

    @Override
    @SneakyThrows
    default R apply(T t, U u) {
        return applyThrowing(t, u);
    }

    default Optional<R> toOptional(T t, U u) {
        try {
            return Optional.of(applyThrowing(t, u));
        } catch (Throwable throwable) {
            return Optional.empty();
        }
    }

}
