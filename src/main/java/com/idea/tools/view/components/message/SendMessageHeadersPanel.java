package com.idea.tools.view.components.message;

import com.idea.tools.dto.HeaderDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.view.components.HeaderTable;

import javax.swing.*;
import java.util.List;

public class SendMessageHeadersPanel extends ViewMessageHeadersPanel {

    private HeaderTable table;
    private HeaderEditPanel headerEditPanel;

    public SendMessageHeadersPanel(List<HeaderDto> headers) {
        super(headers);
    }

    protected JComponent tablePanelContent() {
        table = new HeaderTable(headers, h -> headerEditPanel.setHeader(h));
        return table;
    }

    protected JComponent editPanelContent() {
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
        return headerEditPanel;
    }

    public void fillMessage(MessageDto dto) {
        dto.setHeaders(table.getData());
    }

}
