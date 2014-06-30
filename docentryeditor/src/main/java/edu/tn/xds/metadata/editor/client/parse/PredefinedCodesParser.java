package edu.tn.xds.metadata.editor.client.parse;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import edu.tn.xds.metadata.editor.client.editor.widgets.PredefinedCodesComboBox.PredefinedCodes;
import edu.tn.xds.metadata.editor.client.resources.AppResources;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;
import edu.tn.xds.metadata.editor.shared.model.CodingScheme;
import edu.tn.xds.metadata.editor.shared.model.String256;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public enum PredefinedCodesParser {
    INSTANCE;

    @SuppressWarnings("GwtInconsistentSerializableClass")
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @SuppressWarnings("GwtInconsistentSerializableClass")
    private final static Document dom = XMLParser.parse(PreParse.getInstance()
            .doPreParse(AppResources.INSTANCE.codes().getText()));

    @SuppressWarnings("GwtInconsistentSerializableClass")
    private static final NodeList nodes = dom.getElementsByTagName("CodeType");

    private static final List<CodedTerm> classCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> formatCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> healthcareFacilityTypeCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> practiceSettingCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> typeCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> confidentialityCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> eventCodes = new ArrayList<CodedTerm>();


    public List<CodedTerm> getCodes(PredefinedCodes predefinedCodes) {

        if (predefinedCodes.equals(PredefinedCodes.CLASS_CODES)) {
            if (classCodes.size() == 0) {
                classCodes.addAll(parseNode("classCode"));
            }
            return classCodes;
        }else if (predefinedCodes.equals(PredefinedCodes.FORMAT_CODES)){
            if (formatCodes.size() == 0) {
                formatCodes.addAll(parseNode("formatCode"));
            }
            return formatCodes;
        }else if (predefinedCodes.equals(PredefinedCodes.HEALTHCARE_FACILITY_TYPE_CODES)){
            if (healthcareFacilityTypeCodes.size() == 0) {
                healthcareFacilityTypeCodes.addAll(parseNode("healthcareFacilityTypeCode"));
            }
            return healthcareFacilityTypeCodes;
        }else if (predefinedCodes.equals(PredefinedCodes.PRACTICE_SETTING_CODES)){
            if (practiceSettingCodes.size() == 0) {
                practiceSettingCodes.addAll(parseNode("practiceSettingCode"));
            }
            return practiceSettingCodes;
        }else if (predefinedCodes.equals(PredefinedCodes.TYPE_CODES)){
            if (typeCodes.size() == 0) {
                typeCodes.addAll(parseNode("typeCode"));
            }
            return typeCodes;
        }else if (predefinedCodes.equals(PredefinedCodes.CONFIDENTIALITY_CODES)){
            if (confidentialityCodes.size() == 0) {
                confidentialityCodes.addAll(parseNode("confidentialityCode"));
            }
            return confidentialityCodes;
        }else if (predefinedCodes.equals(PredefinedCodes.EVENT_CODES)){
            if (eventCodes.size() == 0) {
                eventCodes.addAll(parseNode("eventCodeList"));
            }
            return eventCodes;
        }
        return null;
    }

    private List<CodedTerm> parseNode(String nodeName){
        int index = 0;
        List<CodedTerm> temp = new ArrayList<CodedTerm>();
        while (!(((Element) nodes.item(index)).getAttribute("name")
                .equals(nodeName))) {
            index++;
        }
        NodeList n = ((Element) nodes.item(index))
                .getElementsByTagName("Code");
        for (int i = 0; i < n.getLength(); i++) {
            CodedTerm code = new CodedTerm();
            code.setCode(new String256().setString(((Element) n
                    .item(i)).getAttribute("code")));
            code.setDisplayName(new String256()
                    .setString(((Element) n.item(i))
                            .getAttribute("display")));
            code.setCodingScheme(new CodingScheme()
                    .setCodingScheme(new String256()
                            .setString(((Element) n.item(i))
                                    .getAttribute("codingScheme"))));
            if(!temp.contains(code))
                temp.add(code);
        }
        Collections.sort(temp, new Comparator<CodedTerm>() {
            @Override
            public int compare(CodedTerm o1, CodedTerm o2) {
                return o1.getDisplayName().toString()
                        .compareTo(o2.getDisplayName().toString());
            }
        });
        return temp;
    }
}
