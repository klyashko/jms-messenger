package com.idea.tools.run.configuration;

import com.idea.tools.view.JmsSettingsEditorPanel;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class JmsSettingsEditor extends SettingsEditor<JmsRunConfiguration> {

    private final JmsSettingsEditorPanel panel;

    public JmsSettingsEditor(Project project, String templateId) {
        this.panel = new JmsSettingsEditorPanel(project, templateId);
    }

    public JmsSettingsEditor(Project project) {
        this.panel = new JmsSettingsEditorPanel(project);
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
