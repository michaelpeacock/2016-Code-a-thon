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
import java.io.InputStream;

import gov.nasa.worldwind.formats.shapefile.Shapefile;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecord;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwindx.examples.util.ShapefileLoader;

public class WorldBorders extends RenderableLayer {

	protected static final String SHAPE_FILE_PATH = "mapdata/WORLD_BORDERS.shp";
	private boolean displayed = true;

	public WorldBorders() {
		this.setName("Borders Layer");

		ShapefileLoader loader = new ShapefileLoader() {
			protected ShapeAttributes createPolygonAttributes(ShapefileRecord record) {
				ShapeAttributes attrs = new BasicShapeAttributes();
				attrs.setInteriorMaterial(new Material(new Color(85, 87, 71)));
				attrs.setOutlineMaterial(new Material(Color.BLACK));
				attrs.setInteriorOpacity(0);
				attrs.setOutlineWidth(1);
				return attrs;
			}
		};

		InputStream input = LandLayer.class.getResourceAsStream(SHAPE_FILE_PATH);
		RenderableLayer newLayer = (RenderableLayer) loader.createLayerFromShapefile(new Shapefile(input));
		this.addRenderables(newLayer.getRenderables());

		this.setPickEnabled(false);
	}
}
