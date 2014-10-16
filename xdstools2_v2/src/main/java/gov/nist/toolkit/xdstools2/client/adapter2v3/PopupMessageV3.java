package gov.nist.toolkit.xdstools2.client.adapter2v3;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.KeyDownEvent;
import com.smartgwt.client.widgets.events.KeyDownHandler;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.results.client.AssertionResult;
import gov.nist.toolkit.results.client.AssertionResults;

public class PopupMessageV3 extends Dialog {

	public PopupMessageV3(String message) {
        super();

		// Set the dialog box's caption.
        Label lab =  new Label(message);
        addItem(lab);
        lab.setLayoutAlign(Alignment.CENTER);
        lab.setAutoFit(true);

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

        // popup attributes
        setTitle("Error");
        setPadding(5);
        setPaddingAsLayoutMargin(true);
        draw();

        // Set the focus on the button (has to be set only after drawing the widget)
        ok.focus();
	}

	public PopupMessageV3(AssertionResults result) {
        super();

        VStack panel = new VStack();
		addItem(panel);
		for (AssertionResult ar : result.assertions) {
			if (ar.status) {
                panel.addMember(new HTMLFlow(ar.assertion));
			} else {
				panel.addMember(new HTMLFlow("<font color=\"#FF0000\">" + ar.assertion + "</font>"));
			}
		}
		// DialogBox is a SimplePanel, so you have to set its widget property to
		// whatever you want its contents to be.
		Button ok = new Button("OK");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeFromParent();
			}
		});
		panel.addMember(ok);
        setAlign(Alignment.CENTER);

		draw();
	}

}

