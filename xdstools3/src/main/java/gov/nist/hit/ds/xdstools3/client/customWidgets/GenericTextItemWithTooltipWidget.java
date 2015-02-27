package gov.nist.hit.ds.xdstools3.client.customWidgets;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * Creates a TextItem with a questionmark icon that displays a tooltip when hovered.
 * @see PatientIDWidget as an example of use.
 * @author dazais
 *
 */
public class GenericTextItemWithTooltipWidget extends TextItem {
	
	private String tooltip;
	private FormItemIcon icon = new FormItemIcon(); 
	
	public GenericTextItemWithTooltipWidget(){
	     icon.setSrc("icons/glyphicons/glyphicons_194_circle_question_mark.png");
         icon.setBaseStyle("glyphicon");
	     icon.setPrompt("");
	     setIcons(icon);  
	}
	
	public void setTooltip(String tip){
		tooltip = tip;
		icon.setPrompt(tooltip);
	}

}
