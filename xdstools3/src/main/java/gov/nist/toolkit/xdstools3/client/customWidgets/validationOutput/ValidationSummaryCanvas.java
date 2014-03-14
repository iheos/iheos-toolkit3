package gov.nist.toolkit.xdstools3.client.customWidgets.validationOutput;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;

public class ValidationSummaryCanvas extends Canvas {
	
        private String contents = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt "
        				+ "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
        				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu "
        				+ "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit "
        				+ "anim id est laborum."; 
  
        public ValidationSummaryCanvas(String id) {  
            setID(id);  
            setPadding(10);  
            setOverflow(Overflow.AUTO);  
            setContents(contents);  
        }  

}
