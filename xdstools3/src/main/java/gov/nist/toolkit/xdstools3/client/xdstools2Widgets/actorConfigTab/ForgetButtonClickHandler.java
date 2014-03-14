package gov.nist.toolkit.xdstools3.client.xdstools2Widgets.actorConfigTab;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

class ForgetButtonClickHandler implements ClickHandler {

	/**
	 * 
	 */
	private ActorConfigTab actorConfigTab;

	/**
	 * @param actorConfigTab
	 */
	ForgetButtonClickHandler(ActorConfigTab actorConfigTab) {
		this.actorConfigTab = actorConfigTab;
	}

	public void onClick(ClickEvent event) {
		this.actorConfigTab.currentEditSite.changed = false;
		this.actorConfigTab.newActorEditGrid();
	}

}