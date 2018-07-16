package com.idea.tools.view.components;

import com.idea.tools.utils.GuiUtils;
import com.idea.tools.view.button.ShowHideButton;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.intellij.ui.ToolbarDecorator.createDecorator;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class HeaderViewTable extends AddEditRemovePanel<MutablePair<String, Object>> {

    public HeaderViewTable(List<MutablePair<String, Object>> data) {
        super(new MyTableModel(), data);
        render();
    }

    private void render() {
        getTable().setShowColumns(true);
    }

    @Override
    protected JBTable createTable() {
        JBTable table = super.createTable();
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        return table;
    }

    @Override
    protected void initPanel() {
        setLayout(new BorderLayout());
        ToolbarDecorator decorator = createDecorator(getTable())
                .setPreferredSize(new Dimension(450, 200));

        DefaultActionGroup actions = new DefaultActionGroup("showHideHeadersActionGroup", false);

        final JPanel panel = decorator.createPanel();
        actions.add(ShowHideButton.of(panel));

        add(GuiUtils.toolbar(actions, "showHideHeadersToolbar", false), WEST);
        add(panel, CENTER);
    }

    @Nullable
    @Override
    protected MutablePair<String, Object> addItem() {
        return null;
    }

    @Override
    protected boolean removeItem(MutablePair<String, Object> header) {
        return false;
    }

    @Nullable
    @Override
    protected MutablePair<String, Object> editItem(MutablePair<String, Object> o) {
        return null;
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
                    return header.getValue() != null ? header.getValue() : "";
            }
            return null;
        }
    }
}
