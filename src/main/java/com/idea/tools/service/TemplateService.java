package com.idea.tools.service;

import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.markers.Listener;

import static com.idea.tools.App.settings;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static java.util.UUID.randomUUID;

public class TemplateService extends AbstractPersistedService<TemplateMessageDto> {

    public TemplateService() {
        Listener<TemplateMessageDto> listener = Listener.<TemplateMessageDto>builder()
                .add(settings()::put)
                .edit(settings()::put)
                .remove(settings()::remove)
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
}
