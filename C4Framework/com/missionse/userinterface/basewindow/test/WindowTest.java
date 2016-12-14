package com.missionse.userinterface.basewindow.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.missionse.application.ArcticWindowManager;
import com.missionse.menubar.MenuBar;
import com.missionse.menubar.MenuItemSelectionHandler;
import com.missionse.menubar.MenuSubItem;
import com.missionse.tacsit.layers.LayerManagerPanel;
import com.missionse.tacsit.window.TacsitManager;
import com.missionse.userinterface.basewindow.Window;

import javafx.embed.swing.SwingNode;

public class WindowTest extends Window {
	public WindowTest() {

		setTitle("Window Test");
		// w.setTitle("Window Test");
		setPrefSize(300, 500);
		setLayoutX(30);
		setLayoutY(30);

		JPanel layer = new JPanel();
		layer.setLayout(new BorderLayout(10, 10));
		layer.setPreferredSize(new Dimension(300, 300));
		layer.setMaximumSize(new Dimension(300, 500));
		// layer.setOpaque(false);
		layer.setBackground(new Color(0, 0, 0, 0));
		// layer.add(new
		// LayerManagerPanel(TacsitManager.getInstance().getWorldWindowJPanel()),
		// BorderLayout.CENTER);

		SwingNode swingNode = new SwingNode();
		swingNode.setOpacity(0.5);
		swingNode.setContent(new LayerManagerPanel(TacsitManager.getInstance().getWorldWindowJPanel()));
		getContentPane().getChildren().add(swingNode);

		// layer.add(new
		// LayerManagerPanel(TacsitManager.getInstance().getWorldWindowJPanel()),
		// BorderLayout.CENTER);

		layer.setBackground(new Color(0, 0, 0, 65));
		// layer.setPreferredSize(new Dimension(250,150));

		MenuBar menu = ArcticWindowManager.getMenu();
		MenuItemSelectionHandler handler = new MenuItemSelectionHandler() {

			@Override
			public void onMenuItemSelection(String parent, MenuSubItem item) {
				System.out.println("Menu selection: parent: " + parent + " item: " + item.getSubMenuItemName());
				if (item.getSubMenuItemName().equals("Layer Test")) {
					show();
				}

			}

		};

		MenuSubItem subMenu = new MenuSubItem("Layer Test", handler);
		menu.addMenuItem("Layers", subMenu);

		setVisible(false);
	}
}
