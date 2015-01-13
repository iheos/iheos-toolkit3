package edu.tn.xds.metadata.editor.client.generics.abstracts;

import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract class to implement a class full GWT MVP architecture.
 * @param <M> Model class
 * @param <V> View class
 * @param <P> Presenter class
 */
public abstract class AbstractMVP<M, V extends AbstractView<P>, P extends AbstractPresenter<V>> {

	private V view;
	private P presenter;

	// instance
	public AbstractMVP() {
	}

    /**
     * Abstract method that builds the view for MVP and returns its instance.
     * @return View instance for MVP
     */
	public abstract V buildView();

    /**
     * Abstract method that builds the presenter for MVP and returns its instance.
     * @return Presenter instance for MVP
     */
	public abstract P buildPresenter();

    /**
     * Method that initialize the MVP. Must be called with MVP class is instantiated.
     */
    // impl
	public void init() {
		// build
		view = buildView();
		presenter = buildPresenter();

		// init
		presenter.setView(view);
		view.setPresenter(presenter);

		presenter.init();
		view.init();

		// start
		start();
	}

	private void start() {
		presenter.start();
		view.start();
	}

	public Widget getDisplay() {
		return view.asWidget();
	}

	// getter / setter
	public V getView() {
		return view;
	}

	public void setView(V view) {
		this.view = view;
	}

	public P getPresenter() {
		return presenter;
	}

	public void setPresenter(P presenter) {
		this.presenter = presenter;
	}

}
