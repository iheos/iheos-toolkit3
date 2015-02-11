package gov.nist.hit.ds.xdstools3.client.customWidgets.validationOutput;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * Holds the full validation output (validation result summary AND complete validation output).
 * @author dazais
 * @see ValidationSummaryCanvas
 *
 */
public class ValidationSummaryWidget extends SectionStack {

	public ValidationSummaryWidget() {
		setVisibilityMode(VisibilityMode.MULTIPLE);  
		setAnimateSections(true);  
		setOverflow(Overflow.HIDDEN);

		final ValidationSummaryCanvas loremipsum1 = new ValidationSummaryCanvas("loremipsum1"); 
		final ValidationSummaryCanvas loremipsum2 = new ValidationSummaryCanvas("loremipsum2"); 

		SectionStackSection middleSection = new SectionStackSection();  
		middleSection.setTitle("Validation result");  
		middleSection.setExpanded(true);  
		middleSection.setItems(loremipsum1);

		SectionStackSection lowerSection = new SectionStackSection();  
		lowerSection.setTitle("Validation output");  
		lowerSection.setExpanded(true);  
		lowerSection.setItems(loremipsum2);

		setSections( middleSection, lowerSection);  
	}

}
