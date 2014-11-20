package gov.nist.toolkit.xdstools2.client.tabs.directSenderTab;

import gov.nist.toolkit.xdstools2.client.adapter2v3.PopupMessageV3;
import gov.nist.toolkit.xdstools2.client.Toolkit2ServiceAsync;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class LoadTestdataList {
	Toolkit2ServiceAsync toolkit;
	String listName;
	DirectSenderTab.Display display;

	public LoadTestdataList(Toolkit2ServiceAsync toolkit, String listName, DirectSenderTab.Display display) {
		this.toolkit = toolkit;
		this.listName = listName;
		this.display = display;
	}

	public void run() {
		toolkit.getTestdataSetListing(listName, loadCallback);
	}

	AsyncCallback<List<String>> loadCallback = new AsyncCallback<List<String>>() {

		  
		public void onFailure(Throwable arg0) {
			new PopupMessageV3("Error: " + arg0.getMessage());
		}

		  
		public void onSuccess(List<String> arg0) {
			display.setMessageSelections(arg0);
	}};
}
