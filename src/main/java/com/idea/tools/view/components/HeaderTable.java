package com.idea.tools.view.components;

import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.Consumer;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static com.intellij.openapi.actionSystem.ActionToolbarPosition.LEFT;
import static com.intellij.ui.IdeBorderFactory.createTitledBorder;
import static com.intellij.ui.ToolbarDecorator.createDecorator;
import static com.intellij.util.ui.UIUtil.addBorder;
import static java.awt.BorderLayout.CENTER;

public class HeaderTable extends AddEditRemovePanel<MutablePair<String, Object>> {

    private Queue<MutablePair<String, Object>> newHeaders = new ArrayDeque<>();
    private Consumer<MutablePair<String, Object>> edit;

    public HeaderTable(List<MutablePair<String, Object>> data, Consumer<MutablePair<String, Object>> edit) {
        super(new MyTableModel(), data);
        this.edit = edit;
        render();
    }

    private void render() {
    }

    @Override
    protected JBTable createTable() {
        JBTable table = super.createTable();
        table.getSelectionModel().addListSelectionListener(event -> {
            if (getTable().getSelectedRow() >= 0) {
                edit.consume(getData().get(getTable().getSelectedRow()));
            } else {
                edit.consume(MutablePair.of("", null));
            }
        });
        return table;
    }

    @Override
    protected void initPanel() {
        setLayout(new BorderLayout());
        ToolbarDecorator decorator = createDecorator(getTable())
                .setMoveDownAction(button -> doDown())
                .setMoveUpAction(button -> doUp())
                .setRemoveAction(button -> doRemove())
                .setPreferredSize(new Dimension(450, 200))
                .setAddAction(button -> edit.consume(MutablePair.of("", null)))
                .setToolbarPosition(LEFT);

        final JPanel panel = decorator.createPanel();
        add(panel, CENTER);
        final String label = getLabelText();
        if (label != null) {
            addBorder(panel, createTitledBorder(label, false));
        }
    }

    @Nullable
    @Override
    protected MutablePair<String, Object> addItem() {
        return newHeaders.poll();
    }

    @Override
    protected boolean removeItem(MutablePair<String, Object> header) {
        return showYesNoDialog(String.format("Delete header %s?", header.getKey()));
    }

    @Nullable
    @Override
    protected MutablePair<String, Object> editItem(MutablePair<String, Object> o) {
        return null;
    }

    public void add(MutablePair<String, Object> header) {
        if (header != null) {
            newHeaders.add(header);
            doAdd();
        }
    }


    public static class MyTableModel extends TableModel<MutablePair<String, Object>> {

        private static final String[] COLUMNS = {"Name", "Value"};

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Nullable
        @Override
        public String getColumnName(int columnIndex) {
            return COLUMNS[columnIndex];
        }

        @Override
        public Object getField(MutablePair<String, Object> header, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return header.getKey();
                case 1:
                    return Objects.toString(header.getValue());
            }
            return null;
        }
    }
}
