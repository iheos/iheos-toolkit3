package gov.nist.hit.ds.ebDocsrcSim.transactions;

import gov.nist.hit.ds.ebDocsrcSim.engine.DocumentHandler;
import gov.nist.hit.ds.ebDocsrcSim.soap.SoapActionFactory;
import gov.nist.hit.ds.ebMetadata.Metadata;
import gov.nist.hit.ds.ebMetadata.MetadataParser;
import gov.nist.hit.ds.ebMetadata.MetadataSupport;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsExceptions.ExceptionUtil;
import gov.nist.hit.ds.xdsExceptions.MetadataException;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;

import javax.activation.DataHandler;
import java.util.Map;

public class ProvideAndRegisterTransaction extends AbstractTransaction {
    boolean use_xop = true;

    public ProvideAndRegisterTransaction()  {}

    public OMElement run(OMElement metadata_element, Map<String, DocumentHandler> documents) throws XdsInternalException, MetadataException {
        OMElement pnr = XmlUtil.om_factory.createOMElement("ProvideAndRegisterDocumentSetRequest", MetadataSupport.xdsB);
        if (no_convert)
            pnr.addChild(metadata_element);
        else {
            Metadata metadata = MetadataParser.parse(metadata_element);
            pnr.addChild(metadata.getV3SubmitObjectsRequest());
        }
        for (String id : documents.keySet() ) {
            if (nameUuidMap != null) {
                String newId = nameUuidMap.get(id);
                if (newId != null && !id.equals(""))
                    id = newId;
            }
            DataHandler dataHandler = documents.get(id).getDataHandler();
            OMText t = MetadataSupport.om_factory.createOMText(dataHandler, true);
            t.setOptimize(use_xop);
            OMElement document = MetadataSupport.om_factory.createOMElement("Document", MetadataSupport.xdsB);
            document.addAttribute("id", id, null);
            document.addChild(t);
            pnr.addChild(document);
        }

        try {
            useMtom = true;
            useAddressing = true;

            soapCall(pnr);
            OMElement result = getSoapResult();

//            if (result == null) {
//                if (testLog != null)
//                    testLog.add_name_value(instruction_output, "Response", "Not Available");
//                s_ctx.set_error("Result was null");
//            } else {
//                if (testLog != null)
//                    testLog.add_name_value(instruction_output, "Response", result);
//            }
            return result;

        }
        catch (Exception e) {
            throw new XdsInternalException(ExceptionUtil.exception_details(e));
        }
    }

    protected String getRequestAction() {
        if (async) {
            return SoapActionFactory.pnr_b_async_action;
        } else {
            return SoapActionFactory.pnr_b_action;
        }
    }


}
