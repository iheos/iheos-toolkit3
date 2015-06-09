package gov.nist.hit.ds.utilities.xml;

/**
 * Created by bill on 6/8/15.
 */
public class HtmlizeXml  {
    public static String run(String input) {
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

}
