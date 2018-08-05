package com.idea.tools.run.configuration;

import com.idea.tools.view.JmsSettingsEditorPanel;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class JmsSettingsEditor extends SettingsEditor<JmsRunConfiguration> {

    private JmsSettingsEditorPanel panel;

    public JmsSettingsEditor(String templateId) {
        this.panel = new JmsSettingsEditorPanel(templateId);
    }

    public JmsSettingsEditor() {
        this.panel = new JmsSettingsEditorPanel();
    }

    @Override
    protected void disposeEditor() {
        super.disposeEditor();
    }

    @Override
    protected void resetEditorFrom(@NotNull JmsRunConfiguration s) {
        panel.setSelectedTemplateId(s.getMessageId());
    }

    @Override
    protected void applyEditorTo(@NotNull JmsRunConfiguration s) {
        s.setMessageId(panel.getSelectedTemplateId());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return panel;
    }

}
