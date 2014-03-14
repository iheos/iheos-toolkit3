package gov.nist.toolkit.xdstools3.client.customWidgets;



/**
 * Creates a TextItem to hold a Patient ID, to be used in a form. The PID input is required.
 * @author dazais
 *
 */
public class PatientIDWidget extends TextItemWithTooltipWidget {
	
	private String tooltip = "Ex.: 2729f2aca58d47f^^^&1.3.6.1.4.1.21367.2005.3.7&ISO";
	
	
	public PatientIDWidget(){
	     setWidth(400);
	     setTitle("Patient ID *");  
	     setRequired(true); 
	     setTooltip(tooltip);
	}
	
	

}
