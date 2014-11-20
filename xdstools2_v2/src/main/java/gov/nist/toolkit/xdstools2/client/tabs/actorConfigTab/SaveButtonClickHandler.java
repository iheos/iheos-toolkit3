package gov.nist.toolkit.xdstools2.client.tabs.actorConfigTab;

import gov.nist.toolkit.xdstools2.client.AdminPasswordDialogBox;
import gov.nist.toolkit.xdstools2.client.PasswordManagement;
import gov.nist.toolkit.xdstools2.client.adapter2v3.PopupMessageV3;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

class SaveButtonClickHandler implements ClickHandler {
	/**
	 * 
	 */
	private ActorConfigTab actorConfigTab;
	
	public SaveButtonClickHandler(ActorConfigTab actorConfigTab) {
		this.actorConfigTab = actorConfigTab;
	}
	
	public void onClick(ClickEvent event) {
		if (actorConfigTab.currentEditSite.getName().equals(actorConfigTab.newSiteName)) {
			new PopupMessageV3("You must give site a real name before saving");
			return;
		}

		if (PasswordManagement.isSignedIn) {
			actorConfigTab.saveSignedInCallback.onSuccess(true);
		}
		else {
			PasswordManagement.addSignInCallback(actorConfigTab.saveSignedInCallback);

			new AdminPasswordDialogBox(actorConfigTab.topPanel);
		}

	}

}