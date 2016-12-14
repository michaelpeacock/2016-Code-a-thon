/*******************************************************************************
 * Copyright (c) 2016 ASRC Federal Mission Solutions.
 * All rights reserved. No warranty, explicit or implicit, provided. This 
 * program and the accompanying materials are proprietary and 
 * confidential. Unauthorized copying or distribution of this file, 
 * via any medium, is strictly prohibited without consent from
 * ASRC Federal Mission Solutions.
 *******************************************************************************/
package com.missionse.tacsit.window;

import javax.swing.JPanel;

import com.missionse.application.ArcticWindowManager;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.awt.ViewInputAttributes;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.view.orbit.OrbitView;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;

/**
 * This is the most basic World Wind program.
 *
 * @version $Id: HelloWorldWind.java 1971 2014-04-29 21:31:28Z dcollins $
 */
public class TacsitWindow {
	private static JPanel conferencePanel = null;
	private WorldWindowGLJPanel wwd;

	public TacsitWindow() {
		SwingNode swingNode = new SwingNode();

		this.wwd = TacsitManager.getInstance().getWorldWindowJPanel();
		wwd.setModel(new BasicModel());
		swingNode.setContent(this.wwd);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				ArcticWindowManager.getCenterPane().getChildren().add(swingNode);
			}

		});

		OrbitView view = (OrbitView) wwd.getView();
		view.setZoom(256 * 6076 * 6);

		ViewInputAttributes attrs = wwd.getView().getViewInputHandler().getAttributes();
		attrs.getActionMap(ViewInputAttributes.DEVICE_MOUSE).getActionAttributes(ViewInputAttributes.VIEW_MOVE_TO)
				.setMouseActionListener(null);

		// Create and install the view controls layer and register a controller
		// for it with the World Window.
		// ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
		for (Layer layer : this.wwd.getModel().getLayers()) {
			System.out.println("Layer name: " + layer.getName());
		}

		ViewControlsLayer viewControls = (ViewControlsLayer) this.wwd.getModel().getLayers()
				.getLayerByName("View Controls");
		if (null != viewControls) {
			this.wwd.addSelectListener(new ViewControlsSelectListener(this.wwd, viewControls));
		}
	}

	// public JDesktopPane getDesktop() {
	// return desktop;
	// }
	//
	// public static TacsitApplication getFrame() {
	// return tacsitFrame;
	// }

	public static JPanel getConferencePanel() {
		return conferencePanel;
	}

	public static void setConferencePanel(JPanel conferencePanel) {
		TacsitWindow.conferencePanel = conferencePanel;
	}

	public static void main(String[] args) {
		if (Configuration.isMacOS()) {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Hello World Wind");
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("START TACIT");

				// Create an AppFrame and immediately make it visible. As per
				// Swing convention, this
				// is done within an invokeLater call so that it executes on an
				// AWT thread.
				new TacsitWindow();
			}
		});
	}
}