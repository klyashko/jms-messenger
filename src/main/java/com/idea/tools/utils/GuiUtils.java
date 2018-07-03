package com.idea.tools.utils;

import com.idea.tools.markers.Closable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

import static com.intellij.openapi.application.ApplicationManager.getApplication;

public class GuiUtils {

    private static final String ICON_FOLDER = "/images/";

    public static Icon icon(String iconFilename) {
        return IconLoader.findIcon(ICON_FOLDER + iconFilename);
    }


    public static Icon icon(String parentPath, String iconFilename) {
        return IconLoader.findIcon(parentPath + iconFilename);
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

    public static void cleanUp(Object obj) {
        if (obj instanceof Closable) {
            ((Closable) obj).close();
        }
    }

}
