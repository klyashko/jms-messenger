package com.idea.tools.view.components;

import com.idea.tools.dto.MessageEntity;
import com.idea.tools.dto.Queue;
import com.idea.tools.view.button.MessagesReloadButton;
import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static com.idea.tools.App.queueService;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static com.intellij.openapi.actionSystem.ActionToolbarPosition.TOP;
import static com.intellij.ui.IdeBorderFactory.createTitledBorder;
import static com.intellij.ui.ToolbarDecorator.createDecorator;
import static com.intellij.util.ui.UIUtil.addBorder;
import static java.awt.BorderLayout.CENTER;

public class QueueBrowserTable extends AddEditRemovePanel<MessageEntity> {

    private Queue queue;

    public QueueBrowserTable(Queue queue) {
        super(new MyTableModel(), queueService().receive(queue));
        this.queue = queue;
    }

    @Override
    protected void initPanel() {
        setLayout(new BorderLayout());
        ToolbarDecorator decorator = createDecorator(getTable())
                .setMoveDownAction(button -> doDown())
                .setMoveUpAction(button -> doUp())
                .setRemoveAction(button -> doRemove())
                .addExtraAction(new MessagesReloadButton(this, queue))
                .setToolbarPosition(TOP);

        final JPanel panel = decorator.createPanel();
        add(panel, CENTER);
        final String label = getLabelText();
        if (label != null) {
            addBorder(panel, createTitledBorder(label, false));
        }
    }

    @Nullable
    @Override
    protected MessageEntity addItem() {
        return null;
    }

    @Override
    protected boolean removeItem(MessageEntity msg) {
        boolean delete = showYesNoDialog("Delete message from the queue?");
        return delete && queueService().removeFromQueue(msg, queue);
    }

    @Nullable
    @Override
    protected MessageEntity editItem(MessageEntity o) {
        return null;
    }

    public static class MyTableModel extends TableModel<MessageEntity> {

        private static final String[] COLUMNS = {"Payload"};
        private static final Class<?>[] COLUMN_TYPES = {String.class};

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Nullable
        @Override
        public String getColumnName(int columnIndex) {
            return COLUMNS[columnIndex];
        }

        @Override
        public Object getField(MessageEntity messageEntity, int columnIndex) {
            return messageEntity.getPayload();
        }

        @Override
        public Class getColumnClass(int columnIndex) {
            return COLUMN_TYPES[columnIndex];
        }
    }
}
