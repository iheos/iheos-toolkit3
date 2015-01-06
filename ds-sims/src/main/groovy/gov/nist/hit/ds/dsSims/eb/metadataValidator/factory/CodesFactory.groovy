package gov.nist.hit.ds.dsSims.eb.metadataValidator.factory
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.codes.AllCodes
import gov.nist.hit.ds.dsSims.eb.metadataValidator.parser.CodesParser
import gov.nist.hit.ds.toolkit.environment.Environment
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import gov.nist.toolkit.utilities.xml.Util
import org.apache.axiom.om.OMElement

public class CodesFactory {

    @Deprecated
    public AllCodes load(File codesFile)  {
        try {
            OMElement rawCodes = Util.parse_xml(codesFile)
            return new CodesParser().parse(rawCodes)
        } catch (Exception e) {
            throw new RuntimeException("Cannot load codes file <" + codesFile + ">.")
        }
    }

    static public OMElement getCodes(Environment environment) {
        if (environment == null) {
            // retrieve default internal codes
            try {
                String codesText = new CodesFactory().getClass().getClassLoader().getResource('codes.xml').text
                return Util.parse_xml(codesText)
            } catch (Exception e) {
                throw new ToolkitRuntimeException("Cannot load default codes file.", e)
            }
        } else {
            File codesFile = environment.codesFile
            try {
                return Util.parse_xml(codesFile)
            } catch (Exception e) {
                throw new ToolkitRuntimeException("Cannot load codes file for environment ${environment.name}.", e)
            }
        }
    }

    }
