package com.idea.tools.view.components;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.view.button.CopyButton;
import com.idea.tools.view.button.ShowHideButton;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.tabs.JBTabsPosition;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

import static com.idea.tools.utils.GuiUtils.toolbar;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class QueueBrowserPanel extends JPanel {

    private final Project project;

    private DestinationDto destination;

    @Getter
    private QueueBrowserTable queueBrowserTable;
    private HeaderViewTable headerTable;
    private JBTabsImpl tabsPanel;
    private JTextArea payloadField;

    public QueueBrowserPanel(Project project, DestinationDto queue) {
        this.destination = queue;
        this.project = project;
        render();
    }

    private void render() {
        setLayout(new BorderLayout());
        queueBrowserTable = new QueueBrowserTable(project, destination);
        headerTable = new HeaderViewTable(Collections.emptyList());
        initTabPanel();

        queueBrowserTable.getTable().getSelectionModel().addListSelectionListener(event -> {
            JBTable table = queueBrowserTable.getTable();
            if (table.getSelectedRow() >= 0) {
                MessageDto message = queueBrowserTable.getData().get(table.getSelectedRow());
                headerTable.setData(message.getHeaders());
                payloadField.setText(message.getPayload());
            } else {
                headerTable.setData(Collections.emptyList());
                payloadField.setText("");
            }
        });

        DefaultActionGroup actions = new DefaultActionGroup("showHideHeadersActionGroup", false);
        actions.add(ShowHideButton.of(tabsPanel));

        JPanel headersPanel = new JPanel();
        headersPanel.setLayout(new BorderLayout());
        headersPanel.add(toolbar(actions, "showHideHeadersToolbar", false), WEST);
        headersPanel.add(tabsPanel, CENTER);

        add(queueBrowserTable, BorderLayout.CENTER);
        add(headersPanel, BorderLayout.EAST);
    }

    private void initTabPanel() {
        tabsPanel = new JBTabsImpl(project);
        tabsPanel.setTabsPosition(JBTabsPosition.right);

//        Headers tab
        TabInfo headersTab = new TabInfo(headerTable);
        headersTab.setText("Headers");
        tabsPanel.addTab(headersTab);

//        Payload tab
        JPanel panel = new JPanel(new BorderLayout());
        JPanel toolbarPanel = new JPanel(new BorderLayout());

        payloadField = new JTextArea();
        payloadField.setPreferredSize(new Dimension(450, 250));

        JCheckBox wrapLinesCheckBox = new JCheckBox();
        wrapLinesCheckBox.setText("Wrap lines");
        wrapLinesCheckBox.setSelected(true);
        wrapLinesCheckBox.addActionListener(event -> payloadField.setLineWrap(wrapLinesCheckBox.isSelected()));

        payloadField.setLineWrap(wrapLinesCheckBox.isSelected());

        DefaultActionGroup copyActionGroup = new DefaultActionGroup("CopyPasteBrowseToolbar", false);
        copyActionGroup.add(new CopyButton(payloadField));

        toolbarPanel.add(toolbar(copyActionGroup, "PayloadBrowsePanel", true), BorderLayout.EAST);
        toolbarPanel.add(wrapLinesCheckBox, BorderLayout.WEST);

        panel.add(toolbarPanel, BorderLayout.NORTH);
        panel.add(new JBScrollPane(payloadField), BorderLayout.CENTER);

        TabInfo payloadTab = new TabInfo(panel);
        payloadTab.setText("Payload");
        tabsPanel.addTab(payloadTab);

        tabsPanel.select(payloadTab, false);
    }
}
