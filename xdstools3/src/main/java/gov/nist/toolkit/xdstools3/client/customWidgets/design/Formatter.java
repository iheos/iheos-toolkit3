package gov.nist.toolkit.xdstools3.client.customWidgets.design;

/**
 * Formatting utilities.
 */
public class Formatter {

    public static IconLabel createTabHeader(String s){
        IconLabel l = new IconLabel();
        l.setContents(s);
        l.setStyleName("h3");
        return l;
    }

    public static IconLabel createTabHeaderWithoutLeftPadding(String s){
        IconLabel l = new IconLabel();
        l.setContents(s);
        l.setStyleName("h3-nopadding");
        return l;
    }

    public static IconLabel createHomeSubtitle(String s){
        IconLabel l = new IconLabel();
        l.setContents(s);
        l.setStyleName("home-subtitle");
        return l;
    }

    public static IconLabel createSubtitle1(String s){
        IconLabel l = new IconLabel();
        l.setContents(s);
        l.setStyleName("subtitle1");
        return l;
    }

    public static IconLabel createSubtitle2(String s){
        IconLabel l = new IconLabel();
        l.setContents(s);
        l.setStyleName("subtitle2");
        return l;
    }
}
