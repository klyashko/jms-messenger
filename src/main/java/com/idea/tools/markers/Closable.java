package com.idea.tools.markers;

@FunctionalInterface
public interface Closable extends AutoCloseable {

    void close();

}
