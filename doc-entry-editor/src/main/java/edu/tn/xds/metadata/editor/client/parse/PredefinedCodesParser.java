package edu.tn.xds.metadata.editor.client.parse;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import edu.tn.xds.metadata.editor.client.editor.widgets.PredefinedCodes;
import edu.tn.xds.metadata.editor.client.resources.AppResources;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;
import edu.tn.xds.metadata.editor.shared.model.CodingScheme;
import edu.tn.xds.metadata.editor.shared.model.String256;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Parser class for loading possible coded values for CodedTerms and MimeType.
 *
 * <code>
 *     // Sample to load the list of possible CodedTerms
 *     PredefinedCodesParser.INSTANCE.getCodes(PredefinedCodes.XXXX); // PredefinedCodes.CLASS_CODES for instance
 *     // Sample to load the list of possible Mime Types
 *     PredefinedCodesParser.INSTANCE.getMimeTypes();
 * </code>
 *
 * @see edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry
 * @see edu.tn.xds.metadata.editor.shared.model.CodedTerm
 */
public enum PredefinedCodesParser {
    INSTANCE;
    @SuppressWarnings("GwtInconsistentSerializableClass")
    private final static Document dom = XMLParser.parse(PreParse.getInstance().doPreParse(
            AppResources.INSTANCE.codes().getText()));
    private static final List<CodedTerm> classCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> formatCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> healthcareFacilityTypeCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> practiceSettingCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> typeCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> confidentialityCodes = new ArrayList<CodedTerm>();
    private static final List<CodedTerm> eventCodes = new ArrayList<CodedTerm>();
    private static final List<String256> mimeTypes = new ArrayList<String256>();
    private static String CONFIG_FILE_ROOT_NODE = "CodeType";
    @SuppressWarnings("GwtInconsistentSerializableClass")
    private static final NodeList nodes = dom.getElementsByTagName(CONFIG_FILE_ROOT_NODE);
    @SuppressWarnings("GwtInconsistentSerializableClass")
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Method that extracts and returns a list of possible CodedTerms from a server XML configuration file for Codes.
     * @param predefinedCodes Type of CodedTerm requested
     * @return list of possible CodedTerms for the type requested.
     * @see edu.tn.xds.metadata.editor.shared.model.CodedTerm
     * @see edu.tn.xds.metadata.editor.client.editor.widgets.PredefinedCodes
     */
    public List<CodedTerm> getCodes(PredefinedCodes predefinedCodes) {

        if (predefinedCodes.equals(PredefinedCodes.CLASS_CODES)) {
            if (classCodes.size() == 0) {
                classCodes.addAll(parseNode(ConfigCodeNodes.classCode.toString()));
            }
            return classCodes;
        } else if (predefinedCodes.equals(PredefinedCodes.FORMAT_CODES)) {
            if (formatCodes.size() == 0) {
                formatCodes.addAll(parseNode(ConfigCodeNodes.formatCode.toString()));
            }
            return formatCodes;
        } else if (predefinedCodes.equals(PredefinedCodes.HEALTHCARE_FACILITY_TYPE_CODES)) {
            if (healthcareFacilityTypeCodes.size() == 0) {
                healthcareFacilityTypeCodes.addAll(parseNode(ConfigCodeNodes.healthcareFacilityTypeCode.toString()));
            }
            return healthcareFacilityTypeCodes;
        } else if (predefinedCodes.equals(PredefinedCodes.PRACTICE_SETTING_CODES)) {
            if (practiceSettingCodes.size() == 0) {
                practiceSettingCodes.addAll(parseNode(ConfigCodeNodes.practiceSettingCode.toString()));
            }
            return practiceSettingCodes;
        } else if (predefinedCodes.equals(PredefinedCodes.TYPE_CODES)) {
            if (typeCodes.size() == 0) {
                typeCodes.addAll(parseNode(ConfigCodeNodes.typeCode.toString()));
            }
            return typeCodes;
        } else if (predefinedCodes.equals(PredefinedCodes.CONFIDENTIALITY_CODES)) {
            if (confidentialityCodes.size() == 0) {
                confidentialityCodes.addAll(parseNode(ConfigCodeNodes.confidentialityCode.toString()));
            }
            return confidentialityCodes;
        } else if (predefinedCodes.equals(PredefinedCodes.EVENT_CODES)) {
            if (eventCodes.size() == 0) {
                eventCodes.addAll(parseNode(ConfigCodeNodes.eventCodeList.toString()));
            }
            return eventCodes;
        }
        return null;
    }

    /**
     * Method to parse a specific code node from xml configuration file for codes
     * @param nodeName
     * @return list of CodedTerm
     * @see edu.tn.xds.metadata.editor.shared.model.CodedTerm
     */
    private List<CodedTerm> parseNode(String nodeName) {
        int index = 0;
        List<CodedTerm> temp = new ArrayList<CodedTerm>();
        while (!(((Element) nodes.item(index)).getAttribute(ConfigCodeNodes.CodeAttributes.name.toString()).equals(nodeName))) {
            index++;
        }
        NodeList n = ((Element) nodes.item(index)).getElementsByTagName("Code");
        for (int i = 0; i < n.getLength(); i++) {
            CodedTerm code = new CodedTerm();
            code.setCode(new String256().setString(((Element) n.item(i)).getAttribute(ConfigCodeNodes.CodeAttributes.code
                    .toString())));
            code.setDisplayName(new String256().setString(((Element) n.item(i))
                    .getAttribute(ConfigCodeNodes.CodeAttributes.display.toString())));
            code.setCodingScheme(new CodingScheme().setCodingScheme(new String256().setString(((Element) n.item(i))
                    .getAttribute(ConfigCodeNodes.CodeAttributes.codingScheme.toString()))));
            if (!temp.contains(code))
                temp.add(code);
        }
        Collections.sort(temp, new Comparator<CodedTerm>() {
            @Override
            public int compare(CodedTerm o1, CodedTerm o2) {
                return o1.getDisplayName().toString().compareTo(o2.getDisplayName().toString());
            }
        });
        return temp;
    }

    /**
     * Method to extract and return possible MimeTypes from a server configuration XML file.
     *
     * @return list of possible MimeTypes
     * @see edu.tn.xds.metadata.editor.shared.model.String256
     */
    public List<String256> getMimeTypes() {
        if (mimeTypes.size() == 0) {
            int index = 0;
            while (!(((Element) nodes.item(index)).getAttribute("name").equals("mimeType"))) {
                index++;
            }
            NodeList n = ((Element) nodes.item(index)).getElementsByTagName("Code");
            for (int i = 0; i < n.getLength(); i++) {
                String256 code = new String256().setString(((Element) n.item(i)).getAttribute("code"));

                if (!mimeTypes.contains(code))
                    mimeTypes.add(code);
            }
            Collections.sort(mimeTypes, new Comparator<String256>() {
                @Override
                public int compare(String256 o1, String256 o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
        }
        return mimeTypes;
    }

    /**
     * Possible nodes codes enumerated class for parser
     */
    private static enum ConfigCodeNodes {
        classCode, formatCode, healthcareFacilityTypeCode, practiceSettingCode, typeCode, confidentialityCode, eventCodeList, mimeType;
        /**
         * Possible attributes codes enumerated class for parser
         */
        private static enum CodeAttributes {
            name, code, display, codingScheme;
        }
    }
}
