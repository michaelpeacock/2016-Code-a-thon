package com.missionse.core;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import gov.nasa.worldwind.WorldWindow;

public class Controller {
    private String appConfigurationLocation;
    private Registry registry = new Registry();
    
    public void start(String appConfigurationLocation)
            throws Exception
        {
            this.appConfigurationLocation = appConfigurationLocation;
            final AppConfiguration appConfig = new AppConfiguration();
            appConfig.initialize(this);

            appConfig.configure(this.appConfigurationLocation);

//            SwingUtilities.invokeLater(new Runnable()
//            {
//                public void run()
//                {
//                    redraw();
//                }
//            });
        }
    
    public WorldWindow getWWd()
    {
        return getWWPanel().getWWd();
    }

    public WWPanel getWWPanel()
    {
        return (WWPanel) getRegisteredObject(Constants.WW_PANEL);
    }

    public void redraw()
    {
        if (this.getWWd() != null)
            this.getWWd().redraw();
    }
    
    public Object getRegisteredObject(String objectID)
    {
        Object o = this.registry.getRegisteredObject(objectID);
        if (o == null)
            return null;

        if (!(o instanceof Class))
            return this.registry.getRegisteredObject(objectID);

        try
        {
            // Create on-demand objects
            Object newObj = this.createAndRegisterObject(objectID, o);
            if (newObj instanceof Initializable)
                ((Initializable) newObj).initialize(this);
            return newObj;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void registerObject(String objectID, Object o)
    {
        this.registry.registerObject(objectID, o);
    }

    public Object createAndRegisterObject(String objectID, Object className)
        throws IllegalAccessException, ClassNotFoundException, InstantiationException
    {
        return this.registry.createAndRegisterObject(objectID, className);
    }

    public Object createRegistryObject(String className)
        throws IllegalAccessException, ClassNotFoundException, InstantiationException
    {
        return this.registry.createRegistryObject(className);
    }


}
