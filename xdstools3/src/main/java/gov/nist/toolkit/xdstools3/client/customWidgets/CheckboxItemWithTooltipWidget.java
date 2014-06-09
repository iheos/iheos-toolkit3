package gov.nist.toolkit.xdstools3.client.customWidgets;

import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;

public class CheckboxItemWithTooltipWidget extends CheckboxItem {
	private String tooltip;
	private FormItemIcon icon = new FormItemIcon(); 
	
	public CheckboxItemWithTooltipWidget(){
	     icon.setSrc("help.png");
	     icon.setPrompt("");
	     setIcons(icon);
	}
	
	public void setTooltip(String tip){
		tooltip = tip;
		icon.setPrompt(tooltip);
	}
	
}
