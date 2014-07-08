package gov.nist.toolkit.xdstools3.client.tabs.homeTab;


import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VStack;
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

        Label queriesAndRetrieves = createSubtitle("Queries and Retrieves");


        HomeLinkButton findDocs = new HomeLinkButton("Find Documents");
        HomeLinkButton mpq = new HomeLinkButton("MPQ Find Documents");

        stack.setMembers(queriesAndRetrieves, findDocs, mpq);

        return stack;
    }


}
