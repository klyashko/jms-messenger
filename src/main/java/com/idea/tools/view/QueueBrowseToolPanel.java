package com.idea.tools.view;

import static com.idea.tools.utils.IconUtils.getBrowseIcon;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.view.components.QueueBrowserPanel;
import com.idea.tools.view.components.QueueBrowserTable;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;
import org.apache.commons.lang3.tuple.Pair;

public class QueueBrowseToolPanel extends SimpleToolWindowPanel implements Disposable {

    public static final String JMS_MESSENGER_BROWSER_WINDOW_ID = "Jms Browser";
    public static final Icon JMS_MESSENGER_BROWSER_ICON = getBrowseIcon();

    private final Project project;
    private JPanel rootPanel;
    private JBTabsImpl queuesTabPanel;

    private final Map<Pair<String, String>, TabInfo> queueTabs = new ConcurrentHashMap<>();

    public QueueBrowseToolPanel(Project project) {
        super(true);
        this.project = project;
        render();
    }

    public static QueueBrowseToolPanel of(Project project) {
        return ServiceManager.getService(project, QueueBrowseToolPanel.class);
    }

    private void render() {
        queuesTabPanel = new JBTabsImpl(project);
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
        queuesTabPanel.select(info, true);
        return table;
    }

    private TabInfo renderNewTab(DestinationDto queue) {
        TabInfo info = new TabInfo(new QueueBrowserPanel(project, queue));
        info.setText(String.format("Server: %s queue: %s", queue.getServer().getName(), queue.getName()));
        return info;
    }

    @Override
    public void dispose() {
    }
}
