package com.idea.tools.view.components;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.utils.GuiUtils;
import com.idea.tools.view.button.ShowHideButton;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.ui.table.JBTable;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class QueueBrowserPanel extends JPanel {

    private DestinationDto destination;

    @Getter
    private QueueBrowserTable queueBrowserTable;
    private HeaderViewTable headerTable;

    public QueueBrowserPanel(DestinationDto queue) {
        this.destination = queue;
        render();
    }

    private void render() {
        setLayout(new BorderLayout());
        queueBrowserTable = new QueueBrowserTable(destination);
        headerTable = new HeaderViewTable(Collections.emptyList());

        queueBrowserTable.getTable().getSelectionModel().addListSelectionListener(event -> {
            JBTable table = queueBrowserTable.getTable();
            if (table.getSelectedRow() >= 0) {
                MessageDto message = queueBrowserTable.getData().get(table.getSelectedRow());
                headerTable.setData(message.getHeaders());
            } else {
                headerTable.setData(Collections.emptyList());
            }
        });

        DefaultActionGroup actions = new DefaultActionGroup("showHideHeadersActionGroup", false);
        actions.add(ShowHideButton.of(headerTable));

        JPanel headersPanel = new JPanel();
        headersPanel.setLayout(new BorderLayout());
        headersPanel.add(GuiUtils.toolbar(actions, "showHideHeadersToolbar", false), WEST);
        headersPanel.add(headerTable, CENTER);

        add(queueBrowserTable, BorderLayout.CENTER);
        add(headersPanel, BorderLayout.EAST);
    }
}
