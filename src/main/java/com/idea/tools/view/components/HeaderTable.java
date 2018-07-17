package com.idea.tools.view.components;

import com.idea.tools.dto.HeaderDto;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static com.intellij.openapi.actionSystem.ActionToolbarPosition.LEFT;
import static com.intellij.ui.ToolbarDecorator.createDecorator;
import static java.awt.BorderLayout.CENTER;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class HeaderTable extends HeaderViewTable {

    private Queue<HeaderDto> newHeaders = new ArrayDeque<>();
    private Consumer<HeaderDto> edit;

    public HeaderTable(List<HeaderDto> data, Consumer<HeaderDto> edit) {
        super(data);
        this.edit = edit;
        render();
    }

    private void render() {
        getTable().setShowColumns(true);
        getTable().setSelectionMode(SINGLE_SELECTION);
    }

    @Override
    protected JBTable createTable() {
        JBTable table = super.createTable();
        table.getSelectionModel().addListSelectionListener(event -> {
            if (getTable().getSelectedRow() >= 0) {
                edit.consume(getData().get(getTable().getSelectedRow()));
            } else {
                edit.consume(new HeaderDto("", null));
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
                .setAddAction(button -> edit.consume(new HeaderDto("", null)))
                .setToolbarPosition(LEFT);

        final JPanel panel = decorator.createPanel();
        add(panel, CENTER);
    }

    @Nullable
    @Override
    protected HeaderDto addItem() {
        return newHeaders.poll();
    }

    @Override
    protected boolean removeItem(HeaderDto header) {
        return showYesNoDialog(String.format("Delete header %s?", header.getName()));
    }

    @Override
    protected boolean allowSelection() {
        return true;
    }

    public void add(HeaderDto header) {
        if (header != null) {
            newHeaders.add(header);
            doAdd();
        }
    }

}
