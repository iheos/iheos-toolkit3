package gov.nist.toolkit.xdstools3.server.demo;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by onh2 on 8/26/2014.
 *
 * Provides test data / GUI demo data for Actors and Collections widgets
 */
public enum TestDataHelper {
    instance;

    Map<String,String> testsMap;

    private TestDataHelper(){
        testsMap = new HashMap<>();
        testsMap.put("DSUB_IHEGREEN-1016","DSUB_IHEGREEN-1016");
        testsMap.put("MPQ_IHEGREEN-1020","MPQ_IHEGREEN-1020");
        testsMap.put("DSUB_IHERED-1024","DSUB_IHERED-1024");
        testsMap.put("MPQ_IHEGREEN-1018","MPQ_IHEGREEN-1018");
    }

    public Map<String,String> getTestDataSet(){
        return testsMap;
    }
}
