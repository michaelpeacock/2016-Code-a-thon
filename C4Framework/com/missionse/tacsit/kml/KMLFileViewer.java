package com.missionse.tacsit.kml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.xml.stream.XMLStreamException;

import com.missionse.application.ArcticWindowManager;
import com.missionse.menubar.MenuBar;
import com.missionse.menubar.MenuItemSelectionHandler;
import com.missionse.menubar.MenuSubItem;
import com.missionse.tacsit.window.TacsitManager;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.util.WWIO;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.kml.KMLApplicationController;
import gov.nasa.worldwindx.examples.util.BalloonController;
import gov.nasa.worldwindx.examples.util.HotSpotController;
import javafx.stage.FileChooser;

public class KMLFileViewer {
	protected static KMLFileViewer viewer;
    protected HotSpotController hotSpotController;
    protected KMLApplicationController kmlAppController;
    protected BalloonController balloonController;

	public KMLFileViewer() {
		viewer = this;
		
		makeMenu();
		
        // Add a controller to handle input events on the layer selector and on browser balloons.
        this.hotSpotController = new HotSpotController(this.getWwd());

        // Add a controller to handle common KML application events.
        this.kmlAppController = new KMLApplicationController(this.getWwd());

        // Add a controller to display balloons when placemarks are clicked. We override the method addDocumentLayer
        // so that loading a KML document by clicking a KML balloon link displays an entry in the on-screen layer
        // tree.
        this.balloonController = new BalloonController(this.getWwd())
        {
            @Override
            protected void addDocumentLayer(KMLRoot document)
            {
                addKMLLayer(document);
            }
        };

        // Give the KML app controller a reference to the BalloonController so that the app controller can open
        // KML feature balloons when feature's are selected in the on-screen layer tree.
        this.kmlAppController.setBalloonController(balloonController);

	}
	
	private WorldWindow getWwd() {
		return TacsitManager.getInstance().getWorldWindowJPanel();
	}
	public static KMLFileViewer getViewer() {
		return viewer;
	}
	
	protected static void makeMenu()
	{
		final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("KML Loader");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("KML", "*.kml")
            );

		MenuBar menu = ArcticWindowManager.getMenu();
		MenuItemSelectionHandler handler = 	new MenuItemSelectionHandler() {

			@Override
			public void onMenuItemSelection(String parent, MenuSubItem item) {
				System.out.println("Menu selection: parent: " + parent + " item: " + item.getSubMenuItemName());
				if (item.getSubMenuItemName().equals("Load KML File")) {
					try
					{
						List<File> list = fileChooser.showOpenMultipleDialog(ArcticWindowManager.getStage());
						if (list != null) {
							for (File file : list) {

	                            new WorkerThread(file, getViewer()).start();
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

		MenuSubItem subMenu = new MenuSubItem("Load KML File", handler);
		menu.addMenuItem("Layers", subMenu);
	}

    protected void addKMLLayer(KMLRoot kmlRoot)
    {
        // Create a KMLController to adapt the KMLRoot to the World Wind renderable interface.
        KMLController kmlController = new KMLController(kmlRoot);

        // Adds a new layer containing the KMLRoot to the end of the WorldWindow's layer list. This
        // retrieves the layer name from the KMLRoot's DISPLAY_NAME field.
        RenderableLayer layer = new RenderableLayer();
        layer.setName((String) kmlRoot.getField(AVKey.DISPLAY_NAME));
        
        layer.addRenderable(kmlController);
        TacsitManager.getInstance().getWorldWindowJPanel().getModel().getLayers().add(layer);
    }
    
    protected static String formName(Object kmlSource, KMLRoot kmlRoot)
    {
        KMLAbstractFeature rootFeature = kmlRoot.getFeature();

        if (rootFeature != null && !WWUtil.isEmpty(rootFeature.getName()))
            return rootFeature.getName();

        if (kmlSource instanceof File)
            return ((File) kmlSource).getName();

        if (kmlSource instanceof URL)
            return ((URL) kmlSource).getPath();

        if (kmlSource instanceof String && WWIO.makeURL((String) kmlSource) != null)
            return WWIO.makeURL((String) kmlSource).getPath();

        return "KML Layer";
    }

    
    public static class WorkerThread extends Thread
    {
        protected Object kmlSource;
        protected KMLFileViewer viewer;

        public WorkerThread(Object kmlSource, KMLFileViewer viewer)
        {
            this.kmlSource = kmlSource;
            this.viewer = viewer;
        }

        public void run()
        {
            try
            {
                KMLRoot kmlRoot = this.parse();

                // Set the document's display name
                kmlRoot.setField(AVKey.DISPLAY_NAME, formName(this.kmlSource, kmlRoot));

                // Schedule a task on the EDT to add the parsed document to a layer
                final KMLRoot finalKMLRoot = kmlRoot;
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        viewer.addKMLLayer(finalKMLRoot);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        protected KMLRoot parse() throws IOException, XMLStreamException
        {
            return KMLRoot.createAndParse(this.kmlSource);
        }
    }

}