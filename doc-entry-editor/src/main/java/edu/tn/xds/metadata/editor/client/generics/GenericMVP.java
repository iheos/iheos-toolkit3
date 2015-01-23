package edu.tn.xds.metadata.editor.client.generics;

import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractMVP;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractPresenter;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractView;


public class GenericMVP<M, V extends AbstractView<P>, P extends AbstractPresenter<V>> //
		extends AbstractMVP<M, V, P> {

	private final V view;
	private final P presenter;

	// instance
	public GenericMVP(V view, P presenter) {
		this.view = view;
		this.presenter = presenter;
	}

	@Override
	public P buildPresenter() {
		return presenter;
	}

	@Override
	public V buildView() {
		return view;
	}

}
