/*******************************************************************************
 * Copyright (c) 2016 ASRC Federal Mission Solutions.
 * All rights reserved. No warranty, explicit or implicit, provided. This 
 * program and the accompanying materials are proprietary and 
 * confidential. Unauthorized copying or distribution of this file, 
 * via any medium, is strictly prohibited without consent from
 * ASRC Federal Mission Solutions.
 *******************************************************************************/
package com.missionse.tacsit.layers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.SurfaceImage;

public class WaterLayer extends RenderableLayer {
	private boolean displayed = true;

	public WaterLayer() {
		this.setName("Water Layer");

		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		// g.setColor(new Color(70, 70, 70)); // AEGIS water color
		g.setColor(new Color(20, 40, 75));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.dispose();

		this.addRenderable(new SurfaceImage(image, Sector.FULL_SPHERE));

		// Disable picking for the layer because it covers the full sphere and
		// will override a terrain pick.
		this.setPickEnabled(false);

	}

}
