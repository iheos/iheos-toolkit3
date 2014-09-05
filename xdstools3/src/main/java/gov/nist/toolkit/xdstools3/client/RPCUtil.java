package gov.nist.toolkit.xdstools3.client;

import com.google.gwt.core.client.GWT;
import gov.nist.toolkit.xdstools3.client.customWidgets.toolbar.ToolbarService;
import gov.nist.toolkit.xdstools3.client.customWidgets.toolbar.ToolbarServiceAsync;

/**
 * Utility class that retrieves an instance of the RPC Async Service interface
 */
public class RPCUtil {
    private static ToolbarServiceAsync RPCService = null;

    private RPCUtil(){
        RPCService = GWT.create(ToolbarService.class);
    }


        public static ToolbarServiceAsync getInstance(){
          if (RPCService == null) {
              new RPCUtil();
          }
            return RPCService;
     }

}
