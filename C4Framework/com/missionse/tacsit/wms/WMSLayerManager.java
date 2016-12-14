/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package com.missionse.tacsit.wms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.missionse.application.ArcticWindowManager;
import com.missionse.menubar.MenuBar;
import com.missionse.menubar.MenuItemSelectionHandler;
import com.missionse.menubar.MenuSubItem;
import com.missionse.tacsit.window.TacsitManager;
import com.missionse.userinterface.widgets.WidgetFrame;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwindx.examples.WMSLayersPanel;
import javafx.embed.swing.SwingNode;
import javafx.stage.FileChooser;

/**
 * This example demonstrates the use of multiple WMS layers, as displayed in a
 * WMSLayersPanel.
 *
 * @author tag
 * @version $Id: WMSLayerManager.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class WMSLayerManager extends WidgetFrame {
	protected final Dimension wmsPanelSize = new Dimension(400, 600);
	protected JTabbedPane tabbedPane;
	protected int previousTabIndex;

	protected static final String[] servers = new String[] { "http://neowms.sci.gsfc.nasa.gov/wms/wms",
			"http://wms.alaskamapped.org/gina/bdl"

	};

	public WMSLayerManager() {
		super("WMS Layer Manager", 400, 600);

		makeMenu();

		this.tabbedPane = new JTabbedPane();

		this.tabbedPane.add(new JPanel());
		this.tabbedPane.setTitleAt(0, "+");
		this.tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				if (tabbedPane.getSelectedIndex() != 0) {
					previousTabIndex = tabbedPane.getSelectedIndex();
					return;
				}

				String server = JOptionPane.showInputDialog("Enter wms server URL");
				if (server == null || server.length() < 1) {
					tabbedPane.setSelectedIndex(previousTabIndex);
					return;
				}

				// Respond by adding a new WMSLayerPanel to the tabbed pane.
				if (addTab(tabbedPane.getTabCount(), server.trim()) != null)
					tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
			}
		});

		// Create a tab for each server and add it to the tabbed panel.
		for (int i = 0; i < servers.length; i++) {
			this.addTab(i + 1, servers[i]); // i+1 to place all server tabs to
											// the right of the Add Server tab
		}

		// Display the first server pane by default.
		this.tabbedPane.setSelectedIndex(this.tabbedPane.getTabCount() > 0 ? 1 : 0);
		this.previousTabIndex = this.tabbedPane.getSelectedIndex();

		// Add the tabbed pane to a frame separate from the world window.
		// JFrame controlFrame = new JFrame();
		// controlFrame.getContentPane().add(tabbedPane);
		// controlFrame.pack();
		// controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// controlFrame.setVisible(true);

		tabbedPane.setPreferredSize(wmsPanelSize);
		tabbedPane.setMaximumSize(wmsPanelSize);
		SwingNode swingNode = new SwingNode();
		// swingNode.setOpacity(0.75);
		swingNode.setContent(tabbedPane);

		this.addNode(swingNode);

	}

	protected void makeMenu() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("KML Loader");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("KML", "*.kml"));

		MenuBar menu = ArcticWindowManager.getMenu();
		MenuItemSelectionHandler handler = new MenuItemSelectionHandler() {
			@Override
			public void onMenuItemSelection(String parent, MenuSubItem item) {
				if (item.getSubMenuItemName().equals("Load WMS Data")) {
					WMSLayerManager.this.show();
				}
			}
		};

		MenuSubItem subMenu = new MenuSubItem("Load WMS Data", handler);
		menu.addMenuItem("Layers", subMenu);
	}

	private WorldWindow getWwd() {
		return TacsitManager.getInstance().getWorldWindowJPanel();
	}

	protected WMSLayersPanel addTab(int position, String server) {
		// Add a server to the tabbed dialog.
		try {
			WMSLayersPanel layersPanel = new WMSLayersPanel(this.getWwd(), server, wmsPanelSize);
			this.tabbedPane.add(layersPanel, BorderLayout.CENTER);
			String title = layersPanel.getServerDisplayString();
			this.tabbedPane.setTitleAt(position, title != null && title.length() > 0 ? title : server);

			// Add a listener to notice wms layer selections and tell the layer
			// panel to reflect the new state.
			layersPanel.addPropertyChangeListener("LayersPanelUpdated", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
					Layer newLayer = (Layer) propertyChangeEvent.getNewValue();
					// AppFrame.this.getLayerPanel().update(WMSLayerManager.this.getWwd());
					TacsitManager.getInstance().getWorldWindowJPanel().getModel().getLayers().add(newLayer);
				}
			});

			return layersPanel;
		} catch (URISyntaxException e) {
			JOptionPane.showMessageDialog(null, "Server URL is invalid", "Invalid Server URL",
					JOptionPane.ERROR_MESSAGE);
			tabbedPane.setSelectedIndex(previousTabIndex);
			return null;
		}
	}
}
