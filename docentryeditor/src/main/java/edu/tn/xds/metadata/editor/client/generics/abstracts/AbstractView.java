package edu.tn.xds.metadata.editor.client.generics.abstracts;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractView<P extends AbstractPresenter<?>> //
		implements IsWidget {

	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	protected P presenter;
	protected Widget ui;

	// instance
	public AbstractView() {
	}

	public void init() {
		ui = buildUI();
		bindUI();
	}

	public void start() {
	}

	// abstract
	abstract protected Widget buildUI();

	abstract protected void bindUI();

	// impl
	@Override
	public Widget asWidget() {
		if (ui == null)
			init();
		return ui;
	}

	// getter / setter
	public P getPresenter() {
		return presenter;
	}

	public void setPresenter(P presenter) {
		this.presenter = presenter;
	}
}
