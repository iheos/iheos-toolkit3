package edu.tn.xds.metadata.editor.client.generics.abstracts;

import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractMVP<M, V extends AbstractView<P>, P extends AbstractPresenter<V>> {

	private V view;
	private P presenter;

	// instance
	public AbstractMVP() {
	}

	// abstract
	public abstract V buildView();

	public abstract P buildPresenter();

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

	public void start() {
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
