package gov.nist.toolkit.valregmsg.message;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.xdsException.MetadataValidationException;
import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.docref.SqDocRef;
import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.registrysupport.MetadataSupport;
import gov.nist.toolkit.registrysupport.logging.LoggerException;
import gov.nist.toolkit.valregmetadata.object.RegistryObject;
import gov.nist.toolkit.valregmsg.registry.storedquery.generic.StoredQuery;
import gov.nist.toolkit.valregmsg.registry.storedquery.support.ParamParser;
import gov.nist.toolkit.valregmsg.registry.storedquery.support.ParamParser.SlotParse;
import gov.nist.toolkit.valregmsg.registry.storedquery.validation.ValidationStoredQueryFactory;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;
import gov.nist.toolkit.valsupport.message.MessageValidator;
import org.apache.axiom.om.OMElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Validate a Query Request message.
 * @author bill
 *  
 */
public class QueryRequestMessageValidator extends MessageValidator {
	OMElement ahqr;
	
	public void run(IAssertionGroup er, MessageValidatorEngine mvc) {
		this.er = er;
		
		if (ahqr == null) {
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("AdhocQueryRequest: top element null", ""), this);
			return;
		}
		
		OMElement respOpt = MetadataSupport.firstChildWithLocalName(ahqr, "ResponseOption");
		OMElement ahq = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery");
		
		if (!"AdhocQueryRequest".equals(ahqr.getLocalName()))
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("Top level element must be AdhocQueryRequest - found instead " + ahqr.getLocalName(), "ebRS section 6.1"), this);
		
		if (respOpt == null) 
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("AdhocQueryRequest: ResponseOption element missing", "ebRS section 6.1"), this);
		
		if (ahq == null)
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("AdhocQueryRequest: AdhocQuery element missing", "ebRS section 6.1"), this);
		
		String returnType = "";
		if (respOpt != null) {
			returnType = respOpt.getAttributeValue(MetadataSupport.return_type_qname);
			if (returnType == null || returnType.equals("")) {
				er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("AdhocQuery: returnType attribute missing or empty", "ebRS section 6.1"), this);
			} else {
				if (! (returnType.equals("LeafClass") || returnType.equals("ObjectRef"))) {
					if (!(vc.leafClassWithDocumentOk && returnType.equals("LeafClassWithRepositoryItem")))
						er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("AdhocQuery: returnType must be LeafClass or ObjectRef", SqDocRef.Return_type), this);
				}
			}
		}

		if (ahq != null) {
			String queryId = ahq.getAttributeValue(MetadataSupport.id_qname);
			String sqName = MetadataSupport.getSQName(queryId);
			
			er.detail("Query ID is " + queryId);
			er.detail("Query Name is " + sqName);
			
			List<SlotParse> sps = new ArrayList<SlotParse>();
			
			er.detail("Query Parameters are:");
			ParamParser parser = new ParamParser();
			for (OMElement slot : MetadataSupport.childrenWithLocalName(ahq, "Slot")) {
				SlotParse sp = parser.parseSingleSlot(slot);
				er.detail(sp.name + " ==> " + sp.rawValues);
				er.detail(".    .    . yields values " + sp.values);
				for (String error : sp.errs) {
					er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext(error, SqDocRef.Request_parms), this);
				}
				sps.add(sp);
			}
			
			
			ValidationStoredQueryFactory vsqf;
			try {
				vsqf = new ValidationStoredQueryFactory(ahqr, er);
				StoredQuery sq = vsqf.getImpl();
				
				if (sq == null) {
					er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("Do not understand query [" + queryId + "]", SqDocRef.QueryID), this);
					return;
				}
				
				sq.validateParameters();
			} catch (MetadataValidationException e) {
				er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext(e.getMessage(), SqDocRef.Request_parms), this);
			} catch (LoggerException e) {
				er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext(e.getMessage(), null), this);
			} catch (XdsException e) {
				er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext(e.getMessage(), SqDocRef.Request_parms), this);
			}
		}
		
		try {
			new RegistryObject(new Metadata(), ahq);
		
		} catch (XdsInternalException e) {
			
		}


	}
	
	public QueryRequestMessageValidator(ValidationContext vc, OMElement xml) {
		super(vc);
		this.ahqr = xml;
	}

}
