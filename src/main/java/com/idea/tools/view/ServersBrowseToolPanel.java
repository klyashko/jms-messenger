package com.idea.tools.view;

import com.idea.tools.App;
import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.DestinationType;
import com.idea.tools.dto.ServerDto;
import com.idea.tools.dto.TemplateMessageDto;
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
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.idea.tools.App.*;
import static com.idea.tools.dto.DestinationType.QUEUE;
import static com.idea.tools.dto.DestinationType.TOPIC;
import static com.idea.tools.utils.GuiUtils.installActionGroupInToolBar;
import static com.idea.tools.utils.Utils.cast;
import static com.idea.tools.utils.Utils.groupingBy;
import static com.intellij.ui.PopupHandler.installPopupHandler;
import static com.intellij.ui.ScrollPaneFactory.createScrollPane;
import static java.awt.BorderLayout.CENTER;

public class ServersBrowseToolPanel extends SimpleToolWindowPanel implements Disposable {

    private static final Logger logger = Logger.getLogger(ServersBrowseToolPanel.class);

    private static final String LOADING = "Loading...";
    private final Tree serversTree;
    private final Settings settings;
    private JPanel rootPanel;
    private JPanel serverPanel;
    private Listener<ServerDto> serverListener;
    private Listener<DestinationDto> destinationListener;
    private Listener<TemplateMessageDto> templateListener;

    public ServersBrowseToolPanel(final Project project) {
        super(true);
        App.setProject(project);
        settings = settings();
        setProvideQuickActions(false);
        serversTree = createTree();

        this.serverListener = Listener.<ServerDto>builder()
                .add(s -> addOrUpdateServer())
                .edit(s -> addOrUpdateServer())
                .remove(this::removeServer)
                .build();

        this.destinationListener = Listener.<DestinationDto>builder()
                .add(this::addOrUpdateDestination)
                .edit(this::addOrUpdateDestination)
                .remove(this::removeDestination)
                .build();

        this.templateListener = Listener.<TemplateMessageDto>builder()
                .add(this::addOrUpdateTemplate)
                .edit(this::addOrUpdateTemplate)
                .remove(this::removeTemplate)
                .build();

        serverService().addListener(serverListener);
        destinationService().addListener(destinationListener);
        templateService().addListener(templateListener);

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
        destinationService().removeListener(destinationListener);
        templateService().removeListener(templateListener);
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
        actions.add(new ToolBarReconnectAction(this));
        actions.add(new ToolBarSendMessageAction(this));
        actions.add(new ToolBarBrowseQueueAction(this));
        actions.addSeparator();
        actions.add(new ToolBarOpenSettingsAction());

        installActionGroupInToolBar(actions, this, "JmsMessengerBrowserActions");
    }

    private void installActionsInPopupMenu() {
        DefaultActionGroup popup = new DefaultActionGroup("JmsMessengerPopupAction", true);

        popup.add(new PopupRemoveTemplateAction(this));
        popup.add(new PopupEditTemplateAction(this));
        popup.addSeparator();
        popup.add(new PopupRemoveDestinationAction(this));
        popup.add(new PopupEditDestinationAction(this));
        popup.add(new PopupAddDestinationAction(this));
        popup.addSeparator();
        popup.add(new PopupReconnectAction(this));
        popup.add(new PopupSendMessageAction(this));
        popup.add(new PopupBrowseQueueAction(this));
        popup.addSeparator();
        popup.add(new PopupRemoveServerAction(this));
        popup.add(new PopupEditServerAction(this));

        installPopupHandler(serversTree, popup, "POPUP", ActionManager.getInstance());
    }

    private void addOrUpdateDestination(DestinationDto destination) {
        ServerDto server = destination.getServer();
        DefaultTreeModel model = (DefaultTreeModel) serversTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> data = root.children();

        findServerNode(data, server).ifPresent(node -> {
            node.removeAllChildren();
            fillDestinationTypeTree(server, node);
            model.nodeChanged(node);
            model.reload(node);
        });
    }

    private void addOrUpdateTemplate(TemplateMessageDto template) {
        DestinationDto destination = template.getDestination();

        DefaultTreeModel model = (DefaultTreeModel) serversTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> serverNodes = root.children();

        findDestinationNode(serverNodes, destination).ifPresent(node -> {
            node.removeAllChildren();
            fillTemplateTree(destination, node);
            model.nodeChanged(node);
            model.reload(node);
        });
    }

