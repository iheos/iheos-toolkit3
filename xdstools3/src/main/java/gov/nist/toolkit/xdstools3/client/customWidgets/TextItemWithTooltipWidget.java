package gov.nist.toolkit.xdstools3.client.customWidgets;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * Creates a TextItem with a questionmark icon that displays a tooltip when hovered.
 * @see PatientIDWidget as an example of use.
 * @author dazais
 *
 */
public class TextItemWithTooltipWidget extends TextItem {
	
	private String tooltip;
	private FormItemIcon icon = new FormItemIcon(); 
	
	public TextItemWithTooltipWidget(){
	     icon.setSrc("help.png");
	     icon.setPrompt("");
	     setIcons(icon);  
	}
	
	public void setTooltip(String tip){
		tooltip = tip;
		icon.setPrompt(tooltip);
	}
	

}
