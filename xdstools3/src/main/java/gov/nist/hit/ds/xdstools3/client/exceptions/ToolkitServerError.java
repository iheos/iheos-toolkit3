package gov.nist.hit.ds.xdstools3.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by dazais on 12/17/2014.
 */
public class ToolkitServerError extends Exception  implements IsSerializable {

        private static final long serialVersionUID = 1L;

        public ToolkitServerError(String msg) {
            super(msg);
        }

        public ToolkitServerError() {}

    }
