package com.idea.tools.view.button;

import com.idea.tools.dto.Queue;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;

import javax.swing.*;

import static com.idea.tools.App.queueService;
import static com.idea.tools.utils.IconUtils.getRefreshIcon;

public class MessagesReloadButton extends AnActionButton {

    private static final Icon ICON = getRefreshIcon();
    private final QueueBrowserTable table;
    private final Queue queue;

    public MessagesReloadButton(QueueBrowserTable table, Queue queue) {
        super("Reload messages", ICON);
        this.table = table;
        this.queue = queue;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        table.setData(queueService().receive(queue));
    }
}
