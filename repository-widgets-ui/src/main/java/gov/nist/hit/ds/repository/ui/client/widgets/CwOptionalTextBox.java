package gov.nist.hit.ds.repository.ui.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CwOptionalTextBox extends Composite implements
     ClickHandler {

   private TextBox textBox = new TextBox();
   private CheckBox checkBox = new CheckBox();

   /**
    * Constructs an CwOptionalTextBox with the given caption on the check.
    * 
    * @param caption the caption to be displayed with the check box
    */
   public CwOptionalTextBox(String caption) {
     // Place the check above the text box using a vertical panel.
     VerticalPanel panel = new VerticalPanel();
     panel.add(checkBox);
     panel.add(textBox);

     // Set the check box's caption, and check it by default.
     checkBox.setText(caption);
     // checkBox.setChecked(true);
     checkBox.addClickHandler(this);

     // All composites must call initWidget() in their constructors.
     initWidget(panel);

     // Give the overall composite a style name.
     setStyleName("example-OptionalCheckBox");
   }

   public void onClick(ClickEvent event) {
     if (event.getSource() == checkBox) {
       // When the check box is clicked, update the text box's enabled state.
       // textBox.setEnabled(checkBox.isChecked());
     }
   }

   /**
    * Sets the caption associated with the check box.
    * 
    * @param caption the check box's caption
    */
   public void setCaption(String caption) {
     // Note how we use the use composition of the contained widgets to provide
     // only the methods that we want to.
     checkBox.setText(caption);
   }

   /**
    * Gets the caption associated with the check box.
    * 
    * @return the check box's caption
    */
   public String getCaption() {
     return checkBox.getText();
   }
 
}
