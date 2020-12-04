package com.idea.tools.view.button;

import static com.idea.tools.utils.IconUtils.getRefreshIcon;

import com.idea.tools.task.LoadMessagesTask;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import javax.swing.*;

public class MessagesReloadButton extends AnActionButton {

    private static final Icon ICON = getRefreshIcon();
    private final QueueBrowserTable table;

    public MessagesReloadButton(QueueBrowserTable table) {
        super("Reload messages", ICON);
        this.table = table;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        new LoadMessagesTask(table.getProject(), table).queue();
    }
}
