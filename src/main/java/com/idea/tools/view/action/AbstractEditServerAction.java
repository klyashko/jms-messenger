package com.idea.tools.view.action;

import com.idea.tools.dto.Server;
import com.idea.tools.view.ServerEditDialog;
import com.idea.tools.view.ServersBrowseToolPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;
import java.util.Optional;

import static com.intellij.util.IconUtil.getEditIcon;

public abstract class AbstractEditServerAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = getEditIcon();

    AbstractEditServerAction(ServersBrowseToolPanel serversBrowseToolPanel) {
        super("Edit server", "", ICON, serversBrowseToolPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        serversBrowseToolPanel.getSelectedValue(Server.class).ifPresent(server -> ServerEditDialog.showDialog(Optional.of(server)));
    }

    boolean isServerSelected() {
        return isSelected(Server.class);
    }

}
