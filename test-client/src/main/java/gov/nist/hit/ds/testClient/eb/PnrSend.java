package gov.nist.hit.ds.testClient.eb;

import gov.nist.hit.ds.ebMetadata.MetadataSupport;
import gov.nist.hit.ds.testClient.engine.PlanContext;
import gov.nist.hit.ds.testClient.engine.StepContext;
import gov.nist.hit.ds.testClient.engine.TransactionSettings;
import gov.nist.hit.ds.testClient.transactions.BasicTransaction;
import gov.nist.hit.ds.testClient.transactions.ProvideAndRegisterTransaction;
import gov.nist.hit.ds.xdsExceptions.XdsException;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;
import org.apache.axiom.om.OMElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bmajur on 1/13/15.
 */
public class PnrSend {
    OMElement metadata_element;
    Map<String, DocumentHandler> documents;
    String endpoint;
    OMElement logOutput = MetadataSupport.om_factory.createOMElement("Log", null);
    OMElement stepLogOutput = MetadataSupport.om_factory.createOMElement("StepLog", null);

    public PnrSend(OMElement _metadata_element, Map<String, DocumentHandler> _documents, String _endpoint) { metadata_element = _metadata_element; documents = _documents; endpoint = _endpoint;}

    // return is [result, logOutput]
    public List<OMElement> run() throws XdsException {
        ProvideAndRegisterTransaction trans = new ProvideAndRegisterTransaction();
        trans.no_convert = false;
        trans.nameUuidMap = null;
        trans.testConfig.prepare_only = false;
        trans.testConfig.saml = false;
        trans.instruction_output = logOutput;
        trans.endpoint = endpoint;
        trans.xds_version = BasicTransaction.xds_b;
        TransactionSettings ts = new TransactionSettings();
        trans.transactionSettings = ts;
        PlanContext plan = new PlanContext();
        StepContext step = new StepContext(plan);
        step.test_step_output = stepLogOutput;
        trans.s_ctx = step;
        trans.planContext = plan;
        step.transactionSettings = ts;
        step.setTransationSettings(ts);

        List<OMElement> results = new ArrayList<OMElement>();
        try {
            OMElement result = trans.run(metadata_element, documents);
            results.add(result);
        } catch (XdsInternalException e) {
            OMElement err_ele = MetadataSupport.om_factory.createOMElement("Fault", null);
            err_ele.setText(e.getMessage());
            results.add(err_ele);
        }
        results.add(logOutput);
        results.add(stepLogOutput);
        return results;
    }
}
