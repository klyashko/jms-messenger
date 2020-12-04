package com.idea.tools;

import com.idea.tools.view.ConfigurationPanel;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import javax.swing.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JmsMessengerComponent implements SearchableConfigurable {

    private static final String MESSENGER_PLUGIN_NAME = "Messenger Plugin";
    private static final String MESSENGER_COMPONENT_NAME = "Messenger Component";

    private final Project project;

    private ConfigurationPanel configurationPanel;

    public JmsMessengerComponent(Project project) {
        this.project = project;
    }

    @Override
    public JComponent createComponent() {
        if (configurationPanel == null) {
            configurationPanel = new ConfigurationPanel(project);
        }
        return configurationPanel.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void disposeUIResources() {
        configurationPanel.dispose();
        configurationPanel = null;
    }

    @Override
    public String getHelpTopic() {
        return "preferences.jms.messenger";
    }

    @Override
    public void apply() {
        System.out.println("Apply is called");
    }

    @NotNull
    public String getComponentName() {
        return MESSENGER_COMPONENT_NAME;
    }

    @Nls
    public String getDisplayName() {
        return MESSENGER_PLUGIN_NAME;
    }


    public void reset() { }

    @NotNull
    @Override
    public String getId() {
        return "preferences.JmsMessenger";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String option) {
        return null;
    }
}
