package gov.nist.hit.ds.registrySim.sq.sims;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registryMsgFormats.AdhocQueryResponse;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuery;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQueryFactory;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.valSupport.engine.MessageValidatorEngine;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

import com.google.gwt.http.client.Response;


public class SqSim  extends TransactionSimulator implements MetadataGeneratingSim, AdhocQueryResponseGeneratingSim {
	AdhocQueryResponse response;
	Metadata m = new Metadata();
	Exception startUpException = null;
	Logger logger = Logger.getLogger(SqSim.class);

	public SqSim(SimCommon common) {

		super(common);
		
		vc.hasSoap = true;
		vc.isSQ = true;
		vc.isRequest = true;
		vc.updateable = false;
		
		// build response
		try {
			response = new AdhocQueryResponse(Response.version_3);
		} catch (Exception e) {
			System.out.println(ExceptionUtil.exception_details(e));
			startUpException = e;
			return;
		}
	}
	
	public void run(ErrorRecorder er, MessageValidatorEngine mvc) {

		if (startUpException != null)
			er.err(XdsErrorCode.Code.XDSRegistryError, startUpException);

		// if request didn't validate, return so errors can be reported
		if (common.hasErrors()) {
			try {
				response.add(common.getRegistryErrorList(), null);
			} catch (XdsInternalException e) {
				er.err(XdsErrorCode.Code.XDSRegistryError, e);
			}
			return;
		}
		
		// run stored query
		try {
			SoapMessageValidator smv = (SoapMessageValidator) common.getMessageValidator(SoapMessageValidator.class);
			OMElement ahqr = smv.getMessageBody();
			
			StoredQueryFactory fact = null;
			fact = new SQFactory(ahqr, response, null);
			StoredQuery sq = fact.getImpl();
			if (sq == null)
				throw new Exception("Stored Query not implemented");
			StoredQuerySupport sqs = sq.getStoredQuerySupport();
			
			// only appropriate for original implementation
			sqs.runEndProcessing = false;
			linkSqToRegIndex(sq);
			
			Metadata mr = sq.run();
			
			er.detail("SQ contents: " + mr.structure());
			
			m.copy(mr);
			List<OMElement> results = m.getAllObjects(); // everything but ObjectRefs
			results.addAll(m.getObjectRefs());
			response.addQueryResults(results, false);
		} catch (Exception e) {
			String msg = e.getMessage();
			if (msg == null || msg.equals(""))
				msg = ExceptionUtil.exception_details(e);
			logger.error(msg);
			er.err(XdsErrorCode.Code.XDSRegistryError, msg, this, null);
		}

	}
	
	void linkSqToRegIndex(StoredQuery sq) throws XdsInternalException {
		if (sq instanceof FindDocumentsSim) {
			FindDocumentsSim sim = (FindDocumentsSim) sq;
			sim.setRegIndex(common.regIndex);
		} else if (sq instanceof FindSubmissionSetsSim) {
				FindSubmissionSetsSim sim = (FindSubmissionSetsSim) sq;
				sim.setRegIndex(common.regIndex);
		} else if (sq instanceof FindFoldersSim) {
			FindFoldersSim sim = (FindFoldersSim) sq;
			sim.setRegIndex(common.regIndex);
		} else if (sq instanceof GetDocumentsSim) {
			GetDocumentsSim sim = (GetDocumentsSim) sq;
			sim.setRegIndex(common.regIndex);
		} else if (sq instanceof GetRelatedDocumentsSim) {
			GetRelatedDocumentsSim sim = (GetRelatedDocumentsSim) sq;
			sim.setRegIndex(common.regIndex);
		} else if (sq instanceof GetSubmissionSetsSim) {
			GetSubmissionSetsSim sim = (GetSubmissionSetsSim) sq;
			sim.setRegIndex(common.regIndex);
		} else if (sq instanceof GetAssociationsSim) {
			GetAssociationsSim sim = (GetAssociationsSim) sq;
			sim.setRegIndex(common.regIndex);
		} else if (sq instanceof GetDocumentsAndAssociationsSim) {
			GetDocumentsAndAssociationsSim sim = (GetDocumentsAndAssociationsSim) sq;
			sim.setRegIndex(common.regIndex);
		} else if (sq instanceof GetFoldersSim) {
			GetFoldersSim sim = (GetFoldersSim) sq;
			sim.setRegIndex(common.regIndex);
		} else if (sq instanceof GetFoldersForDocumentSim) {
			GetFoldersForDocumentSim sim = (GetFoldersForDocumentSim) sq;
			sim.setRegIndex(common.regIndex);
		} else if (sq instanceof GetSubmissionSetAndContentsSim) {
			GetSubmissionSetAndContentsSim sim = (GetSubmissionSetAndContentsSim) sq;
			sim.setRegIndex(common.regIndex);
		} else if (sq instanceof GetFolderAndContentsSim) {
			GetFolderAndContentsSim sim = (GetFolderAndContentsSim) sq;
			sim.setRegIndex(common.regIndex);
		} else {
			throw new XdsInternalException("Internal Error: " + sq.getClass().getCanonicalName() + " is not linked to Registry Index");
		}
	}


	public Metadata getMetadata() {
		return m;
	}

	public AdhocQueryResponse getAdhocQueryResponse() {
		return response;
	}

	public Response getResponse() {
		return response;
	}


}
