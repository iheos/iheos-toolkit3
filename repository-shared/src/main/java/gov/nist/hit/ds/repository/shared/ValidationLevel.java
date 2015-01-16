package gov.nist.hit.ds.repository.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by bmajur on 1/14/15.
 */
public enum ValidationLevel implements IsSerializable {
    INFO {
        @Override
        public String toString() { return "Info"; }
    },
    WARNING {
        @Override
        public String toString() { return "Warning"; }
    },
    ERROR {
        @Override
        public String toString() { return "Error"; }
    }
}
