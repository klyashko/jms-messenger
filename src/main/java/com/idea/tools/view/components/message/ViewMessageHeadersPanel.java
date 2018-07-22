package com.idea.tools.view.components.message;

import com.idea.tools.dto.HeaderDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.view.components.HeaderViewTable;

import javax.swing.*;
import java.util.List;

public class ViewMessageHeadersPanel extends JPanel {

    protected List<HeaderDto> headers;

    private JPanel rootPanel;
    private JPanel tablePanel;
    private JPanel editPanel;

    private HeaderViewTable table;


    public ViewMessageHeadersPanel(List<HeaderDto> headers) {
        this.headers = headers;
        render();
    }

    public void fillMessage(MessageDto dto) {
        dto.setHeaders(table.getData());
    }

    private void render() {
        table = tablePanelContent();
        tablePanel.add(table);
        editPanel.add(editPanelContent());
        add(rootPanel);
    }

    protected HeaderViewTable tablePanelContent() {
        return new HeaderViewTable(headers);
    }

    protected JComponent editPanelContent() {
        return new JPanel();
    }

}
