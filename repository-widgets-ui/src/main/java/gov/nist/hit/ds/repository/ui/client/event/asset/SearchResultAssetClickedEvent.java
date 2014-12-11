package gov.nist.hit.ds.repository.ui.client.event.asset;


import com.google.gwt.event.shared.GwtEvent;
import gov.nist.hit.ds.repository.shared.data.AssetNode;

/**
 * Search result asset refers to the items returned from a search result.
 */
public class SearchResultAssetClickedEvent extends GwtEvent<SearchResultAssetClickedEventHandler> {


    public static final Type<SearchResultAssetClickedEventHandler> TYPE = new Type<SearchResultAssetClickedEventHandler>();

    private AssetNode an;

    private int rowNumber;


    public SearchResultAssetClickedEvent(AssetNode value) {
        this.an = value;
    }

    public SearchResultAssetClickedEvent(AssetNode an, int rowNumber) {
        this.an = an;
        this.rowNumber = rowNumber;
    }

    public AssetNode getValue() {
        return this.an;
    }


    @Override
    public Type<SearchResultAssetClickedEventHandler> getAssociatedType() {
        return TYPE;
    }


    @Override
    protected void dispatch(SearchResultAssetClickedEventHandler handler) {

        handler.onAssetClick(this);

    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

}



