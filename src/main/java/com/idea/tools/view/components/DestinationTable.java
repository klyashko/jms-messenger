package com.idea.tools.view.components;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.utils.TableModelBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

import static com.idea.tools.service.DestinationService.destinationService;
import static com.idea.tools.utils.Utils.sort;
import static com.intellij.openapi.actionSystem.ActionToolbarPosition.LEFT;
import static com.intellij.ui.ToolbarDecorator.createDecorator;
import static java.awt.BorderLayout.CENTER;

public class DestinationTable extends AddEditRemovePanel<DestinationDto> {

    private final Project project;

    private ServerEditDestinationPanel panel;

    public DestinationTable(Project project, ServerEditDestinationPanel panel, List<DestinationDto> data) {
        super(tableModel(), sort(data));
        this.panel = panel;
        this.project = project;
    }

    private static TableModel<DestinationDto> tableModel() {
        return new TableModelBuilder<DestinationDto>()
                .withColumn("Name", DestinationDto::getName)
                .withColumn("Type", dto -> dto.getType().name())
                .build();
    }

    @Override
    protected JBTable createTable() {
        JBTable table = super.createTable();
        table.getSelectionModel().addListSelectionListener(event -> {
            if (getTable().getSelectedRow() >= 0) {
                panel.setOnEdit(getData().get(getTable().getSelectedRow()));
            } else {
                panel.setOnEdit(new DestinationDto());
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
                .setAddAction(button -> panel.setOnEdit(new DestinationDto()))
                .setToolbarPosition(LEFT);

        final JPanel panel = decorator.createPanel();
        add(panel, CENTER);
    }

    @Nullable
    @Override
    protected DestinationDto addItem() {
        return null;
    }

    @Override
    protected boolean removeItem(DestinationDto dto) {
        return destinationService(project).remove(dto);
    }

    @Override
    protected void doRemove() {
        int idx = getTable().getSelectedRow();
        if (idx != -1) {
            removeItem(getData().get(idx));
            clearSelection();
            ((AbstractTableModel) getTable().getModel()).fireTableDataChanged();
        }
    }

    @Nullable
    @Override
    protected DestinationDto editItem(DestinationDto o) {
        return null;
    }

    @Override
    public void setData(List<DestinationDto> data) {
        super.setData(sort(data));
    }

    public void clearSelection() {
        getTable().getSelectionModel().clearSelection();
    }
}
