package com.idea.tools;

import com.idea.tools.view.ConfigurationPanel;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JmsMessengerComponent implements ProjectComponent, SearchableConfigurable {


    private static final String MESSENGER_PLUGIN_NAME = "Messenger Plugin";
    private static final String MESSENGER_COMPONENT_NAME = "Messenger Component";

    private final Project project;

    private ConfigurationPanel configurationPanel;

    public JmsMessengerComponent(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {
        JmsMessengerWindowManager.of(project);
    }

    @Override
    public void projectClosed() {
        try {
            JmsMessengerWindowManager.of(project).unregister();
        } catch (Exception ignored) {}
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
//        return configurationPanel != null && configurationPanel.isModified(jenkinsAppSettings, jenkinsSettings);
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

//
//    public Icon getIcon() {
//        return null;
//    }


    public void reset() {
//        configurationPanel.loadConfigurationData(jenkinsAppSettings, jenkinsSettings);
    }


    public void initComponent() {

    }


    public void disposeComponent() {

    }

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
