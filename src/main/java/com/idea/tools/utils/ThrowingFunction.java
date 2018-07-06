package com.idea.tools.utils;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {

    R applyThrowing(T value) throws Throwable;

    @Override
    default R apply(T t) {
        try {
            return applyThrowing(t);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    default Optional<R> toOptional(T t) {
        try {
            return Optional.of(applyThrowing(t));
        } catch (Throwable throwable) {
            return Optional.empty();
        }
    }

}
