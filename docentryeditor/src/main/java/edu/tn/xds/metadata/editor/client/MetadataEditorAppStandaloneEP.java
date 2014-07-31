package edu.tn.xds.metadata.editor.client;

/* Imports */

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.logging.Logger;

/**
 * This is the XDS Metadata Editor Application EntryPoint. That's the first
 * class loaded, which instantiate the different object global to the
 * application.
 */
public class MetadataEditorAppStandaloneEP implements EntryPoint {
    protected static Logger logger = Logger.getLogger(MetadataEditorAppStandaloneEP.class.getName());

    @SuppressWarnings("deprecation")
    @Override
    public void onModuleLoad() {
        RootPanel.get("editorAppContainer").add(new MetadataEditorApp());
        //        RootLayoutPanel.get().add(activityPanel);
        logger.info("Application Started!");
    }


}
