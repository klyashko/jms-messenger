package com.idea.tools.view.components;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.intellij.ui.table.JBTable;
import lombok.Getter;
import org.apache.commons.lang3.tuple.MutablePair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueueBrowserPanel extends JPanel {

    private QueueDto queue;

    @Getter
    private QueueBrowserTable queueBrowserTable;
    private HeaderViewTable headerTable;

    public QueueBrowserPanel(QueueDto queue) {
        this.queue = queue;
        render();
    }

    private void render() {
        setLayout(new BorderLayout());
        queueBrowserTable = new QueueBrowserTable(queue);
        headerTable = new HeaderViewTable(Collections.emptyList());

        queueBrowserTable.getTable().getSelectionModel().addListSelectionListener(event -> {
            JBTable table = queueBrowserTable.getTable();
            if (table.getSelectedRow() >= 0) {
                MessageDto message = queueBrowserTable.getData().get(table.getSelectedRow());
                List<MutablePair<String, Object>> headers = new ArrayList<>();
                message.getHeaders().forEach((name, value) -> headers.add(MutablePair.of(name, value)));
                headerTable.setData(headers);
            } else {
                headerTable.setData(Collections.emptyList());
            }
        });

        add(queueBrowserTable, BorderLayout.CENTER);
        add(headerTable, BorderLayout.EAST);
    }
}
