package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.smartgwt.client.data.*;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.DynamicForm;

/**
 * Contains the parameters common to all forms used to build the Endpoint Configuration Widget.
 *
 * Created by dazais on 3/4/2015.
 */
public class TransactionBasicForm extends DynamicForm {

    public TransactionBasicForm(DataSource ds){

        // Form display parameters
        setNumCols(3);
        setColWidths(210, "*", "*");
        setPadding(5);
        setIsGroup(true);
        setOverflow(Overflow.VISIBLE);

        // DataSource configuration
        setDataSource(ds);
    }


    // fetch related data
    public void fetchRelatedData(Criteria criteria) {
        fetchData(criteria, new DSCallback() {
            @Override
            public void execute(DSResponse dsResponse, Object data, DSRequest dsRequest) {
                Record record = new Record();

                if (dsResponse.getData()[0] != null) {
                    record = dsResponse.getData()[0];
                }
                // if record is empty, warn the user to contact the admin?
                    setValues(record.toMap());
            }
        });
    }

}
