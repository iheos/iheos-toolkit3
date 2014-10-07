package gov.nist.toolkit.xdstools3.client.tabs.preConnectathonTestsTab;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Map;


@RemoteServiceRelativePath("preconnectathon-tab")
public interface PreConnectathonTabService extends RemoteService  {

	/* Test management */
	public Map<String, String> getCollectionNames(String collectionSetName) throws Exception;
	public Map<String, String> getCollection(String collectionSetName,String collectionName) throws Exception;
	public String getTestReadme(String test) throws Exception;
//	public String getTestReadme(String test) throws Exception;
	/* Simulator Management */
//	public List<String> getActorTypeNames() throws Exception ;
}
