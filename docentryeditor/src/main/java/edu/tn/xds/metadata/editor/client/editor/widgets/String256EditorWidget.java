package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import edu.tn.xds.metadata.editor.shared.model.String256;

/**
 * <p>
 * <b>This class represents the widget which matches String256 model type</b>
 * </p>
 */
public class String256EditorWidget extends Composite implements Editor<String256> {

	private final VerticalLayoutContainer vcontainer = new VerticalLayoutContainer();
	BoundedTextField string = new BoundedTextField();
    int initWindowWidth;

	public String256EditorWidget() {
		initWidget(vcontainer);
		// string.setWidth(400);
        initWindowWidth=Window.getClientWidth();

		string.setWidth("auto");
		string.setMaxLength(256);
		vcontainer.add(string, new VerticalLayoutData(1, -1));
	}

	/**
	 * This method sets the default text to display in an empty field (defaults
	 * to null). It is done to help and guide the user during his input.
	 *
	 * @param str
	 *            Default text displayed in an empty field
	 */
	public void setEmptyText(String str) {
		string.setEmptyText(str);
	}

	/**
	 * Sets the widget's tool tip with the given config
	 *
	 * @param config
	 */
	public void setToolTipConfig(ToolTipConfig config) {
		string.setToolTipConfig(config);
	}

	/**
	 * Sets whether a field is valid when its value length = 0 (default to
	 * true). This will warn the user through the editor widget if he didn't
	 * input anything in field which does not allow blank.
	 *
	 * @param allowBlank
	 *            true to allow blanks, false otherwise
	 */
	public void setAllowBlank(boolean allowBlank) {
		string.setAllowBlank(allowBlank);
	}

    public void addValidator(Validator fieldValidator){string.addValidator(fieldValidator);}
}
