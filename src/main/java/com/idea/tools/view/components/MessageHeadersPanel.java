package com.idea.tools.view.components;

import com.idea.tools.dto.HeaderDto;
import com.idea.tools.dto.MessageDto;

import javax.swing.*;
import java.util.List;

public class MessageHeadersPanel extends JPanel {

    private List<HeaderDto> headers;

    private JPanel rootPanel;
    private JPanel tablePanel;
    private JPanel editPanel;

    private HeaderTable table;
    private HeaderEditPanel headerEditPanel;

    public MessageHeadersPanel(List<HeaderDto> headers) {
        this.headers = headers;
        render();
    }

    private void render() {
        table = new HeaderTable(headers, h -> headerEditPanel.setHeader(h));

        headerEditPanel = new HeaderEditPanel(pair -> {
            boolean isNew = table.getData().stream().noneMatch(h -> h.getName().equals(pair.getName()));
            if (isNew) {
                table.add(pair);
            } else {
                table.getData().replaceAll(h -> {
                    if (h.getName().equals(pair.getName())) {
                        return pair;
                    }
                    return h;
                });
                table.repaint();
            }
            table.getTable().clearSelection();
        });

        tablePanel.add(table);
        editPanel.add(headerEditPanel);
        add(rootPanel);
    }

    public void fillMessage(MessageDto dto) {
        dto.setHeaders(table.getData());
    }

}
