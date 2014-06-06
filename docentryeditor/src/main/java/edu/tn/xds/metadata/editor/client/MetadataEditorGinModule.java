package edu.tn.xds.metadata.editor.client;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.generics.ActivityDisplayer;
import edu.tn.xds.metadata.editor.client.generics.ActivityDisplayer.MetadataEditorAppDisplayer;
import edu.tn.xds.metadata.editor.client.root.MetadataEditorAppView;

public class MetadataEditorGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(com.google.web.bindery.event.shared.EventBus.class).to(MetadataEditorEventBus.class);
		bind(com.google.gwt.event.shared.EventBus.class).to(MetadataEditorEventBus.class);
		bind(MetadataEditorEventBus.class).in(Singleton.class);

		bind(com.google.web.bindery.requestfactory.shared.RequestFactory.class).to(MetadataEditorRequestFactory.class);

		bind(com.google.gwt.place.shared.PlaceController.class).toProvider(PlaceControllerProvider.class).in(Singleton.class);

		bind(ActivityDisplayer.class).to(MetadataEditorAppDisplayer.class).in(Singleton.class);
		bind(MetadataEditorAppView.class).in(Singleton.class);
	}

	// Provider for PlaceController
	public static class PlaceControllerProvider implements Provider<PlaceController> {
		@Inject
		MetadataEditorEventBus eventBus;
		private PlaceController controller;

		@SuppressWarnings("deprecation")
		@Override
		public PlaceController get() {
			if (controller == null) {
				controller = new PlaceController(eventBus);
			}
			return controller;
		}
	}

	// Provider de MetadataEditorRequestFactory
	@Provides
	MetadataEditorRequestFactory provideMetadataEditorRequestFactory() {
		MetadataEditorRequestFactory requestFactory = GWT.create(MetadataEditorRequestFactory.class);
		requestFactory.initialize(MetadataEditorGinjector.instance.getEventBus());
		return requestFactory;
	}
}
