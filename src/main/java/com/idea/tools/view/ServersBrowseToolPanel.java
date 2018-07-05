package com.idea.tools.view;

import com.idea.tools.dto.Queue;
import com.idea.tools.dto.Server;
import com.idea.tools.markers.Listener;
import com.idea.tools.settings.Settings;
import com.idea.tools.view.action.*;
import com.idea.tools.view.render.TreeRender;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.Tree;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.idea.tools.App.*;
import static com.idea.tools.JmsMessengerWindowManager.JMS_MESSENGER_WINDOW_ID;
import static com.idea.tools.utils.GuiUtils.installActionGroupInToolBar;
import static com.idea.tools.utils.Utils.cast;
import static com.intellij.ui.PopupHandler.installPopupHandler;
import static com.intellij.ui.ScrollPaneFactory.createScrollPane;
import static java.awt.BorderLayout.CENTER;

public class ServersBrowseToolPanel extends SimpleToolWindowPanel implements Disposable {

    private static final Logger logger = Logger.getLogger(ServersBrowseToolPanel.class);

    private static final String UNAVAILABLE = "No server available";

    private static final String LOADING = "Loading...";
    private final Tree serversTree;
    private final Settings settings;
    private JPanel rootPanel;
    private JPanel serverPanel;
    private Listener<Server> serverListener;
    private Listener<Queue> queueListener;

    public ServersBrowseToolPanel(final Project project) {
        super(true);
        settings = settings();
        setProvideQuickActions(false);
        serversTree = createTree();

        this.serverListener = Listener.<Server>builder()
                .add(this::addServer)
                .edit(this::editServer)
                .remove(this::removeServer)
                .build();

        this.queueListener = Listener.<Queue>builder()
                .add(this::addQueue)
                .edit(this::editQueue)
                .remove(this::removeQueue)
                .build();

        serverService().addListener(serverListener);
        queueService().addListener(queueListener);

        serverPanel.setLayout(new BorderLayout());
        serverPanel.add(createScrollPane(serversTree), CENTER);

        setContent(rootPanel);
    }

    public static ServersBrowseToolPanel of() {
        return fetch(ServersBrowseToolPanel.class);
    }

    @Override
    public void dispose() {
        serverService().removeListener(serverListener);
        queueService().removeListener(queueListener);
        toolWindowManager().unregisterToolWindow(JMS_MESSENGER_WINDOW_ID);
    }

    public void init() {
        installActionsInToolbar();
        installActionsInPopupMenu();
        fillServerTree();
    }

    private void installActionsInToolbar() {
        DefaultActionGroup actions = new DefaultActionGroup("JmsMessengerToolbarGroup", false);

        actions.add(new ToolBarAddServerAction());
        actions.add(new ToolBarRemoveAction(this));
        actions.add(new ToolBarEditServerAction(this));
        actions.addSeparator();
        actions.add(new ToolBarSendMessageAction(this));
        actions.add(new ToolBarBrowseQueueAction(this));
        actions.addSeparator();
        actions.add(new ToolBarOpenPluginSettingsAction());

        installActionGroupInToolBar(actions, this, "JmsMessengerBrowserActions");
    }

    private void installActionsInPopupMenu() {
        DefaultActionGroup popup = new DefaultActionGroup("JmsMessengerPopupAction", true);

        popup.add(new PopupRemoveQueueAction(this));
        popup.add(new PopupEditQueueAction(this));
        popup.add(new PopupAddQueueAction(this));
        popup.addSeparator();
        popup.add(new PopupSendMessageAction(this));
        popup.add(new PopupBrowseQueueAction(this));
        popup.addSeparator();
        popup.add(new PopupEditServerAction(this));
        popup.add(new PopupRemoveServerAction(this));

        installPopupHandler(serversTree, popup, "POPUP", ActionManager.getInstance());
    }

    private void addQueue(Queue queue) {
        DefaultTreeModel model = (DefaultTreeModel) serversTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> data = root.children();

        findServerNode(data, queue.getServer()).ifPresent(node -> {
            node.removeAllChildren();
            fillQueueTree(queue.getServer(), node);
            model.nodeChanged(node);
        });

        serversTree.setModel(new DefaultTreeModel(root));
    }

    private void addServer(Server server) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) serversTree.getModel().getRoot();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(server);
        fillQueueTree(server, node);
        root.add(node);

        serversTree.setModel(new DefaultTreeModel(root));
    }

    private void editServer(Server server) {
        DefaultTreeModel model = (DefaultTreeModel) serversTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> data = root.children();

        findServerNode(data, server).ifPresent(node -> {
            node.removeAllChildren();
            node.setUserObject(server);
            fillQueueTree(server, node);
            model.nodeChanged(node);
        });
    }

    private void editQueue(Queue queue) {
        editServer(queue.getServer());
    }

    private void removeServer(Server server) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) serversTree.getModel().getRoot();
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> data = root.children();

        findServerNode(data, server).ifPresent(node -> {
            root.remove(node);
            serversTree.setModel(new DefaultTreeModel(root));
        });
    }

    private void removeQueue(Queue queue) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) serversTree.getModel().getRoot();
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> data = root.children();

        findServerNode(data, queue.getServer()).ifPresent(serverNode -> {
            @SuppressWarnings("unchecked")
            Enumeration<DefaultMutableTreeNode> queuesData = serverNode.children();
            findNode(queuesData, Queue.class, q -> q.getId().equals(queue.getId())).ifPresent(queueNode -> {
                serverNode.remove(queueNode);
                serversTree.setModel(new DefaultTreeModel(root));
            });
        });
    }

    private Optional<DefaultMutableTreeNode> findServerNode(Enumeration<DefaultMutableTreeNode> data, Server server) {
        return findNode(data, Server.class, s -> s.getId().equals(server.getId()));
    }

    private <T> Optional<DefaultMutableTreeNode> findNode(Enumeration<DefaultMutableTreeNode> data, Class<T> clazz, Predicate<T> predicate) {
        while (data.hasMoreElements()) {
            DefaultMutableTreeNode node = data.nextElement();
            Object value = node.getUserObject();
            if (clazz.isInstance(value) && predicate.test(clazz.cast(value))) {
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    private void fillServerTree() {
        List<Server> servers = settings.getServersList();

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(servers);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        serversTree.setModel(model);

        servers.forEach(s -> {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(s);
            fillQueueTree(s, node);
            rootNode.add(node);
        });

        serversTree.setRootVisible(true);
    }

    private void fillQueueTree(Server server, DefaultMutableTreeNode serverNode) {
        server.getQueues().forEach(queue -> serverNode.add(new DefaultMutableTreeNode(queue)));
    }

    private Tree createTree() {

        SimpleTree tree = new SimpleTree();
        tree.getEmptyText().setText(LOADING);
        tree.setCellRenderer(new TreeRender());
        tree.setName("serverTree");

        new TreeSpeedSearch(tree, treePath -> {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            final Object userObject = node.getUserObject();
            if (userObject instanceof Server) {
                return ((Server) userObject).getName();
            }
            return "<empty>";
        });

        return tree;
    }

    public <T> Optional<T> getSelectedValue(Class<T> clazz) {
        return Optional.ofNullable(serversTree.getLastSelectedPathComponent())
                       .map(cast(DefaultMutableTreeNode.class))
                       .map(DefaultMutableTreeNode::getUserObject)
                       .map(cast(clazz));
    }

}