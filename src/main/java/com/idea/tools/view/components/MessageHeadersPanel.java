package com.idea.tools.view.components;

import com.idea.tools.dto.MessageDto;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.util.List;
import java.util.Map;

import static com.idea.tools.utils.Utils.toMap;

public class MessageHeadersPanel extends JPanel {

    private List<MutablePair<String, Object>> headers;

    private JPanel rootPanel;
    private JPanel tablePanel;
    private JPanel editPanel;

    private HeaderTable table;
    private HeaderEditPanel headerEditPanel;

    public MessageHeadersPanel(List<MutablePair<String, Object>> headers) {
        this.headers = headers;
        render();
    }

    private void render() {
        table = new HeaderTable(headers, pair -> headerEditPanel.setHeader(pair));

        headerEditPanel = new HeaderEditPanel(pair -> {
            boolean isNew = table.getData().stream().noneMatch(h -> h.getKey().equals(pair.getKey()));
            if (isNew) {
                table.add(pair);
            } else {
                table.getData().replaceAll(h -> {
                    if (h.getKey().equals(pair.getKey())) {
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
        Map<String, Object> headersMap = toMap(table.getData(), Pair::getKey, Pair::getValue);
        dto.setHeaders(headersMap);
    }

}
