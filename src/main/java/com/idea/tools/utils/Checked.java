package com.idea.tools.utils;

import java.util.Optional;
import java.util.function.Function;

public class Checked {

    public static <T, R> Function<T, R> function(ThrowingFunction<T, R> throwingFunction) {
        return throwingFunction;
    }

    public static <T, R> Function<T, Optional<R>> functionToOpt(ThrowingFunction<T, R> throwingFunction) {
        return throwingFunction::toOptional;
    }

}
