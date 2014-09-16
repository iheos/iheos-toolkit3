package gov.nist.hit.ds.logBrowser.client.event.asset;


import com.google.gwt.event.shared.GwtEvent;
import gov.nist.hit.ds.repository.shared.AssetNode;

/**
 * Out of context refers to a mode where a repository tree is not immediately available.
 */

public class OutOfContextAssetClickedEvent extends GwtEvent<OutOfContextAssetClickedEventHandler> {



    public static final Type<OutOfContextAssetClickedEventHandler> TYPE = new Type<OutOfContextAssetClickedEventHandler>();

    private AssetNode an;

    private int rowNumber;



    public OutOfContextAssetClickedEvent(AssetNode value) {
        this.an = value;
    }

    public OutOfContextAssetClickedEvent(AssetNode an, int rowNumber) {
        this.an = an;
        this.rowNumber = rowNumber;
    }

    public AssetNode getValue() {
        return this.an;
    }


    @Override
    public Type<OutOfContextAssetClickedEventHandler> getAssociatedType() {
        return TYPE;
    }


    @Override
    protected void dispatch(OutOfContextAssetClickedEventHandler handler) {

        handler.onAssetClick(this);

    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

}




