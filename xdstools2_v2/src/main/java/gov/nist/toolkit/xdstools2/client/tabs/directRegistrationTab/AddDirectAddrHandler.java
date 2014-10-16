package gov.nist.toolkit.xdstools2.client.tabs.directRegistrationTab;

import gov.nist.toolkit.directsim.client.ContactRegistrationData;
import gov.nist.toolkit.xdstools2.client.adapter2v3.PopupMessageV3;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddDirectAddrHandler implements ChangeHandler, ClickHandler {
	DirectRegistrationTab tab;
	boolean newDirect;
	
	public AddDirectAddrHandler(DirectRegistrationTab tab, boolean newDirect) {
		this.tab = tab;
		this.newDirect = newDirect;
	}
	
	
	  
	public void onChange(ChangeEvent arg0) {
		String direct = tab.addDirectFrom.getText();
		byte[] cert = tab.cert.getText().getBytes();
		if (direct != null && !direct.equals("")) {
			tab.currentRegistration.directToCertMap.put(direct, cert);
		} 
//		else if (tab.currentDirectForDeletion() != null) {
//			tab.currentRegistration.directToCertMap.put(tab.currentDirectForDeletion(), cert);
//		} 
		else {
			new PopupMessageV3("Must enter Direct (From) address.");
			return;
		}
		tab.toolkitService.contactRegistration(tab.currentRegistration, registrationCallback);
	}
	
	AsyncCallback<ContactRegistrationData> registrationCallback = new AsyncCallback<ContactRegistrationData> () {

		  
		public void onFailure(Throwable arg0) {
			new PopupMessageV3("Error: " + arg0.getMessage());
		}

		  
		public void onSuccess(ContactRegistrationData arg0) {
			if (newDirect)
				tab.directFromMessage("Added");
			else
				tab.directFromMessage("Saved");
			tab.currentRegistration = arg0;
			tab.refreshContact();
		}
		
	};

	  
	public void onClick(ClickEvent arg0) {
//		String direct = tab.currentDirectForUpdating();
//		byte[] cert = tab.cert.getText().getBytes();
//		if (direct != null && !direct.equals("")) {
//			tab.currentRegistration.directToCertMap.put(direct, cert);
//		} else if (tab.currentDirectForUpdating() != null) {
//			tab.currentRegistration.directToCertMap.put(tab.currentDirectForUpdating(), cert);
//		}
//		tab.toolkitService.contactRegistration(tab.currentRegistration, registrationCallback);
	}

}
