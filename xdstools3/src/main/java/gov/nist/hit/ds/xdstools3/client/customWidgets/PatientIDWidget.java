package gov.nist.hit.ds.xdstools3.client.customWidgets;


import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;


/**
 * Creates a TextItem to hold a Patient ID, to be used in a form. The PID input is required.
 * @author dazais
 *
 */
public class PatientIDWidget extends DynamicForm {

    private String tooltip = "Ex.: 2729f2aca58d47f^^^&1.3.6.1.4.1.21367.2005.3.7&ISO";
    private boolean pidValueEntered = false; //monitors the state of the PID field (value is required).
    private GenericTextItemWithTooltipWidget pid;


    public PatientIDWidget(){

        pid = new GenericTextItemWithTooltipWidget();
        pid.setWidth(400);
        pid.setTitle("Patient ID *");
        pid.setName("patientID");
        pid.setRequired(true);
        pid.setTooltip(tooltip);

        setFields(pid);

        this.addItemChangedHandler(new ItemChangedHandler() {
            @Override
            public void onItemChanged(ItemChangedEvent itemChangedEvent) {
                if (itemChangedEvent.getNewValue() != null) {
                    pidValueEntered = true;
                } else { pidValueEntered = false; }
            }
        });

    }

    /** Indicates whether the PID field is empty or not
     * */
    public boolean isPidValueEntered() {
        return pidValueEntered;
    }

    /** Returns the value of the text field PID
     * **/
    public String getValue(){
        return pid.getValueAsString();
    }
}
