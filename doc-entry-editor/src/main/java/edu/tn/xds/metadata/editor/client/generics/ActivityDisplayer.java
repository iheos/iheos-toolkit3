package edu.tn.xds.metadata.editor.client.generics;

import javax.inject.Inject;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.tn.xds.metadata.editor.client.root.MetadataEditorAppView;

/**
 * Displayer interface to load widget into an Activity container.
 */
public interface ActivityDisplayer {
	public void display(Widget w, AcceptsOneWidget p, EventBus b);

    /**
     * ActivityDisplayer implementations. This Class enables to inject a widget (or a view)
     * into our application main container.
     */
	public class MetadataEditorAppDisplayer implements ActivityDisplayer {
		@Inject
		MetadataEditorAppView appView;

		@Override
		public void display(Widget w, AcceptsOneWidget p, EventBus b) {
			appView.setCenterDisplay(w);
		}
	}
}
