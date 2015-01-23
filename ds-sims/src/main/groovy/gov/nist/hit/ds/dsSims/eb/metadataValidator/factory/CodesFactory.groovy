package gov.nist.hit.ds.dsSims.eb.metadataValidator.factory
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.codes.AllCodes
import gov.nist.hit.ds.dsSims.eb.metadataValidator.parser.CodesParser
import gov.nist.hit.ds.toolkit.environment.Environment
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
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
        File codesFile = environment.codesFile
        def codes
        if (codesFile.exists())
            codes = codesFile.text
        else
            codes = new CodesFactory().getClass().classLoader.getResource('external-cache/environment/default/codes.xml').text
        try {
            return Util.parse_xml(codes)
        } catch (Exception e) {
            throw new ToolkitRuntimeException("Cannot load codes file for environment ${environment.name}. File name is ${codesFile}", e)
        }
//        }
    }

}
