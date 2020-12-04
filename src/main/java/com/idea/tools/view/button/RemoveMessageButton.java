package com.idea.tools.view.button;

import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static com.intellij.util.IconUtil.getRemoveIcon;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.task.RemoveMessageTask;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import java.util.Collections;
import javax.swing.*;

public class RemoveMessageButton extends AnActionButton {

    private static final Icon ICON = getRemoveIcon();
    private final QueueBrowserTable table;

    public RemoveMessageButton(QueueBrowserTable table) {
        super("Reload messages", ICON);
        this.table = table;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (table.getTable().getSelectedRow() >= 0) {
            MessageDto message = table.getData().get(table.getTable().getSelectedRow());
            boolean delete = showYesNoDialog("Delete message from the queue?");
            if (delete) {
                new RemoveMessageTask(table.getProject(), table.getDestination(), Collections.singletonList(message), table).queue();
            }
        } else {
            boolean delete = showYesNoDialog("Delete all messages from the queue?");
            if (delete) {
                new RemoveMessageTask(table.getProject(), table.getDestination(), table.getData(), table).queue();
            }
        }
    }

}
