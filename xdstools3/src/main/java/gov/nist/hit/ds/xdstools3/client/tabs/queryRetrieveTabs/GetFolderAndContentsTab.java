package gov.nist.hit.ds.xdstools3.client.tabs.queryRetrieveTabs;

import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;

public class GetFolderAndContentsTab extends GetFoldersTab{
    private static final String header="Get Folder and Contents";

    public GetFolderAndContentsTab(){
        setHeader(header);
        setTitle(header);
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getGetFoldersAndContentsCode();
    }
}
