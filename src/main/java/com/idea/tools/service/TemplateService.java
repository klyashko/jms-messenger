package com.idea.tools.service;

import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.markers.Listener;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import static com.idea.tools.settings.Settings.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static java.util.UUID.randomUUID;

public class TemplateService extends AbstractPersistedService<TemplateMessageDto> {

    public TemplateService(Project project) {
        Listener<TemplateMessageDto> listener = Listener.<TemplateMessageDto>builder()
                .add(settings(project)::put)
                .edit(settings(project)::put)
                .remove(settings(project)::remove)
                .build();

        addListener(listener);
    }

    @Override
    protected void persist(TemplateMessageDto template) {
        template.setId(randomUUID().toString());
    }

    @Override
    protected boolean isNew(TemplateMessageDto template) {
        return template.getId() == null;
    }

    @Override
    protected boolean confirmRemove(TemplateMessageDto template) {
        return showYesNoDialog(String.format("Do you want to delete template %s", template.getName()));
    }

    public static TemplateService templateService(Project project) {
        return ServiceManager.getService(project, TemplateService.class);
    }
}
