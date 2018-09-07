package com.idea.tools.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class Assert {

    public static <T> void equals(T value, T expected, String msg, Object... args) {
        if (!expected.equals(value)) {
            throw new IllegalArgumentException(String.format(msg, args));
        }
    }

    public static void notNull(Object o, String msg) {
        notNull(o, msg, IllegalArgumentException::new);
    }

    public static void notBlank(String str, String msg) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notNull(Object o, String msg, Function<String, RuntimeException> exception) {
        if (o == null) {
            throw exception.apply(msg);
        }
    }

}
