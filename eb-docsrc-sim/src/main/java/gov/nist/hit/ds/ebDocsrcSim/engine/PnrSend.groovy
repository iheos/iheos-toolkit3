package gov.nist.hit.ds.ebDocsrcSim.engine
import gov.nist.hit.ds.ebDocsrcSim.soap.EnvironmentAccess
import gov.nist.hit.ds.ebDocsrcSim.transactions.AbstractTransaction
import gov.nist.hit.ds.ebDocsrcSim.transactions.ProvideAndRegisterTransaction
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsException
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 1/13/15.
 */
public class PnrSend {
    OMElement metadata_element;
    Map<String, DocumentHandler> documents;
    String endpoint;
    EnvironmentAccess environmentAccess
    OMElement logOutput = MetadataSupport.om_factory.createOMElement("Log", null);

    public PnrSend(OMElement _metadata_element, Map<String, DocumentHandler> _documents, String _endpoint, EnvironmentAccess _environmentAccess) {
        metadata_element = _metadata_element
        documents = _documents
        endpoint = _endpoint
        environmentAccess = _environmentAccess
    }

    // return is [result, logOutput]
    public List<OMElement> run() throws XdsException {
        ProvideAndRegisterTransaction trans = new ProvideAndRegisterTransaction();
        trans.no_convert = false;
        trans.nameUuidMap = null;
        trans.instruction_output = logOutput;
        trans.endpoint = endpoint;
        trans.xds_version = AbstractTransaction.xds_b;
        TransactionSettings ts = new TransactionSettings();
        ts.securityParams = environmentAccess
        trans.transactionSettings = ts;
        StepContext step = new StepContext();
        trans.s_ctx = step;
        step.transactionSettings = ts;

        OMElement endpointEle = MetadataSupport.om_factory.createOMElement("Endpoint", null)
        endpointEle.setText(endpoint)
        logOutput.addChild(endpointEle)

        List<OMElement> results = new ArrayList<>();
        try {
            OMElement result = trans.run(metadata_element, documents);
        } catch (XdsInternalException e) {
            OMElement err_ele = MetadataSupport.om_factory.createOMElement("Fault", null);
            err_ele.setText(e.getMessage());
            results.add(err_ele);
        }
        results.add(logOutput);
        return results;
    }
}
