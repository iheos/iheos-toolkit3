package gov.nist.hit.ds.repository.ui.client.event.asset;


import com.google.gwt.event.shared.GwtEvent;
import gov.nist.hit.ds.repository.shared.data.AssetNode;

/**
 * InContext refers to the context already provided within the log browser tree repository viewer.
 */
public class InContextAssetClickedEvent extends GwtEvent<InContextAssetClickedEventHandler> {



    public static final Type<InContextAssetClickedEventHandler> TYPE = new Type<InContextAssetClickedEventHandler>();

    private AssetNode an;

    private int rowNumber;

    public AssetNode getValue() {
        return this.an;
    }


    public InContextAssetClickedEvent(AssetNode value) {
        this.an = value;
    }

    public InContextAssetClickedEvent(AssetNode an, int rowNumber) {
        this.an = an;
        this.rowNumber = rowNumber;
    }

    @Override
    public Type<InContextAssetClickedEventHandler> getAssociatedType() {
        return TYPE;
    }


    @Override
    protected void dispatch(InContextAssetClickedEventHandler handler) {

        handler.onAssetClick(this);

    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

}



