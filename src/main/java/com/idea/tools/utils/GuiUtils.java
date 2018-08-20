package com.idea.tools.utils;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.text.NumberFormat;
import java.util.function.Consumer;
import java.util.function.Function;

import static javax.swing.JOptionPane.*;

public class GuiUtils {

    public static ToolWindowManager toolWindowManager(Project project) {
        return ToolWindowManager.getInstance(project);
    }

    public static StartupManager startupManager(Project project) {
        return StartupManager.getInstance(project);
    }

    public static ContentFactory contentFactory() {
        return ContentFactory.SERVICE.getInstance();
    }

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

    public static JComponent toolbar(ActionGroup actions, String place, boolean horizontal) {
        return ActionManager.getInstance().createActionToolbar(place, actions, horizontal).getComponent();
    }

    public static <T> JLabel label(T value, Function<T, String> function) {
        if (value != null) {
            return new JLabel(function.apply(value));
        }
        return new JLabel();
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
