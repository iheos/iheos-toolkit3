package edu.tn.xds.metadata.editor.client.generics.abstracts;

import com.google.web.bindery.event.shared.EventBus;

import javax.inject.Inject;
import java.util.logging.Logger;

public abstract class AbstractPresenter<V extends AbstractView<?>> {

	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	@Inject
	protected EventBus eventBus;

	protected V view;

	// instance
	@Inject
	public AbstractPresenter() {
	}

	@Inject
	public AbstractPresenter(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	// abstract
	/**
     * called by mvp before view.init() does not necessarily need to do something
     */
    public abstract void init();

	/**
	 * called by mvp just before view.getDisplay()
	 */
	public void start() {
	};

	// getter / setter
	public V getView() {
		return view;
	}

	public void setView(V view) {
		this.view = view;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	// public abstract RequestContext getRequestContext();

}
