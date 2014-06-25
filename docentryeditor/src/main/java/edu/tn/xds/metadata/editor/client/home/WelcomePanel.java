package edu.tn.xds.metadata.editor.client.home;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;

public class WelcomePanel extends HtmlLayoutContainer {

	public WelcomePanel() {
		super("<br/><br/>" + "<div style='font-family:Helvetica;font-size:2em;font-weight:bold;color:#777777;text-align:center'>" + "Welcome !"
				+ "</div>");

		setBorders(false);
		setSize("100%", "100%");
	}

	@Override
	public Widget asWidget() {
		return this;
	}

}
