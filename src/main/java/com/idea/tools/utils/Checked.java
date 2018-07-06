package com.idea.tools.utils;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Checked {

    public static <T, R> Function<T, R> function(ThrowingFunction<T, R> throwingFunction) {
        return throwingFunction;
    }

    public static <T, R> Function<T, Optional<R>> functionToOpt(ThrowingFunction<T, R> throwingFunction) {
        return throwingFunction::toOptional;
    }

    public static <T> Consumer<T> consumer(ThrowingConsumer<T> consumer) {
        return consumer;
    }

    public static <T, U, R> BiFunction<T, U, R> biFunction(ThrowingBiFunction<T, U, R> biFunction) {
        return biFunction;
    }

}
