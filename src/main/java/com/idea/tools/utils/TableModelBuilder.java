package com.idea.tools.utils;

import com.intellij.ui.AddEditRemovePanel.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

public class TableModelBuilder<T> {

    private final List<Column<T>> columns = new ArrayList<>();

    public TableModelBuilder<T> withColumn(String name, Function<T, Object> mapper) {
        columns.add(new Column<>(name, String.class, mapper));
        return this;
    }

    public TableModelBuilder<T> withColumn(String name, Class<?> type, Function<T, Object> mapper) {
        columns.add(new Column<>(name, type, mapper));
        return this;
    }

    public TableModel<T> build() {
        return new TableModel<T>() {
            @Override
            public int getColumnCount() {
                return columns.size();
            }

            @Nullable
            @Override
            public String getColumnName(int columnIndex) {
                return columns.get(columnIndex).name;
            }

            @Override
            public Object getField(T o, int columnIndex) {
                return columns.get(columnIndex).mapper.apply(o);
            }

            @Override
            public Class getColumnClass(int columnIndex) {
                return columns.get(columnIndex).type;
            }
        };
    }

    @RequiredArgsConstructor
    private static class Column<T> {
        private final String name;
        private final Class<?> type;
        private final Function<T, Object> mapper;
    }

}
