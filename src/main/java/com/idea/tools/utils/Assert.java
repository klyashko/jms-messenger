package com.idea.tools.utils;

import java.util.function.Function;

public class Assert {

    public static <T> void equals(T value, T expected, String msg) {
        if (!expected.equals(value)) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notNull(Object o, String msg) {
        notNull(o, msg, IllegalArgumentException::new);
    }

    public static void notNull(Object o, String msg, Function<String, RuntimeException> exception) {
        if (o == null) {
            throw exception.apply(msg);
        }
    }

}
