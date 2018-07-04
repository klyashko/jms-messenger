package com.idea.tools.utils;

import lombok.extern.apachecommons.CommonsLog;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@CommonsLog
public class Utils {

    public static <T, R> List<R> map(List<T> source, Function<T, R> function) {
        return source.stream().map(function).collect(Collectors.toList());
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static Integer toInteger(String value) {
        if (value != null) {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException e) {
                log.error("An exception during an integer parsing", e);
            }
        }
        return null;
    }

    public static String toString(Integer value) {
        return value != null ? Integer.toString(value) : null;
    }

    public static <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static <T, R> Function<T, R> cast(Class<R> clazz) {
        return value -> clazz.isInstance(value) ? clazz.cast(value) : null;
    }

}
