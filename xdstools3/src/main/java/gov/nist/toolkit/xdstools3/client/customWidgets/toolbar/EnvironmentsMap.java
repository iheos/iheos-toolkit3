package gov.nist.toolkit.xdstools3.client.customWidgets.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gov.nist.toolkit.xdstools3.client.exceptions.NoServletSessionException;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Retrieves and formats Environment values for display inside a SmartGWT dropdrown. Adds and allows retrieve of associated icons.
 * Allows to manually edit icons path and file extension.
 */
public class EnvironmentsMap {
    private LinkedHashMap<String, String> valueMap;
    private LinkedHashMap<String, String> iconsMap;

    // ----- Edit the icon path here if needed -----
    private String iconURLPrefix = "icons/flags/16/";
    private String iconURLSuffix = ".png";


    public EnvironmentsMap(){
        createValueMap();
        createIconsMap();

    }

    /**
     * Retrieves environment values from backend and creates a custom map for a better display featuring flag icons.
     * It differentiates between EU environments ("EURO***") and US only. US environments are all those that are non-EURO.
     *
     */
    private void createValueMap(){
        ToolbarServiceAsync service = GWT.create(ToolbarService.class);
        AsyncCallback<ArrayList<String>> envCallback = new AsyncCallback<ArrayList<String>>() {
            public void onFailure(Throwable caught) {
                new NoServletSessionException(caught);
            }
            @Override
            public void onSuccess(ArrayList<String> result) {
                String key = "";
                int counterEU = 0;
                int counterUS = 0;
                valueMap = new LinkedHashMap<>();
                for (String envName:result){
                    if (envName.contains("EURO")){
                        counterEU++;
                        key = "EU" + Integer.toString(counterEU);
                        valueMap.put(key, envName);
                    }
                    else {
                        counterUS++;
                        key = "US" + Integer.toString(counterUS);
                        valueMap.put(key, envName);
                    }
                }
                valueMap.put("US1", "NA2014");
                valueMap.put("EU1", "EURO2011");
                valueMap.put("EU2", "EURO2012");
                valueMap.put("US2", "NwHIN");
            }
        };
        service.retrieveEnvironments(envCallback);

    }


    private void createIconsMap() {
        // This is a workaround for the  LinkedHashMap, which normally accepts unique elements only, to display the
        // flags although several are redundant.
        iconsMap = new LinkedHashMap<>();
        iconsMap.put("US1", "US");
        iconsMap.put("EU1", "EU");
        iconsMap.put("US2", "US");
        iconsMap.put("EU2", "EU");
    }


    // ------ GETTERS -----

    /**
     * Retrieves the map of available environments (ex. NA2014, EURO2012...)
     * @return Map of environments
     */
    public LinkedHashMap<String, String> getValueMap() {
        return valueMap;
    }

    /**
     * Retrieves the map of icon names matching the Environments, for display purposes (ex. EURO2012 --> EU flag icon). The icon names and stripped
     * of their extension.
     * @return Map of icon names
     */
    public LinkedHashMap<String, String> getIconsMap() {
        return iconsMap;
    }

    /**
     * Retrieves the path of the icon folder associated with the Environments, for display purposes. All icons used must be in same folder.
     * @return Relative path for the environment icons folder
     */
    public String getIconURLPrefix() {
        return iconURLPrefix;
    }

    public String getIconURLSuffix() {
        return iconURLSuffix;
    }
}
