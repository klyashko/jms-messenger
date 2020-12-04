package com.idea.tools.view.button;

import static com.idea.tools.utils.IconUtils.getCopyIcon;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class CopyButton extends AnActionButton {

    private static final Icon ICON = getCopyIcon();
    private final JTextComponent field;

    public CopyButton(JTextComponent field) {
        super("Copy", "", ICON);
        this.field = field;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        StringSelection stringSelection = new StringSelection(field.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @Override
    public void updateButton(AnActionEvent e) {
    }
}
