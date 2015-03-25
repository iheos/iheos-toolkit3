package gov.nist.hit.ds.xdstools3.client.customWidgets;

import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

import java.util.LinkedHashMap;

/**
 * Widget to select the reporting level of the log browser.
 */
public class ReportingLevelSelectWidget extends SelectItem {

    private String selectedReportingLevel;

    public ReportingLevelSelectWidget(){
        setTitle("Select log level");
        setType("comboBox");
        setName("reportingLevelItem");
        setValueMap(loadLogLevels());
        setDefaultValue("errors");
        selectedReportingLevel="errors";
    }

    private LinkedHashMap<String,String> loadLogLevels() {
        LinkedHashMap<String,String> logLevels = new LinkedHashMap<String,String>();
        logLevels.put("all","All");
        logLevels.put("errors","Errors only");
        return logLevels;
    }

    private void bindUI(){
        addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent changedEvent) {
                selectedReportingLevel=(String) changedEvent.getValue();
            }
        });
    }

    public String getSelectedReportingLevel() {
        return selectedReportingLevel;
    }
}
