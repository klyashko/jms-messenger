package com.idea.tools.view;

import com.idea.tools.dto.Queue;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;

import javax.swing.*;
import java.awt.*;

import static com.idea.tools.App.fetch;
import static com.idea.tools.App.getProject;
import static com.idea.tools.utils.IconUtils.getBrowseIcon;

public class QueueBrowseToolPanel extends SimpleToolWindowPanel implements Disposable {

    public static final String JMS_MESSENGER_BROWSER_WINDOW_ID = "Jms Browser";
    public static final Icon JMS_MESSENGER_BROWSER_ICON = getBrowseIcon();

    private JPanel rootPanel;
    private JBTabsImpl queuesTabPanel;

    public QueueBrowseToolPanel() {
        super(true);
        render();
    }

    public static QueueBrowseToolPanel of() {
        return fetch(QueueBrowseToolPanel.class);
    }

    private void render() {
        queuesTabPanel = new JBTabsImpl(getProject());
        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(queuesTabPanel);
        setContent(rootPanel);
    }

    public void addQueueToBrowse(Queue queue) {
        TabInfo info = renderNewTab(queue);
        queuesTabPanel.addTab(info);
        queuesTabPanel.select(info, false);
    }

    private TabInfo renderNewTab(Queue queue) {
        TabInfo info = new TabInfo(new QueueBrowserTable(queue));
        info.setText(String.format("Server: %s queue: %s", queue.getServer().getName(), queue.getName()));
        return info;
    }

    @Override
    public void dispose() {
    }
}
