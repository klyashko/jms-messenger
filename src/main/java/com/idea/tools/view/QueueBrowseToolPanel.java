package com.idea.tools.view;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.view.components.QueueBrowserPanel;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.idea.tools.App.fetch;
import static com.idea.tools.App.getProject;
import static com.idea.tools.utils.IconUtils.getBrowseIcon;

public class QueueBrowseToolPanel extends SimpleToolWindowPanel implements Disposable {

    public static final String JMS_MESSENGER_BROWSER_WINDOW_ID = "Jms Browser";
    public static final Icon JMS_MESSENGER_BROWSER_ICON = getBrowseIcon();

    private JPanel rootPanel;
    private JBTabsImpl queuesTabPanel;

    private Map<Pair<String, String>, TabInfo> queueTabs = new ConcurrentHashMap<>();

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

    public QueueBrowserTable addQueueToBrowse(DestinationDto queue) {
        TabInfo info = queueTabs.computeIfAbsent(Pair.of(queue.getServer().getId(), queue.getId()), pair -> {
            TabInfo tab = renderNewTab(queue);
            queuesTabPanel.addTab(tab);
            return tab;
        });
        QueueBrowserTable table = ((QueueBrowserPanel) info.getComponent()).getQueueBrowserTable();
        queuesTabPanel.select(info, false);
        return table;
    }

    private TabInfo renderNewTab(DestinationDto queue) {
        TabInfo info = new TabInfo(new QueueBrowserPanel(queue));
        info.setText(String.format("Server: %s queue: %s", queue.getServer().getName(), queue.getName()));
        return info;
    }

    @Override
    public void dispose() {
    }
}
