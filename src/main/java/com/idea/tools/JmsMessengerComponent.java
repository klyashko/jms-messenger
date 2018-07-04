/*
 * Copyright (c) 2013 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idea.tools;

import com.idea.tools.settings.Settings;
import com.idea.tools.view.ConfigurationPanel;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.idea.tools.App.settings;


public class JmsMessengerComponent implements ProjectComponent, SearchableConfigurable {


    private static final String MESSENGER_PLUGIN_NAME = "Messenger Plugin";
    private static final String MESSENGER_COMPONENT_NAME = "Messenger Component";

    private final Settings settings;

    private final Project project;

    private ConfigurationPanel configurationPanel;

    public JmsMessengerComponent(Project project) {
        this.project = project;
        App.setProject(project);
        this.settings = settings();
    }

    @Override
    public void projectOpened() {
        JmsMessengerWindowManager.of();
    }

    @Override
    public void projectClosed() {
        JmsMessengerWindowManager.of().unregister();
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
