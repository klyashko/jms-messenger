package com.idea.tools;

import com.idea.tools.service.ServerService;
import com.idea.tools.settings.Settings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ApplicationManager {

    private static Project project;

    private static Settings settings;

    private static ServerService serverService;

    public static Project getProject() {
        return project;
    }

    public static void setProject(Project project) {
        ApplicationManager.project = project;
    }

    public static Settings settings() {
        return getOrCreate(settings, () -> Settings.getOrCreate(project), ApplicationManager::setSettings);
    }

    public static ServerService serverService() {
        return getOrCreate(serverService, fetch(ServerService.class), ApplicationManager::setServerService);
    }

    public static ToolWindowManager toolWindowManager() {
        return ToolWindowManager.getInstance(project);
    }

    private static void setSettings(Settings settings) {
        ApplicationManager.settings = settings;
    }

    private static void setServerService(ServerService serverService) {
        ApplicationManager.serverService = serverService;
    }

    private static <T> T getOrCreate(T value, Supplier<T> creator, Consumer<T> setter) {
        if (value == null) {
            T newValue = creator.get();
            setter.accept(newValue);
            return newValue;
        }
        return value;
    }

    private static <T> Supplier<T> fetch(Class<T> clazz) {
        return () -> ServiceManager.getService(project, clazz);
    }
}
