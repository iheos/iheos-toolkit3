package edu.tn.xds.metadata.editor.client.home;

import javax.inject.Inject;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;

import edu.tn.xds.metadata.editor.client.root.MetadataEditorAppView;

public class WelcomeActivity extends AbstractActivity {

	@Inject
	MetadataEditorAppView appView;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		HtmlLayoutContainer container = new WelcomePanel();
		appView.setCenterDisplay(container.asWidget());

	}

	@Override
	public String mayStop() {
		return null;
	}

}