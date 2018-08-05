package com.idea.tools.run.configuration;

import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.markers.Listener;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;
import java.util.Optional;

import static com.idea.tools.App.*;
import static com.intellij.execution.ui.ConsoleViewContentType.*;
import static java.util.concurrent.CompletableFuture.runAsync;

public class JmsRunConfiguration extends RunConfigurationBase {

    private Optional<String> messageId = Optional.empty();

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
        String id = messageId.orElse("");
        if (id.equals("")) {
            throw new RuntimeConfigurationException("Template id is not specified", "Jms messenger");
        }
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
        return (exec, runner) -> {
            ConsoleViewImpl consoleView = new ConsoleViewImpl(getProject(), true);
            MyProcessHandler handler = MyProcessHandler.send(messageId.orElse(""), consoleView);
            return new DefaultExecutionResult(consoleView, handler);
        };
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        this.messageId = Optional.ofNullable(element.getAttributeValue("template"));
        super.readExternal(element);
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        String templateId = messageId.orElse("");
        element.setAttribute("template", templateId);
        super.writeExternal(element);
    }

    public String getMessageId() {
        return messageId.orElse("");
    }

    public void setMessageId(String messageId) {
        this.messageId = Optional.ofNullable(messageId);
    }

    private static class MyProcessHandler extends ProcessHandler {

        private static MyProcessHandler send(String id, ConsoleView consoleView) {
            MyProcessHandler handler = new MyProcessHandler();
            handler.startNotify();
            runAsync(() -> {
                TemplateMessageDto template = settings().getTemplate(id);
                if (template == null) {
                    consoleView.print("Template not found \n", new ConsoleViewContentType("Not found", LOG_WARNING_OUTPUT_KEY));
                    consoleView.print("FAIL", ERROR_OUTPUT);
                    handler.terminate(1);
                    return;
                }
                try {
                    String sendingMsg = String.format("Sending message %s to destination %s\n", template.getName(), template.getDestination().getName());
                    consoleView.print(sendingMsg, NORMAL_OUTPUT);
//                    Sending message
                    jmsService().send(template);
                    String sendMsg = String.format("Message %s is sent to destination %s\n", template.getName(), template.getDestination().getName());
                    consoleView.print(sendMsg, NORMAL_OUTPUT);
                    consoleView.print("SUCCESS", NORMAL_OUTPUT);
                    handler.terminate(0);
                } catch (Exception e) {
                    consoleView.print("Message has not sent\n", ERROR_OUTPUT);
                    consoleView.print("Reason:\n", ERROR_OUTPUT);
                    consoleView.print(e.getMessage() + "\n", ERROR_OUTPUT);
                    consoleView.print("FAIL", ERROR_OUTPUT);
                    handler.terminate(1);
                }
            });

            return handler;
        }

        @Override
        protected void destroyProcessImpl() {

        }

        @Override
        protected void detachProcessImpl() {

        }

        @Override
        public boolean detachIsDefault() {
            return false;
        }

        @Nullable
        @Override
        public OutputStream getProcessInput() {
            return null;
        }

        public void terminate(int code) {
            this.notifyProcessTerminated(code);
        }
    }
}
