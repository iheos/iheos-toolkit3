package gov.nist.toolkit.xdstools3.client.tabs.queryRetrieveTabs;

import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

public class GetFolderAndContentsTab extends GetFoldersTab{
    private static final String header="Get Folder and Contents";

    public GetFolderAndContentsTab(){
        setHeader(header);
        setTitle(header);
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getGetFoldersAndContentsCode();
    }
}
