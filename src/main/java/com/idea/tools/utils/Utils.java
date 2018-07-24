package com.idea.tools.utils;

import lombok.extern.apachecommons.CommonsLog;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@CommonsLog
public class Utils {

    public static <T, R> List<R> toList(Collection<T> source, Function<T, R> function) {
        return source.stream().map(function).collect(Collectors.toList());
    }

    public static <S, K, V> Map<K, V> toMap(Collection<S> source, Function<S, K> key, Function<S, V> value) {
        return source.stream().collect(Collectors.toMap(key, value));
    }

    public static <T> Map<Boolean, List<T>> partitioningBy(Collection<T> source, Predicate<T> predicate) {
        return source.stream().collect(Collectors.partitioningBy(predicate));
    }

    public static <T, K> Map<K, List<T>> groupingBy(Collection<T> source, Function<T, K> classifier) {
        return source.stream().collect(Collectors.groupingBy(classifier));
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

    public static <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static <T, R> Function<T, R> cast(Class<R> clazz) {
        return value -> clazz.isInstance(value) ? clazz.cast(value) : null;
    }

    public static <T extends Comparable<? super T>> List<T> sort(List<T> source) {
        Collections.sort(source);
        return source;
    }

    public static URI uri(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(String.format("Url is malformed <%s>", url));
        }
    }

}
