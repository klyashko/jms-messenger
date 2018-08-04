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

import java.util.Optional;

public class JmsRunConfiguration extends RunConfigurationBase {

    private Optional<String> messageId;

    protected JmsRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return messageId.map(JmsSettingsEditor::new).orElseGet(JmsSettingsEditor::new);
    }

    @Override
    public void checkConfiguration() {
//        System.out.println(getName() + " checkConfiguration");
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
        return null;
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        this.messageId = Optional.ofNullable(element.getAttributeValue("template"));
//        System.out.println(getName() + " id <" + messageId + "> is loaded");
        super.readExternal(element);
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        String templateId = messageId.orElse("");
//        System.out.println(getName() + " storing id <" + templateId + ">");
        element.setAttribute("template", templateId);
        super.writeExternal(element);
    }

    public String getMessageId() {
        return messageId.orElse("");
    }

    public void setMessageId(String messageId) {
        this.messageId = Optional.ofNullable(messageId);
    }
}
