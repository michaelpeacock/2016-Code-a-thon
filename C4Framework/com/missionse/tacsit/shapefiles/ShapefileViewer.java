package com.missionse.tacsit.shapefiles;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.formats.shapefile.*;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.*;
import gov.nasa.worldwindx.examples.util.*;
import javafx.stage.FileChooser;

import java.awt.Color;
import java.io.File;
import java.util.List;

import javax.swing.SwingUtilities;

import com.missionse.application.ArcticWindowManager;
import com.missionse.menubar.MenuBar;
import com.missionse.menubar.MenuItemSelectionHandler;
import com.missionse.menubar.MenuSubItem;
import com.missionse.tacsit.window.TacsitManager;
import com.missionse.userinterface.widgets.WidgetFrame;

public class ShapefileViewer implements ShapefileLayerFactory.CompletionCallback {
	private static RandomShapeAttributes randomAttrs = new RandomShapeAttributes();
	private static ShapefileViewer viewer;
	
	public ShapefileViewer() {
		viewer = this;
		
		makeMenu();
	}

	public static ShapefileViewer getShapefileViewer() {
		return viewer;
	}
	
	public WorldWindow getWwd() {
		return TacsitManager.getInstance().getWorldWindowJPanel();
	}

	public static void loadShapefile(Object source)
	{
		ShapefileLayerFactory factory = new ShapefileLayerFactory();
		//factory.setNormalPointAttributes(randomAttrs.asPointAttributes());
		//factory.setNormalShapeAttributes(randomAttrs.asShapeAttributes());

		factory.setAttributeDelegate(new ShapefileRenderable.AttributeDelegate() {
			@Override
			public void assignAttributes(ShapefileRecord shapefileRecord, ShapefileRenderable.Record renderableRecord)
			{
				//randomAttrs.nextAttributes(); // display each shapefile in different attributes

				ShapeAttributes attributes = new BasicShapeAttributes();
				attributes.setInteriorMaterial(new Material(new Color(255,204,229)));
				//attributes.setOutlineMaterial(new Material(new Color(70,87,74)));

				renderableRecord.setAttributes(attributes);
			}
		});

		
		
		
		factory.createFromShapefileSource(source, getShapefileViewer()); // add the layer in the completion callback

		//this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	@Override
	public void completion(final Object result)
	{
		if (!SwingUtilities.isEventDispatchThread())
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					completion(result);
				}
			});
			return;
		}

		Layer layer = (Layer) result;
		layer.setName(WWIO.getFilename(layer.getName())); // convert the layer name to the source's filename
		this.getWwd().getModel().getLayers().add(layer);

//		Sector sector = (Sector) layer.getValue(AVKey.SECTOR);
//		if (sector != null)
//		{
//			ExampleUtil.goTo(this.getWwd(), sector);
//		}

		//this.setCursor(null);
	}

	@Override
	public void exception(Exception e)
	{
		Logging.logger().log(java.util.logging.Level.SEVERE, e.getMessage(), e);
	}

	protected static void makeMenu()
	{
		final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Shapefile Loader");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SHP", "*.shp")
            );

		MenuBar menu = ArcticWindowManager.getMenu();
		MenuItemSelectionHandler handler = 	new MenuItemSelectionHandler() {

			@Override
			public void onMenuItemSelection(String parent, MenuSubItem item) {
				System.out.println("Menu selection: parent: " + parent + " item: " + item.getSubMenuItemName());
				if (item.getSubMenuItemName().equals("Load Shapefile")) {
					try
					{
						List<File> list = fileChooser.showOpenMultipleDialog(ArcticWindowManager.getStage());
						if (list != null) {
							for (File file : list) {

								loadShapefile(file);
							}
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}

			}
		};

		MenuSubItem subMenu = new MenuSubItem("Load Shapefile", handler);
		menu.addMenuItem("Layers", subMenu);
	}
}
