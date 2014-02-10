package gov.nist.hit.ds.logBrowser.client;


import gov.nist.hit.ds.logBrowser.client.widgets.LogBrowserWidget;
import gov.nist.hit.ds.logBrowser.client.widgets.LogBrowserWidget.Feature;
import gov.nist.hit.ds.repository.simple.search.client.exception.RepositoryConfigException;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;


public class LogBrowser implements EntryPoint {

	private static Logger logger = Logger.getLogger(LogBrowser.class.getName());	
    SimpleEventBus eventBus;

	public LogBrowser() {}
	
	/**
	 * This is the entry point method.
	 */	

	  public void onModuleLoad() {
		 logger.fine("Entering log browser main module");
		  
		 eventBus = new SimpleEventBus();		  
		 LogBrowserWidget logBrowserWidget = null;
					
			try {
				logBrowserWidget = new LogBrowserWidget(eventBus, new Feature[]{Feature.BROWSE, Feature.SEARCH});
			} catch (RepositoryConfigException e) {
				Window.alert("Could not initialize log browser widget: " + e.toString());
			}			
		 
		 RootLayoutPanel.get().add(logBrowserWidget);
	  }
	  
}
