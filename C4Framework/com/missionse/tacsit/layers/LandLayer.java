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

import javax.swing.SwingUtilities;

import com.missionse.tacsit.window.TacsitManager;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.formats.shapefile.ShapefileLayerFactory;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecord;
import gov.nasa.worldwind.formats.shapefile.ShapefileRenderable;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.Logging;

public class LandLayer extends RenderableLayer {

	protected static final String SHAPE_FILE_PATH = "mapdata/TM_WORLD_BORDERS-0.3.shp";
	private boolean displayed = true;

	public LandLayer() {
		this.setName("Land Layer");

		InputStream input = LandLayer.class.getResourceAsStream(SHAPE_FILE_PATH);

		ShapefileLayerFactory factory = new ShapefileLayerFactory();
		factory.setAttributeDelegate(new ShapefileRenderable.AttributeDelegate() {
			@Override
			public void assignAttributes(ShapefileRecord shapefileRecord, ShapefileRenderable.Record renderableRecord) {
				ShapeAttributes attributes = new BasicShapeAttributes();
				// attributes.setInteriorMaterial(new Material(new
				// Color(85,87,71))); // AEGIS Land Color
				// attributes.setOutlineMaterial(new Material(new
				// Color(85,87,71)));

				attributes.setInteriorMaterial(new Material(new Color(70, 87, 74)));
				attributes.setOutlineMaterial(new Material(new Color(70, 87, 74)));

				renderableRecord.setAttributes(attributes);
			}
		});

		factory.createFromShapefileSource(input, new ShapefileLayerFactory.CompletionCallback() {

			@Override
			public void completion(final Object result) {
				if (!SwingUtilities.isEventDispatchThread()) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							completion(result);
						}
					});
					return;
				}

				RenderableLayer layer = (RenderableLayer) result;
				// layer.setName(WWIO.getFilename("Land Layer")); // convert the
				// layer name to the source's filename
				// getWwd().getModel().getLayers().add(layer);

				addRenderables(layer.getRenderables());
			}

			@Override
			public void exception(Exception e) {
				Logging.logger().log(java.util.logging.Level.SEVERE, e.getMessage(), e);
			}

		});

		this.setPickEnabled(false);

	}

	public WorldWindow getWwd() {
		return TacsitManager.getInstance().getWorldWindowJPanel();
	}

}
