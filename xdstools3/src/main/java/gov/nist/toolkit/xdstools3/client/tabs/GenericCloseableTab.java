package gov.nist.toolkit.xdstools3.client.tabs;

public class GenericCloseableTab extends GenericTab {

    public GenericCloseableTab(String s){
        super(s);
        setCanClose(true);
    }

}
