package edu.tn.xds.metadata.editor.client.widgets;

import com.google.gwt.dom.client.InputElement;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * This class is an extend of Textfield with an limit of input character.
 *
 */
public class BoundedTextField extends TextField {

	public BoundedTextField() {
		super();
	}

	public void setMaxLength(int length) {
		InputElement ie = this.getInputEl().cast();
		ie.setMaxLength(length);
	}
}
