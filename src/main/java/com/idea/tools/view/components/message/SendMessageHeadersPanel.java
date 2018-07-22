package com.idea.tools.view.components.message;

import com.idea.tools.dto.HeaderDto;
import com.idea.tools.view.components.HeaderTable;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class SendMessageHeadersPanel extends ViewMessageHeadersPanel {

    private HeaderTable table;
    private HeaderEditPanel headerEditPanel;

    public SendMessageHeadersPanel(List<HeaderDto> headers) {
        super(headers);
    }

    @Override
    protected HeaderTable tablePanelContent() {
        table = new HeaderTable(headers, h -> headerEditPanel.setHeader(h));
        return table;
    }

    @Override
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
                Collections.sort(table.getData());
                table.repaint();
            }
            table.getTable().clearSelection();
        });
        return headerEditPanel;
    }

}
