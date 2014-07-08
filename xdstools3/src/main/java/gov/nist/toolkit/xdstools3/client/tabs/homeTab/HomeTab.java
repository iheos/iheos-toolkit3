package gov.nist.toolkit.xdstools3.client.tabs.homeTab;


import com.smartgwt.client.widgets.form.DynamicForm;
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

        HomeLink findDocs = new HomeLink("Find Documents");
        HomeLink mpq = new HomeLink("MPQ Find Documents");

        DynamicForm form = new DynamicForm();
        form.setItems(findDocs, mpq);
        stack.setMembers(form);

        return stack;
    }


}
