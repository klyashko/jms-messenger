package com.idea.tools.view.components;

import com.idea.tools.dto.HeaderDto;
import com.idea.tools.utils.GuiUtils;
import com.idea.tools.utils.TableModelBuilder;
import com.idea.tools.view.button.ShowHideButton;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.intellij.ui.ToolbarDecorator.createDecorator;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class HeaderViewTable extends AddEditRemovePanel<HeaderDto> {

    public HeaderViewTable(List<HeaderDto> data) {
        super(tableModel(), data);
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
