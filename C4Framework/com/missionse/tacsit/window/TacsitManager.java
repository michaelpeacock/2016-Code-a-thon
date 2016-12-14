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

import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.view.orbit.OrbitView;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class TacsitManager {
	private static TacsitManager theInstance = new TacsitManager();
	private WorldWindowGLJPanel worldWindPanel = null;
	
	private TacsitManager () {
		worldWindPanel = new WorldWindowGLJPanel();
	}
	
	public static TacsitManager getInstance() {
		return theInstance;		
	}
	
	public WorldWindowGLJPanel getWorldWindowJPanel() {
		return worldWindPanel;
	}

	public void rangeIn() {
		((OrbitView) worldWindPanel.getView()).setZoom(128*6076);
		
	}
	
	public void rangeOut() {
		((OrbitView) worldWindPanel.getView()).setZoom(512*6076);
	}
	
	public void update() {
		worldWindPanel.redrawNow();;
	}

	public void setCenterPosition(Position position) {
		((OrbitView) worldWindPanel.getView()).setCenterPosition(position);
		
	}
}
