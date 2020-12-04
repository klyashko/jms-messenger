package com.idea.tools.view.button;

import static com.idea.tools.utils.IconUtils.getReadMessageIcon;

import com.idea.tools.view.ViewMessageDialog;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import javax.swing.*;

public class OpenMessageButton extends AnActionButton {

    private static final Icon ICON = getReadMessageIcon();
    private final QueueBrowserTable table;

    public OpenMessageButton(QueueBrowserTable table) {
        super("Open message", ICON);
        this.table = table;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (table.getTable().getSelectedRow() >= 0) {
            ViewMessageDialog.showDialog(table.getProject(), table.getData().get(table.getTable().getSelectedRow()));
        }
    }

    @Override
    public void updateButton(AnActionEvent e) {
        e.getPresentation().setEnabled(table.getTable().getSelectedRow() >= 0);
    }
}