    private void addOrUpdateServer() {
        List<ServerDto> servers = settings.getServersList();
        Collections.sort(servers);

        DefaultTreeModel model = (DefaultTreeModel) serversTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();

        root.setUserObject(servers);

        servers.forEach(s -> {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(s);
            fillDestinationTypeTree(s, node);
            root.add(node);
        });
        model.reload(root);
    }

    private void removeServer(ServerDto server) {
        DefaultTreeModel model = (DefaultTreeModel) serversTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> data = root.children();

        findServerNode(data, server).ifPresent(node -> {
            root.remove(node);
            model.reload(root);
        });
    }

    private void removeDestination(DestinationDto destination) {
        DefaultTreeModel model = (DefaultTreeModel) serversTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        Supplier<Enumeration<DefaultMutableTreeNode>> data = root::children;

        findDestinationNode(data.get(), destination).ifPresent(queueNode -> {
            DefaultMutableTreeNode typeNode = (DefaultMutableTreeNode) queueNode.getParent();
            typeNode.remove(queueNode);
            model.reload(typeNode);
        });
    }

    private void removeTemplate(TemplateMessageDto template) {
        DefaultTreeModel model = (DefaultTreeModel) serversTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        Supplier<Enumeration<DefaultMutableTreeNode>> data = root::children;

        findTemplateNode(data.get(), template).ifPresent(templateNode -> {
            DefaultMutableTreeNode destinationNode = (DefaultMutableTreeNode) templateNode.getParent();
            destinationNode.remove(templateNode);
            model.reload(destinationNode);
        });
    }

    private Optional<DefaultMutableTreeNode> findServerNode(Enumeration<DefaultMutableTreeNode> data, ServerDto server) {
        return findNode(data, ServerDto.class, s -> s.getId().equals(server.getId()));
    }

    private Optional<DefaultMutableTreeNode> findDestinationNode(Enumeration<DefaultMutableTreeNode> data, DestinationDto destination) {
        return findServerNode(data, destination.getServer()).flatMap(serverNode -> {
            @SuppressWarnings("unchecked")
            Enumeration<DefaultMutableTreeNode> types = serverNode.children();
            return findNode(types, DestinationType.class, type -> type.equals(destination.getType()))
                    .flatMap(node -> {
                        @SuppressWarnings("unchecked")
                        Enumeration<DefaultMutableTreeNode> destinations = node.children();
                        return findNode(destinations, DestinationDto.class, d -> d.getId().equals(destination.getId()));
                    });
        });
    }

    private Optional<DefaultMutableTreeNode> findTemplateNode(Enumeration<DefaultMutableTreeNode> data, TemplateMessageDto template) {
        return findDestinationNode(data, template.getDestination()).flatMap(queueNode -> {
            @SuppressWarnings("unchecked")
            Enumeration<DefaultMutableTreeNode> templates = queueNode.children();
            return findNode(templates, TemplateMessageDto.class, t -> t.getId().equals(template.getId()));
        });
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
        List<ServerDto> servers = settings.getServersList();
        Collections.sort(servers);

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(servers);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        serversTree.setModel(model);

        servers.forEach(s -> {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(s);
            fillDestinationTypeTree(s, node);
            rootNode.add(node);
        });

        serversTree.setRootVisible(true);
    }

    private void fillDestinationTypeTree(ServerDto server, DefaultMutableTreeNode serverNode) {
        Map<DestinationType, List<DestinationDto>> destinations = groupingBy(server.getDestinations(), DestinationDto::getType);
        for (DestinationType type : Arrays.asList(QUEUE, TOPIC)) {
            List<DestinationDto> destinationList = destinations.getOrDefault(type, Collections.emptyList());
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(type);
            fillDestinationTree(destinationList, node);
            serverNode.add(node);
        }
    }

    private void fillDestinationTree(List<DestinationDto> destinations, DefaultMutableTreeNode serverNode) {
        Collections.sort(destinations);
        destinations.forEach(destination -> {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(destination);
            fillTemplateTree(destination, node);
            serverNode.add(node);
        });
    }

    private void fillTemplateTree(DestinationDto queue, DefaultMutableTreeNode queueNode) {
        Collections.sort(queue.getTemplates());
        queue.getTemplates().forEach(template -> queueNode.add(new DefaultMutableTreeNode(template)));
    }

    private Tree createTree() {

        SimpleTree tree = new SimpleTree();
        tree.getEmptyText().setText(LOADING);
        tree.setCellRenderer(new TreeRender());
        tree.setName("serverTree");

        new TreeSpeedSearch(tree, treePath -> {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            final Object userObject = node.getUserObject();
            if (userObject instanceof ServerDto) {
                return ((ServerDto) userObject).getName();
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