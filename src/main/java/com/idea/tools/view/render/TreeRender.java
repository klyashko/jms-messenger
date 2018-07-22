package com.idea.tools.view.render;

import com.idea.tools.dto.QueueDto;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.dto.TemplateMessageDto;
import com.intellij.ui.ColoredTreeCellRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

import static com.idea.tools.utils.IconUtils.getTemplateIcon;
import static com.intellij.ui.SimpleTextAttributes.REGULAR_ITALIC_ATTRIBUTES;

public class TreeRender extends ColoredTreeCellRenderer {
    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (!(value instanceof DefaultMutableTreeNode)) {
            return;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        Object object = node.getUserObject();

        if (object instanceof List) {
            append("JMS servers");
        } else if (object instanceof ServerDto) {
            ServerDto server = (ServerDto) object;
            append(server.getName());
            setIcon(server.getType().getIcon());
        } else if (object instanceof QueueDto) {
            QueueDto queue = (QueueDto) object;
            append(queue.getName(), REGULAR_ITALIC_ATTRIBUTES);
        } else if (object instanceof TemplateMessageDto) {
            TemplateMessageDto message = (TemplateMessageDto) object;
            append(message.getName());
            setIcon(getTemplateIcon());
        }
    }
}
