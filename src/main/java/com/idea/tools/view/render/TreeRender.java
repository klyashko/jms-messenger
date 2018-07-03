package com.idea.tools.view.render;

import com.idea.tools.dto.Queue;
import com.idea.tools.dto.Server;
import com.intellij.ui.ColoredTreeCellRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

import static com.intellij.ui.SimpleTextAttributes.REGULAR_ITALIC_ATTRIBUTES;

public class TreeRender extends ColoredTreeCellRenderer {
    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//        System.out.println(value);
        if (!(value instanceof DefaultMutableTreeNode)) {
            return;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        Object object = node.getUserObject();

        if (object instanceof List) {
            append("JMS servers");
        } else if (object instanceof Server) {
            Server server = (Server) object;
            append(server.getName());
            setIcon(server.getType().getIcon());
        } else if (object instanceof Queue) {
            Queue queue = (Queue) object;
            append(queue.getName(), REGULAR_ITALIC_ATTRIBUTES);
        }
    }
}
