package gov.nist.hit.ds.repository.ui.client.event.reportingLevel;


import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

/**
 * InContext refers to the context already provided within the log browser tree repository viewer.
 */
public class ReportingLevelUpdatedEvent extends GwtEvent<ReportingLevelUpdatedEventHandler> {



    public static final Type<ReportingLevelUpdatedEventHandler> TYPE = new Type<ReportingLevelUpdatedEventHandler>();


    private List<String> statusCodes;

    public ReportingLevelUpdatedEvent(List<String> statusCodes) {
        this.statusCodes = statusCodes;
    }

    @Override
    public Type<ReportingLevelUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Should only be called by {@link HandlerManager}. In other words, do not use
     * or call.
     *
     * @param handler handler
     */
    @Override
    protected void dispatch(ReportingLevelUpdatedEventHandler handler) {
        handler.onUpdate(this);
    }

    public List<String> getStatusCodes() {
        return statusCodes;
    }

    public void setStatusCodes(List<String> statusCodes) {
        this.statusCodes = statusCodes;
    }
}



