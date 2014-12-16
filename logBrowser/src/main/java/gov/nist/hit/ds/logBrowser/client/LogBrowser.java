package gov.nist.hit.ds.logBrowser.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import gov.nist.hit.ds.repository.ui.client.widgets.LogBrowserWidget;
import gov.nist.hit.ds.repository.ui.client.widgets.LogBrowserWidget.Feature;

import java.util.logging.Logger;


public class LogBrowser implements EntryPoint {

	private static Logger logger = Logger.getLogger(LogBrowser.class.getName());	
    EventBus eventBus;

	public LogBrowser() {}
	
	/**
	 * This is the entry point method.
	 */	

	  public void onModuleLoad() {
		 logger.fine("Entering log browser main module");
		  
		 eventBus = new SimpleEventBus();
		 LogBrowserWidget logBrowserWidget = null;

          // Startup application mainly by initializing the widget
          try {


              // Look for any parameters on startup
              String reposSrc =  Window.Location.getParameter("reposSrc");
              String reposId = Window.Location.getParameter("reposId");
              String assetId = Window.Location.getParameter("assetId");


              if (reposSrc!=null && reposId!=null) {
                  logBrowserWidget = new LogBrowserWidget(eventBus, reposSrc, reposId, assetId);
              } else {
                  logBrowserWidget = new LogBrowserWidget(eventBus,
                          new Feature[] {
                                  Feature.BROWSE
                                  , Feature.SEARCH
//                                , Feature.TRANSACTION_MONITOR // Old
//                                , Feature.TRANSACTION_FILTER // Old
//                                , Feature.TRANSACTION_FILTER_ADVANCED
//                                  , Feature.EVENT_MESSAGE_AGGREGATOR // Independent widget test area
//                                ,  Feature.LOGGING_CONTROL  // Experimental, comment off all others above
                          });

              }

          } catch (Exception ex) {
              ex.printStackTrace();
              Window.alert("Could not initialize log browser widget: " + ex.toString());
          }


		 RootLayoutPanel.get().add(logBrowserWidget);
	  }
	  
}
