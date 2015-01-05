package gov.nist.toolkit.xdstools3.client.tabs;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.Label;

/**
 * Defines the profile for creating a tab.
 * @author dazais
 * @see GenericCloseableTab
 *
 */
public interface TabInterface {
	
	public VLayout getContentsPanel();
	public void setHeader(String s);
	public Label createSubtitle1(String s);
	
}
