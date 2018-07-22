package com.idea.tools.utils;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.ui.SimpleToolWindowPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.text.NumberFormat;
import java.util.function.Consumer;

import static com.intellij.openapi.application.ApplicationManager.getApplication;
import static javax.swing.JOptionPane.*;

public class GuiUtils {

    public static boolean showYesNoDialog(String msg) {
        return YES_OPTION == showConfirmDialog(null, msg, "Confirm", YES_NO_OPTION, WARNING_MESSAGE);
    }

    public static JFormattedTextField createNumberInputField() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        return new JFormattedTextField(format);
    }

    public static void installActionGroupInToolBar(ActionGroup actionGroup,
                                                   SimpleToolWindowPanel toolWindowPanel,
                                                   String toolBarName) {

        JComponent actionToolbar = ActionManager.getInstance()
                                                .createActionToolbar(toolBarName, actionGroup, true)
                                                .getComponent();
        toolWindowPanel.setToolbar(actionToolbar);
    }

    public static JComponent toolbar(ActionGroup actions, String name, boolean horizontal) {
        return ActionManager.getInstance().createActionToolbar(name, actions, horizontal).getComponent();
    }

    public static void runInSwingThread(Runnable runnable) {
        Application application = getApplication();
        if (application.isDispatchThread()) {
            runnable.run();
        } else {
            application.invokeLater(runnable);
        }
    }

    public static void showDialog(JFrame dialog, String title) {
        SwingUtilities.invokeLater(() -> {
            dialog.setLocationRelativeTo(null);
            dialog.setTitle(title);
            dialog.pack();
            dialog.setVisible(true);
        });
    }

    public static DocumentListener simpleListener(Consumer<DocumentEvent> consumer) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                consumer.accept(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                consumer.accept(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                consumer.accept(e);
            }
        };
    }

}
