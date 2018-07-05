package com.idea.tools;

import com.idea.tools.service.QueueService;
import com.idea.tools.service.ServerService;
import com.idea.tools.settings.Settings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.ContentFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class App {

    private static Project project;
    private static Settings settings;
    private static ServerService serverService;
    private static QueueService queueService;

    public static Project getProject() {
        return project;
    }

    public static void setProject(Project project) {
        App.project = project;
    }

    public static Settings settings() {
        return getOrCreate(settings, () -> Settings.getOrCreate(project), App::setSettings);
    }

    public static ServerService serverService() {
        return getOrCreate(serverService, fetchSupplier(ServerService.class), App::setServerService);
    }

    public static QueueService queueService() {
        return getOrCreate(queueService, fetchSupplier(QueueService.class), App::setQueueService);
    }

    public static ToolWindowManager toolWindowManager() {
        return ToolWindowManager.getInstance(project);
    }

    public static StartupManager startupManager() {
        return StartupManager.getInstance(project);
    }

    public static void showSettingsDialog(Class<?> clazz) {
        ShowSettingsUtil.getInstance().showSettingsDialog(App.getProject(), clazz);
    }

    public static ContentFactory contentFactory() {
        return ContentFactory.SERVICE.getInstance();
    }

    public static <T> T fetch(Class<T> clazz) {
        return ServiceManager.getService(project, clazz);
    }

    private static void setSettings(Settings settings) {
        App.settings = settings;
    }

    private static void setServerService(ServerService serverService) {
        App.serverService = serverService;
    }

    private static void setQueueService(QueueService queueService) {
        App.queueService = queueService;
    }

    private static <T> T getOrCreate(T value, Supplier<T> creator, Consumer<T> setter) {
        if (value == null) {
            T newValue = creator.get();
            setter.accept(newValue);
            return newValue;
        }
        return value;
    }

    private static <T> Supplier<T> fetchSupplier(Class<T> clazz) {
        return () -> fetch(clazz);
    }
}
