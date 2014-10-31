package gov.nist.toolkit.xdstools3.client.customWidgets;


import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

/**
 * Creates a TextItem to hold a Patient ID, to be used in a form. The PID input is required.
 * @author dazais
 *
 */
public class PatientIDWidget extends DynamicForm {
//    private DynamicForm form;

    private String tooltip = "Ex.: 2729f2aca58d47f^^^&1.3.6.1.4.1.21367.2005.3.7&ISO";
    private boolean pidValueEntered = false; //monitors the state of the PID field (value is required).


    public PatientIDWidget(){

        GenericTextItemWithTooltipWidget pid = new GenericTextItemWithTooltipWidget();
        pid.setWidth(400);
        pid.setTitle("Patient ID *");
        pid.setName("patientID");
        pid.setRequired(true);
        pid.setTooltip(tooltip);

        // adds to form
//        form = new DynamicForm();
/*        form.*/setFields(pid);

        // adds to canvas
//        addMember(form);

        // add listeners
        // listen to changes in field value
  /*     form.*/addItemChangedHandler(new ItemChangedHandler() {

            public void onItemChanged(ItemChangedEvent event) {
                if (event.getNewValue() != null) pidValueEntered = true;

                // if yes, pass value to validation
//                                                    String newValue = (event.getNewValue() == null ? null : event.getNewValue().toString());
//                                                    disabledForm.getItem(FORM_FIELD_NAME).setValue(newValue);
            }
        });
    }


//    public DynamicForm getForm() {
//        return form;
//    }

    public boolean isPidValueEntered() {
        return pidValueEntered;
    }
}
