package gov.nist.toolkit.xdstools2.client.tabs.simulatorControlTab;

import gov.nist.toolkit.xdstools2.client.adapter2v3.PopupMessageV3;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

class CreateButtonClickHandler implements ClickHandler {
	SimulatorControlTab simulatorControlTab;
	
	CreateButtonClickHandler(SimulatorControlTab simulatorControlTab) {
		this.simulatorControlTab = simulatorControlTab;
	}

	public void onClick(ClickEvent event) {
		int actorTypeIndex = simulatorControlTab.actorSelectListBox.getSelectedIndex();
		String actorTypeName = simulatorControlTab.actorSelectListBox.getItemText(actorTypeIndex);
		if (actorTypeName == null || actorTypeName.equals("")) {
			new PopupMessageV3("Select actor type first");
			return;
		}
		simulatorControlTab.getNewSimulator(actorTypeName);
	}
	
}
