package gov.nist.toolkit.xdstools3.client.tabs;

public abstract class GenericCloseableTab extends GenericTab {

    public GenericCloseableTab(String header){
        super(header);
        setCanClose(true);
    }

}
