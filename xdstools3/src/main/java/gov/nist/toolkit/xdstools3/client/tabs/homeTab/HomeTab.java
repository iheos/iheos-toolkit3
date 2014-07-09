package gov.nist.toolkit.xdstools3.client.tabs.homeTab;


import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.IconLabel;
import gov.nist.toolkit.xdstools3.client.tabs.GenericTab;

public class HomeTab extends GenericTab {

    public HomeTab(String s) {
        super(s);
        setContents();
    }

    private void setContents(){
        VStack queriesAndRetrieves = createQueryRetrieveDisplay();
        setContents(queriesAndRetrieves);
    }

    public VStack createQueryRetrieveDisplay(){
        VStack stack = new VStack();

        IconLabel queriesAndRetrieves = createSubtitle1("Queries and Retrieves");
        queriesAndRetrieves.setIcon("icons/glyphicons/glyphicons_027_search.png");

        HomeLinkButton findDocs = new HomeLinkButton("Find Documents");
        HomeLinkButton mpq = new HomeLinkButton("MPQ Find Documents");

        IconLabel sendTestData = createSubtitle1("Send Test Data");
        sendTestData.setIcon("icons/glyphicons/glyphicons_123_message_out.png");


        stack.setMembers(queriesAndRetrieves, findDocs, mpq, sendTestData);

        return stack;
    }


}
