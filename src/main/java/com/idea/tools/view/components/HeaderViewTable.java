package com.idea.tools.view.components;

import com.idea.tools.dto.HeaderDto;
import com.idea.tools.utils.TableModelBuilder;
import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

import static com.idea.tools.utils.Utils.sort;
import static com.intellij.ui.ToolbarDecorator.createDecorator;
import static java.awt.BorderLayout.CENTER;

public class HeaderViewTable extends AddEditRemovePanel<HeaderDto> {

    public HeaderViewTable(List<HeaderDto> data) {
        super(tableModel(), sort(data));
        render();
    }

    private void render() {
        getTable().setShowColumns(true);
    }

    @Override
    protected JBTable createTable() {
        JBTable table = super.createTable();
        if (!allowSelection()) {
            table.setRowSelectionAllowed(false);
            table.setColumnSelectionAllowed(false);
        }
        return table;
    }

    @Override
    protected void initPanel() {
        setLayout(new BorderLayout());
        ToolbarDecorator decorator = createDecorator(getTable())
                .setPreferredSize(new Dimension(450, 200));

        add(decorator.createPanel(), CENTER);
    }

    protected boolean allowSelection() {
        return false;
    }

    private static TableModel<HeaderDto> tableModel() {
        return new TableModelBuilder<HeaderDto>()
                .withColumn("Name", HeaderDto::getName)
                .withColumn("Value", HeaderDto::getValue)
                .build();
    }

    @Nullable
    @Override
    protected HeaderDto addItem() {
        return null;
    }

    @Override
    protected boolean removeItem(HeaderDto header) {
        return false;
    }

    @Nullable
    @Override
    protected HeaderDto editItem(HeaderDto o) {
        return null;
    }

}
