package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.toolkit.errorrecording.ErrorRecorder;
import gov.nist.toolkit.errorrecording.factories.ErrorRecorderBuilder;
import gov.nist.toolkit.xdsexception.ToolkitRuntimeException;


/**
 * Created by bill on 7/5/15.
 */
public class EventErrorRecorderBuilder  implements ErrorRecorderBuilder {

    // This is only used to build the initial ErrorRecorder. After that the
    // parent ErrorRecorder build its children and siblings
    @Override
    public EventErrorRecorder buildNewErrorRecorder() {   // only used by v2
        throw new ToolkitRuntimeException("Method must not be used in V3 toolkit");
    }

    @Override
    public ErrorRecorder buildNewErrorRecorder(Object o) {   // only used by v3
        if (o instanceof Event) {
            Event e = (Event) o;
            EventErrorRecorder rec = new EventErrorRecorder(e);
            rec.errorRecorderBuilder = this;
            return rec;
        }
        throw new ToolkitRuntimeException("EventErrorRecorderBuilder - called with object that is not of class Event");
    }

}
