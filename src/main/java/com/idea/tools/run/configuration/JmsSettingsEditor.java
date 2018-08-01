package com.idea.tools.run.configuration;

import com.idea.tools.view.JmsSettingsEditorPanel;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class JmsSettingsEditor extends SettingsEditor<JmsRunConfiguration> {

    private JmsSettingsEditorPanel panel;

    public JmsSettingsEditor() {
        this.panel = new JmsSettingsEditorPanel();
    }

    @Override
    protected void disposeEditor() {
        panel.dispose();
        super.disposeEditor();
    }

    @Override
    protected void resetEditorFrom(@NotNull JmsRunConfiguration s) {
        System.out.println(s);
    }

    @Override
    protected void applyEditorTo(@NotNull JmsRunConfiguration s) {
        System.out.println(s);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return panel;
    }

    public String getTemplateId() {
        return panel.getSelectedTemplateId();
    }

    public void setTemplateId(String id) {
        panel.setSelectedTemplateId(id);
    }

}
