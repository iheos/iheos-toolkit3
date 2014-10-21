package gov.nist.toolkit.xdstools2.client.adapter2v3;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.KeyDownEvent;
import com.smartgwt.client.widgets.events.KeyDownHandler;

public class PopupMessageV3 extends Dialog {

	public PopupMessageV3(String message) {
        super();

		// Set the dialog box's caption.
        Label lab =  new Label(message);
        addItem(lab);
        lab.setLayoutAlign(Alignment.CENTER);
        //lab.setAutoFit(true);
        lab.setWidth(300);

        // OK button
		final Button ok = new Button("OK");

        // Close the dialog with a click on the OK button
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
                   clear();
			}
		});
        // Close the dialog when pressing Enter or Escape
        ok.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (EventHandler.getKey().equals("Escape") || EventHandler.getKey().equals("Enter")){
                    clear();
                }
            }
        });
		addItem(ok);
		ok.setLayoutAlign(Alignment.CENTER);

        // Set the focus on the button (has to be set only after drawing the widget)
        ok.focus();

        // popup attributes
        setTitle("Error");
        setAutoSize(true);
        setMinWidth(350);

        draw();
	}


// TODO this is legacy code, not updated yet
//	public PopupMessageV3(AssertionResults result) {
//        super();
//
//        VStack panel = new VStack();
//		addItem(panel);
//		for (AssertionResult ar : result.assertions) {
//			if (ar.status) {
//                panel.addMember(new HTMLFlow(ar.assertion));
//			} else {
//				panel.addMember(new HTMLFlow("<font color=\"#FF0000\">" + ar.assertion + "</font>"));
//			}
//		}
//
//		Button ok = new Button("OK");
//		ok.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				removeFromParent();
//			}
//		});
//		panel.addMember(ok);
//        setAlign(Alignment.CENTER);
//		draw();
//	}

}

