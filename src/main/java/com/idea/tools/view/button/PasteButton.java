package com.idea.tools.view.button;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.AnActionButton;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Optional;

import static com.idea.tools.utils.IconUtils.getPasteIcon;

public class PasteButton extends AnActionButton {

    private static final Logger LOGGER = Logger.getInstance(PasteButton.class);
    private static final Icon ICON = getPasteIcon();
    private final JTextComponent field;

    public PasteButton(JTextComponent field, boolean enabled) {
        super("Copy", "", ICON);
        this.field = field;
        setEnabled(enabled);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Optional.ofNullable(clipboard.getContents(this))
                .ifPresent(t -> {
                    try {
                        field.setText((String) t.getTransferData(DataFlavor.stringFlavor));
                    } catch (UnsupportedFlavorException | IOException e) {
                        LOGGER.error("An exception has been thrown during accessing clipboard", e);
                    }
                });
    }

    @Override
    public void updateButton(AnActionEvent e) {
    }
}
