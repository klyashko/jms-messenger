package com.idea.tools.view.components.message;

import com.idea.tools.dto.HeaderDto;
import com.idea.tools.view.components.HeaderViewTable;

import javax.swing.*;
import java.util.List;

public class ViewMessageHeadersPanel extends JPanel {

    protected List<HeaderDto> headers;

    private JPanel rootPanel;
    private JPanel tablePanel;
    private JPanel editPanel;


    public ViewMessageHeadersPanel(List<HeaderDto> headers) {
        this.headers = headers;
        render();
    }

    private void render() {
        tablePanel.add(tablePanelContent());
        editPanel.add(editPanelContent());
        add(rootPanel);
    }

    protected JComponent tablePanelContent() {
        return new HeaderViewTable(headers);
    }

    protected JComponent editPanelContent() {
        return new JPanel();
    }

}
