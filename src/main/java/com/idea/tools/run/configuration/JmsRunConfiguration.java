package com.idea.tools.run.configuration;

import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.markers.Listener;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.idea.tools.App.templateService;

public class JmsRunConfiguration extends RunConfigurationBase {

    private Optional<String> messageId;

    protected JmsRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
        Listener<TemplateMessageDto> listener = Listener.<TemplateMessageDto>builder()
                .remove(t -> messageId = messageId.filter(id -> !id.equals(t.getId())))
                .build();

        templateService().addListener(listener);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return messageId.map(JmsSettingsEditor::new).orElseGet(JmsSettingsEditor::new);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        System.out.println(getName() + " checkConfiguration");
        String id = messageId.orElse("");
        if (id.equals("")) {
            throw new RuntimeConfigurationException("Template id is not specified", "Jms messenger");
        }
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
