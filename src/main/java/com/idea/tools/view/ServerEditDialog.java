package com.idea.tools.view;

import com.idea.tools.dto.ServerDto;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.components.JBScrollPane;
import javax.swing.*;

public class ServerEditDialog extends JFrame {

	private final Project project;

	private ServerEditDialog(Project project, ServerDto server) {
		this.project = project;
		render(server);
	}

	public static void showDialog(Project project) {
		showDialog(project, new ServerDto());
	}

	public static void showDialog(Project project, ServerDto server) {
		SwingUtilities.invokeLater(() -> {
			ServerEditDialog dialog = new ServerEditDialog(project, server);
			dialog.setLocationRelativeTo(null);
			dialog.pack();
			dialog.setVisible(true);
		});
	}

	private void render(ServerDto server) {
		ServerEditPanel panel = new ServerEditPanel(project, server);
		panel.getCancelButton().setText("Close");
		panel.getCancelButton().addActionListener(event -> dispose());

		JRootPane rootPane = new JRootPane();
		rootPane.setSize(panel.getWidth(), panel.getHeight());
		IdeGlassPaneImpl pane = new IdeGlassPaneImpl(rootPane);
		pane.setSize(panel.getWidth(), panel.getHeight());
		setGlassPane(pane);

		JBScrollPane scrollPane = new JBScrollPane(panel);
		scrollPane.setSize(panel.getWidth(), panel.getHeight());
		add(scrollPane);
	}
}
