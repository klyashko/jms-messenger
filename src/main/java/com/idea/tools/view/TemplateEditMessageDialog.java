package com.idea.tools.view;

import com.idea.tools.dto.TemplateMessageDto;
import com.idea.tools.utils.GuiUtils;

import javax.swing.*;

import static com.idea.tools.App.templateService;
import static com.idea.tools.utils.GuiUtils.simpleListener;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TemplateEditMessageDialog extends SendMessageDialog {

    private TemplateMessageDto message;

    protected TemplateEditMessageDialog(TemplateMessageDto message) {
        super(message);
        this.message = message;
        render();
    }

    public static void showDialog(TemplateMessageDto message) {
        GuiUtils.showDialog(new TemplateEditMessageDialog(message), "Template settings");
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
            templateService().saveOrUpdate(message);
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
