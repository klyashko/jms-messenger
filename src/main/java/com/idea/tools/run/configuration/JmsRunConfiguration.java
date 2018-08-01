package com.idea.tools.run.configuration;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JmsRunConfiguration extends RunConfigurationBase {

    private JmsSettingsEditor editor;

    protected JmsRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
        editor = new JmsSettingsEditor();
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return editor;
    }

    @Override
    public void checkConfiguration() {
        System.out.println();
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
        return null;
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        String templateId = element.getAttributeValue("template");
        editor.setTemplateId(templateId);
        super.readExternal(element);
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        String templateId = editor.getTemplateId();
        if (templateId != null) {
            element.setAttribute("template", templateId);
        } else {
            element.setAttribute("template", "");
        }
        super.writeExternal(element);
    }
}
