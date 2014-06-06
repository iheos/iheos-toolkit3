package edu.tn.xds.metadata.editor.client.home;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;

public class WelcomePanel extends HtmlLayoutContainer {

	public WelcomePanel() {
		super("<br/><br/>" + "<div style='font-family:Helvetica;font-size:2em;font-weight:bold;color:#777777;text-align:center'>" + "Welcome !"
				+ "</div>");

		// TEST MENU //

		// Menu actionMenu = new Menu();
		// MenuItem e = new MenuItem("Enable");
		// MenuItem d = new MenuItem("Disable");
		// actionMenu.add(e);
		// actionMenu.add(d);
		//
		// TextButton actionsBtn = new TextButton("Actions");
		// actionsBtn.setMenu(actionMenu);
		//
		// this.add(actionsBtn);

		// FIN TEST //

		setBorders(false);
		setSize("100%", "100%");
	}

	@Override
	public Widget asWidget() {
		return this;
	}

}