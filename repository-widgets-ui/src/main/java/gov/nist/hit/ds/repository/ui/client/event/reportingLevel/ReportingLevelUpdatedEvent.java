package gov.nist.hit.ds.repository.ui.client.event.reportingLevel;


import com.google.gwt.event.shared.GwtEvent;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.ui.client.event.asset.InContextAssetClickedEventHandler;

/**
 * InContext refers to the context already provided within the log browser tree repository viewer.
 */
public class ReportingLevelUpdatedEvent extends GwtEvent<ReportingLevelUpdatedEventHandler> {



    public static final Type<ReportingLevelUpdatedEventHandler> TYPE = new Type<ReportingLevelUpdatedEventHandler>();


    private String[] level;

    public ReportingLevelUpdatedEvent(String[] level) {
        this.level = level;
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

    public String[] getLevel() {
        return level;
    }

    public void setLevel(String[] level) {
        this.level = level;
    }

}



