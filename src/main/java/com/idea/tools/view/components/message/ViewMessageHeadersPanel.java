package com.idea.tools.view.components.message;

import com.idea.tools.dto.HeaderDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.view.components.HeaderViewTable;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

public class ViewMessageHeadersPanel extends JPanel {

	protected List<HeaderDto> headers;

	private JPanel rootPanel;
	private JPanel tablePanel;
	private JPanel editPanel;

	private HeaderViewTable table;

	public ViewMessageHeadersPanel(MessageDto dto) {
		this.headers = dto.getHeaders();
		render(Optional.of(dto));
	}

	public ViewMessageHeadersPanel(List<HeaderDto> headers) {
		this.headers = headers;
		render(empty());
	}

	public void fillMessage(MessageDto dto) {
		dto.setHeaders(table.getData());
	}

	private void render(Optional<MessageDto> message) {
		table = tablePanelContent();
		tablePanel.add(table);
		editPanel.add(editPanelContent(message));
		add(rootPanel);
	}

	protected HeaderViewTable tablePanelContent() {
		return new HeaderViewTable(headers);
	}

	protected JComponent editPanelContent(Optional<MessageDto> messageDto) {
		return new JPanel();
	}

}
