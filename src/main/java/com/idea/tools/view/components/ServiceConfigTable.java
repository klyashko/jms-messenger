package com.idea.tools.view.components;

import static com.idea.tools.service.ServerService.serverService;
import static com.intellij.openapi.actionSystem.ActionToolbarPosition.TOP;
import static com.intellij.ui.ToolbarDecorator.createDecorator;
import static java.awt.BorderLayout.CENTER;
import static java.util.Collections.sort;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

import com.idea.tools.dto.ServerDto;
import com.idea.tools.markers.Listener;
import com.idea.tools.utils.TableModelBuilder;
import com.idea.tools.view.ConfigurationPanel;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.table.JBTable;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jetbrains.annotations.Nullable;

public class ServiceConfigTable extends AddEditRemovePanel<ServerDto> implements Listener<ServerDto> {

    private final ConfigurationPanel panel;
    private final Project project;

    public ServiceConfigTable(Project project, List<ServerDto> data, ConfigurationPanel panel) {
        super(tableModel(), data);
        this.panel = panel;
        this.project = project;
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
        JPanel panel = createDecorator(getTable())
                .setMinimumSize(new Dimension(170, -1))
                .setPreferredSize(new Dimension(170, -1))
                .setAddAction(button -> this.panel.editServer(null))
                .setRemoveAction(button -> doRemove())
                .setToolbarPosition(TOP)
                .createPanel();

        add(panel, CENTER);
    }

    private void render() {
        TableColumnModel model = getTable().getColumnModel();

        TableColumn iconColumn = model.getColumn(0);
        iconColumn.setMinWidth(40);
        iconColumn.setMaxWidth(40);

        TableColumn nameColumn = model.getColumn(1);
        nameColumn.setMinWidth(100);
        nameColumn.setMaxWidth(130);

        getTable().setSelectionMode(SINGLE_SELECTION);
    }

    @Nullable
    @Override
    protected ServerDto addItem() {
        return null;
    }

    @Override
    protected boolean removeItem(ServerDto server) {
        return serverService(project).remove(server);
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
        sort(getData());
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
