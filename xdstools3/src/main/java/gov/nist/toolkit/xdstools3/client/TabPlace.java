package gov.nist.toolkit.xdstools3.client;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Created by onh2 on 9/22/2014.
 */
public class TabPlace extends Place{
    public static class Tokenizer implements PlaceTokenizer<TabPlace> {

        @Override
        public TabPlace getPlace(String s) {
            return new TabPlace(s);
        }

        @Override
        public String getToken(TabPlace tabPlace) {
            return tabPlace.getTabId().toString();
        }
    }

    private String tabId;

    public TabPlace(String tabId){
        this.tabId=tabId;
    }

    public String getTabId(){
        return tabId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TabPlace) {
            return tabId.equals(((TabPlace) obj).tabId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return tabId.hashCode();
    }
}
