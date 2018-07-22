package com.idea.tools.view.components;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.markers.Listener;
import com.idea.tools.utils.TableModelBuilder;
import com.idea.tools.view.ConfigurationPanel;
import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Collections;
import java.util.List;

import static com.idea.tools.App.serverService;
import static com.intellij.openapi.actionSystem.ActionToolbarPosition.TOP;
import static com.intellij.ui.ToolbarDecorator.createDecorator;
import static java.awt.BorderLayout.CENTER;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class ServiceConfigTable extends AddEditRemovePanel<ServerDto> implements Listener<ServerDto> {

    private final ConfigurationPanel panel;

    public ServiceConfigTable(List<ServerDto> data, ConfigurationPanel panel) {
        super(tableModel(), data);
        this.panel = panel;
        render();
    }

    private static TableModel<ServerDto> tableModel() {
        return new TableModelBuilder<ServerDto>()
                .withColumn(null, Icon.class, s -> s.getType().getIcon())
                .withColumn("Server", String.class, ServerDto::getName)
                .build();
    }

    @Override
    protected JBTable createTable() {
        JBTable table = super.createTable();
        table.getSelectionModel().addListSelectionListener(event -> {
            if (getTable().getSelectedRow() >= 0) {
                panel.editServer(getData().get(getTable().getSelectedRow()));
            }
        });
        return table;
    }


    @Override
    protected void initPanel() {
        setLayout(new BorderLayout());
        ToolbarDecorator decorator = createDecorator(getTable())
                .setMinimumSize(new Dimension(250, -1))
                .setPreferredSize(new Dimension(250, -1))
                .setAddAction(button -> panel.editServer(null))
                .setRemoveAction(button -> doRemove())
                .setToolbarPosition(TOP);

        JPanel panel = decorator.createPanel();
        add(panel, CENTER);
    }

    private void render() {
        TableColumnModel model = getTable().getColumnModel();

        TableColumn iconColumn = model.getColumn(0);
        iconColumn.setMinWidth(40);
        iconColumn.setMaxWidth(40);

        TableColumn nameColumn = model.getColumn(1);
        nameColumn.setMinWidth(100);
        nameColumn.setMaxWidth(100);

        getTable().setSelectionMode(SINGLE_SELECTION);
    }

    @Nullable
    @Override
    protected ServerDto addItem() {
        return null;
    }

    @Override
    protected boolean removeItem(ServerDto server) {
        return serverService().remove(server);
    }

    @Nullable
    @Override
    protected ServerDto editItem(ServerDto o) {
        return null;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void add(ServerDto server) {
        getData().add(server);
        Collections.sort(getData());
        int index = getData().indexOf(server);
        AbstractTableModel model = (AbstractTableModel) getTable().getModel();
        model.fireTableRowsInserted(index, index);
        getTable().setRowSelectionInterval(index, index);
    }

    @Override
    public void edit(ServerDto item) {
        int index = getData().indexOf(item);
        AbstractTableModel model = (AbstractTableModel) getTable().getModel();
        model.fireTableRowsUpdated(index, index);
        getTable().setRowSelectionInterval(index, index);
    }
}
