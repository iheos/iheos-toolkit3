package gov.nist.toolkit.xdstools3.client.customWidgets.design;

/**
 * Formatting utilities.
 */
public class Formatter {

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
