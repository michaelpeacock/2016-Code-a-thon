package com.missionse.tacsit.layers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.missionse.application.ArcticWindowManager;
import com.missionse.menubar.MenuBar;
import com.missionse.menubar.MenuItemSelectionHandler;
import com.missionse.menubar.MenuSubItem;
import com.missionse.tacsit.window.TacsitManager;
import com.missionse.userinterface.basewindow.Window;

import javafx.embed.swing.SwingNode;

public class LayerWindow extends Window {
	private Boolean initialized = false;

	public LayerWindow() {
		setTitle("Layer Manager");
		setPrefSize(300, 500);

		JPanel layer = new JPanel();
		layer.setLayout(new BorderLayout(10, 10));
		layer.setPreferredSize(new Dimension(300, 500));
		layer.setMaximumSize(new Dimension(300, 500));
		// layer.setOpaque(false);
		layer.setBackground(new Color(0, 0, 0, 0));
		layer.add(new LayerManagerPanel(TacsitManager.getInstance().getWorldWindowJPanel()), BorderLayout.CENTER);

		layer.setBackground(new Color(0, 0, 0, 65));
		// layer.setPreferredSize(new Dimension(250,150));

		SwingNode swingNode = new SwingNode();
		swingNode.setOpacity(0.5);
		swingNode.setContent(layer);
		// this.setCenter(swingNode);
		// Platform.runLater(new Runnable(){
		// @Override
		// public void run() {
		getContentPane().getChildren().add(swingNode);
		// }});

		// this.addNode(swingNode);

		initialize();
	}

	private void initialize() {
		MenuBar menu = ArcticWindowManager.getMenu();
		MenuItemSelectionHandler handler = new MenuItemSelectionHandler() {

			@Override
			public void onMenuItemSelection(String parent, MenuSubItem item) {
				System.out.println("Menu selection: parent: " + parent + " item: " + item.getSubMenuItemName());
				if (item.getSubMenuItemName().equals("Layer Management")) {
					show();
				}

			}

		};

		MenuSubItem subMenu = new MenuSubItem("Layer Management", handler);
		menu.addMenuItem("Layers", subMenu);

		this.setVisible(false);
	}

	public LayerWindow getLayerWindow() {
		return this;
	}
}
