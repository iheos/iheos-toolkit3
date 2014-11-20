package gov.nist.toolkit.xdstools2.client.tabs.directStatusTab;

import gov.nist.direct.client.MessageLog;
import gov.nist.toolkit.xdstools2.client.adapter2v3.PopupMessageV3;
import gov.nist.toolkit.xdstools2.client.Toolkit2ServiceAsync;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class MessageLoader  {
	MessageStatusView view;
	Toolkit2ServiceAsync toolkitService;
	
	public MessageLoader(Toolkit2ServiceAsync toolkitService, MessageStatusView view) {
		this.view = view;
		this.toolkitService = toolkitService;
	}
	
	public void run(String username) {
		toolkitService.getDirectOutgoingMsgStatus(username, new AsyncCallback<List<MessageLog>> () {

			  
			public void onFailure(Throwable caught) {
				new PopupMessageV3(caught.getMessage());
			}

			  
			public void onSuccess(List<MessageLog> result) {
				view.build(result);
				
				for (MessageLog s : result) {
					view.addRow(s);
				}
			}
			
		}); 
	}

}
