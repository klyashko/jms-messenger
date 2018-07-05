package com.idea.tools.markers;

import java.util.function.Consumer;

public interface Listener<T> {

    static <T> Listener<T> simple(Consumer<T> consumer) {
        return Listener.<T>builder()
                .add(consumer)
                .edit(consumer)
                .remove(consumer)
                .build();
    }

    static <T> Builder<T> builder() {
        return new Builder<>();
    }

    void add(T item);

    default void edit(T item) {}

    default void remove(T item) {}

    class Builder<T> {
        private Consumer<T> add = s -> {};
        private Consumer<T> edit = s -> {};
        private Consumer<T> remove = s -> {};

        public Builder<T> add(Consumer<T> add) {
            this.add = add;
            return this;
        }

        public Builder<T> edit(Consumer<T> edit) {
            this.edit = edit;
            return this;
        }

        public Builder<T> remove(Consumer<T> remove) {
            this.remove = remove;
            return this;
        }

        public Listener<T> build() {
            return new Listener<T>() {
                @Override
                public void add(T item) {
                    add.accept(item);
                }

                @Override
                public void edit(T item) {
                    edit.accept(item);
                }

                @Override
                public void remove(T item) {
                    remove.accept(item);
                }
            };
        }
    }

}
