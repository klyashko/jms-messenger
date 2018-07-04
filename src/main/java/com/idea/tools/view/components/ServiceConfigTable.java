package com.idea.tools.view.components;

import com.idea.tools.dto.Server;
import com.idea.tools.markers.Listener;
import com.idea.tools.view.ConfigurationPanel;
import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static com.idea.tools.App.serverService;

public class ServiceConfigTable extends AddEditRemovePanel<Server> implements Listener<Server> {

    private final ConfigurationPanel panel;
    private Queue<Server> newServers = new ArrayDeque<>();

    public ServiceConfigTable(TableModel<Server> model, List<Server> data, ConfigurationPanel panel) {
        super(model, data);
        this.panel = panel;
        getTable().getColumnModel().getColumn(0).setMaxWidth(40);
        getTable().getColumnModel().getColumn(0).setMinWidth(40);
        getTable().getColumnModel().getColumn(1).setMaxWidth(100);
        getTable().getColumnModel().getColumn(1).setMinWidth(100);
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
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(getTable())
                                                     .setMinimumSize(new Dimension(250, -1))
                                                     .setPreferredSize(new Dimension(250, -1))
                                                     .setAddAction(button -> panel.editServer(null))
                                                     .setRemoveAction(button -> doRemove());

        final JPanel panel = decorator.createPanel();
        add(panel, BorderLayout.CENTER);
        final String label = getLabelText();
        if (label != null) {
            UIUtil.addBorder(panel, IdeBorderFactory.createTitledBorder(label, false));
        }
    }

    @Nullable
    @Override
    protected Server addItem() {
        return newServers.poll();
    }

    @Override
    protected boolean removeItem(Server server) {
        return serverService().remove(server);
    }

    @Nullable
    @Override
    protected Server editItem(Server o) {
        return null;
    }

    @Override
    public void add(Server server) {
        if (server != null) {
            newServers.add(server);
            doAdd();
        }
    }

    public static class MyTableModel extends TableModel<Server> {

        private static final String[] COLUMNS = {null, "Server"};
        private static final Class<?>[] COLUMN_TYPES = {Icon.class, String.class};

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Nullable
        @Override
        public String getColumnName(int columnIndex) {
            return COLUMNS[columnIndex];
        }

        @Override
        public Object getField(Server server, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return server.getType().getIcon();
                case 1:
                    return server.getName();
            }
            return null;
        }

        @Override
        public Class getColumnClass(int columnIndex) {
            return COLUMN_TYPES[columnIndex];
        }
    }
}
