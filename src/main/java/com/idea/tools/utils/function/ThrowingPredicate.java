package com.idea.tools.utils.function;

import lombok.SneakyThrows;

import java.util.function.Predicate;

@FunctionalInterface
public interface ThrowingPredicate<T> extends Predicate<T> {

    boolean testThrowing(T t) throws Throwable;

    @Override
    @SneakyThrows
    default boolean test(T t) {
        return testThrowing(t);
    }
}
