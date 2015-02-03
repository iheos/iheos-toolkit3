package gov.nist.hit.ds.docentryeditor.client.home;

import javax.inject.Inject;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import gov.nist.hit.ds.docentryeditor.client.root.MetadataEditorAppView;

public class WelcomeActivity extends AbstractActivity {

	@Inject
	MetadataEditorAppView appView;
	@Inject
	WelcomePanel welcomePanel;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		appView.setCenterDisplay(welcomePanel.asWidget());
	}

	@Override
	public String mayStop() {
		return null;
	}

}
