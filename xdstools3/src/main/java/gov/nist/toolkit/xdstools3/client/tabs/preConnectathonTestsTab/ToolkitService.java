package gov.nist.toolkit.xdstools3.client.tabs.preConnectathonTestsTab;


import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("toolkit")
public interface ToolkitService extends RemoteService  {

	/* Test management */
	public Map<String, String> getCollectionNames(String collectionSetName) throws Exception;
//	public Map<String, String> getCollection(String collectionSetName, String collectionName) throws Exception;
//	public String getTestReadme(String test) throws Exception;
	/* Simulator Management */
//	public List<String> getActorTypeNames() throws Exception ;
}
