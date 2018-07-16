package com.idea.tools.view.button;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;

import javax.swing.*;

import static com.idea.tools.utils.IconUtils.getLeftShiftIcon;
import static com.idea.tools.utils.IconUtils.getRightShiftIcon;

public class ShowHideButton extends AnActionButton {

    private static final Icon LEFT_SHIFT_ICON = getLeftShiftIcon();
    private static final Icon RIGHT_SHIFT_ICON = getRightShiftIcon();

    private static final String SHOW_MSG = "Show custom properties";
    private static final String HIDE_MSG = "Hide custom properties";

    private final JPanel panel;
    private boolean state;

    private ShowHideButton(JPanel panel, Icon icon, String text) {
        super(text, icon);
        this.panel = panel;
        this.state = panel.isVisible();
    }

    public static ShowHideButton of(JPanel panel) {
        if (panel.isVisible()) {
            return new ShowHideButton(panel, RIGHT_SHIFT_ICON, HIDE_MSG);
        } else {
            return new ShowHideButton(panel, LEFT_SHIFT_ICON, SHOW_MSG);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        state = !state;
        panel.setVisible(state);
    }

    @Override
    public void updateButton(AnActionEvent e) {
        if (state) {
            e.getPresentation().setText(HIDE_MSG);
            e.getPresentation().setIcon(RIGHT_SHIFT_ICON);
        } else {
            e.getPresentation().setText(SHOW_MSG);
            e.getPresentation().setIcon(LEFT_SHIFT_ICON);
        }
    }
}
