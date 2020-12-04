package com.idea.tools.view.components.message;

import static com.idea.tools.utils.Utils.noneMatch;
import static java.util.Collections.sort;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.view.components.HeaderTable;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.*;

public class SendMessageHeadersPanel extends ViewMessageHeadersPanel {

	private HeaderTable table;
	private HeaderEditPanel headerEditPanel;

	public SendMessageHeadersPanel(MessageDto messageDto) {
		super(messageDto);
	}

	public SendMessageHeadersPanel() {
		super(new ArrayList<>());
	}

	@Override
	public void fillMessage(MessageDto dto) {
		super.fillMessage(dto);
		headerEditPanel.fillMessage(dto);
	}

	@Override
	protected HeaderTable tablePanelContent() {
		//don't use method reference, headerEditPanel may not be initialized yet
		return table = new HeaderTable(headers, h -> headerEditPanel.setHeader(h));
	}

	@Override
	protected JComponent editPanelContent(Optional<MessageDto> dto) {
		headerEditPanel = new HeaderEditPanel(header -> {
			boolean isNew = noneMatch(table.getData(), h -> h.getName().equals(header.getName()));
			if (isNew) {
				table.add(header);
			} else {
				table.getData().replaceAll(h -> {
					if (h.getName().equals(header.getName())) {
						return header;
					}
					return h;
				});
				sort(table.getData());
				table.repaint();
			}
			table.getTable().clearSelection();
		});
		dto.ifPresent(headerEditPanel::setValues);
		return headerEditPanel;
	}

}
