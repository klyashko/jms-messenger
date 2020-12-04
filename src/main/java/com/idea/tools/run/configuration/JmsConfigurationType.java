package com.idea.tools.run.configuration;

import static com.idea.tools.utils.IconUtils.getJmsIcon;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class JmsConfigurationType implements ConfigurationType {
    @Override
    public String getDisplayName() {
        return "Jms";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Jms messenger";
    }

    @Override
    public Icon getIcon() {
        return getJmsIcon();
    }

    @NotNull
    @Override
    public String getId() {
        return "Jms Messenger Configuration";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new JmsConfigurationFactory(this)};
    }
}
