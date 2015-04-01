package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import java.util.ArrayList;

/**
 * Created by dazais on 3/9/2015.
 */
public class DSFactory {
    private static ArrayList<SiteDS> dsArray = new ArrayList<SiteDS>();


    public static SiteDS getDataSource(String siteName){
        SiteDS ds = new SiteDS();
        ds.setDataURL("resources/datasources/endpoints/" + siteName + ".data.xml");
        ds.setID(siteName + "DS");

        registerDS(ds);

        return ds;
    }

    /**
     * Keep track of instances of the SiteDS Datasource
     * @param ds
     */
    private static void registerDS(SiteDS ds){
        dsArray.add(ds);
    }

    /**
     * Checks if a given instance of a datasource was already created, based on its ID
     * @param datasourceID
     * @return
     */
    public static boolean dsInstanceExists(String datasourceID){
        for (SiteDS ds : dsArray) {
            if (ds.getID() == datasourceID) {
                return true;
            }
            return false;
        }
        return false;
    }


}
