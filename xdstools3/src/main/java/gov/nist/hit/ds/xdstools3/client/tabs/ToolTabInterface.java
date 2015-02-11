package gov.nist.hit.ds.xdstools3.client.tabs;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.Label;

/**
 * Defines the profile for creating a tab.
 * @author dazais
 * @see GenericCloseableTab
 *
 */
public interface ToolTabInterface {

	public VLayout getContentsPanel();
	public void setHeader(String s);
	public void removeHeaderTitle();
	public Label createSubtitle1(String s);

	public void createResultsPanel();
	public void setResultsPanel(Canvas canvas);
	public VLayout getResultsPanel();

	public void setHelpButton(Canvas container, String contents);
	public void hideHelpButton();
	public void setHelpWindowContents(String contents);
	public VLayout getHelpPanel();

	public String getTabName();
}
