package gov.nist.toolkit.xdstools3.client.customWidgets;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.UploadItem;

public class UploadItemWithTooltipWidget extends UploadItem {
	
	private String tooltip;
	private FormItemIcon icon = new FormItemIcon(); 
	
	public UploadItemWithTooltipWidget(){
	     icon.setSrc("help.png");
	     icon.setPrompt("");
	     setIcons(icon);  
	}
	
	public void setTooltip(String tip){
		tooltip = tip;
		icon.setPrompt(tooltip);
	}
	

}
