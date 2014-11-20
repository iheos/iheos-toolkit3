package gov.nist.toolkit.xdstools2.client.tabs.directRegistrationTab;

import gov.nist.toolkit.directsim.client.ContactRegistrationData;
import gov.nist.toolkit.xdstools2.client.adapter2v3.PopupMessageV3;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContinueHandler implements ClickHandler {
	DirectRegistrationTab tab;
	
	public ContinueHandler(DirectRegistrationTab tab) {
		this.tab = tab;
	}
	
	  
	public void onClick(ClickEvent arg0) {
		ContactRegistrationData data = null;
		try {
			data = tab.registrationData();
		} catch (Exception e) {
			new PopupMessageV3(e.getMessage());
			return;
		}
		tab.toolkitService.contactRegistration(data, registrationCallback);
	}
	
	AsyncCallback<ContactRegistrationData> registrationCallback = new AsyncCallback<ContactRegistrationData> () {

		  
		public void onFailure(Throwable arg0) {
			new PopupMessageV3("Error: " + arg0.getMessage());
		}

		  
		public void onSuccess(ContactRegistrationData arg0) {
			tab.contactMessage("Created");
		}
		
	};

}
