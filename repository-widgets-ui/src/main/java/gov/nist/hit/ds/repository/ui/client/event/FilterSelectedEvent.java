package gov.nist.hit.ds.repository.ui.client.event;


import com.google.gwt.event.shared.GwtEvent;


public class FilterSelectedEvent extends GwtEvent<FilterSelectedEventHandler>{
	public static final Type<FilterSelectedEventHandler> TYPE = new Type<FilterSelectedEventHandler>();



    private String filterLocation;

	  public FilterSelectedEvent(String filterLocation) {

          setFilterLocation(filterLocation);
	  }


	@Override
	public Type<FilterSelectedEventHandler> getAssociatedType() {
	    return TYPE;
	}

	@Override
	protected void dispatch(FilterSelectedEventHandler handler) {
		
		handler.onFilterSelected(this);
		
	}


    public String getFilterLocation() {
        return filterLocation;
    }

    public void setFilterLocation(String filterLocation) {
        this.filterLocation = filterLocation;
    }

}




