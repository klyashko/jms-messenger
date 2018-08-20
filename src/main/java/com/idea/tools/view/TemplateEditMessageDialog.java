package com.idea.tools.view;

import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.utils.GuiUtils;
import com.intellij.openapi.project.Project;

import javax.swing.*;

import static com.idea.tools.service.TemplateService.templateService;
import static com.idea.tools.utils.GuiUtils.simpleListener;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TemplateEditMessageDialog extends SendMessageDialog {

    private final Project project;
    private TemplateMessageDto message;

    private TemplateEditMessageDialog(Project project, TemplateMessageDto message) {
        super(project, message);
        this.message = message;
        this.project = project;
        render();
    }

    public static void showDialog(Project project, TemplateMessageDto message) {
        GuiUtils.showDialog(new TemplateEditMessageDialog(project, message), "Template settings");
    }

    private void render() {
        templateNameField.setText(message.getName());
    }

    @Override
    protected void actionButton(JButton actionButton) {
        actionButton.setVisible(true);
        actionButton.setText("Save");
        actionButton.addActionListener(event -> {
            fillMessage(message);
            templateService(project).saveOrUpdate(message);
            dispose();
        });
    }

    @Override
    protected void closeAfterSendCheckBox(JCheckBox closeAfterSendCheckBox) {
        closeAfterSendCheckBox.setVisible(false);
    }

    @Override
    protected void saveAsTemplateButton(JButton saveAsTemplateButton) {
        saveAsTemplateButton.setVisible(false);
    }

    @Override
    protected void templateNameField(JTextField templateNameField) {
        templateNameField.getDocument().addDocumentListener(
                simpleListener(event -> actionButton.setEnabled(isNotBlank(templateNameField.getText())))
        );
    }

}
