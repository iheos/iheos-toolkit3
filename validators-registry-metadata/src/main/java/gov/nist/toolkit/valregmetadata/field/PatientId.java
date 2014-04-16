package gov.nist.toolkit.valregmetadata.field;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.registrysupport.MetadataSupport;

import java.util.ArrayList;
import java.util.List;

public class PatientId {
	List<String> patient_ids;
    IAssertionGroup er;
	Metadata m;


	public PatientId( Metadata m, IAssertionGroup er) {
		this.er = er;
		this.m = m;
		patient_ids = new ArrayList<String>();
	}

	public void run()  {
		try {
		gather_patient_ids(m, m.getSubmissionSetIds(),   MetadataSupport.XDSSubmissionSet_patientid_uuid);
		gather_patient_ids(m, m.getExtrinsicObjectIds(), MetadataSupport.XDSDocumentEntry_patientid_uuid);
		gather_patient_ids(m, m.getFolderIds(),          MetadataSupport.XDSFolder_patientid_uuid);

		if (patient_ids.size() > 1)
			er.err(XdsErrorCode.Code.XDSPatientIdDoesNotMatch, new ErrorContext("Multiple Patient IDs found in submission: " + patient_ids, "ITI TF-3: 4.1.4.1"), this);
		} catch (Exception e) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(e.getMessage(), ""), this);
		}
}


	void gather_patient_ids(Metadata m, List<String> parts, String uuid) throws MetadataException {
		String patient_id;
		for (String id : parts) {		
			patient_id = m.getExternalIdentifierValue(id, uuid);
			if (patient_id == null) continue;
			if ( ! patient_ids.contains(patient_id)) 
				patient_ids.add(patient_id);
		}
	}

//	void err(String msg) {
//		rel.add_error(MetadataSupport.XDSRegistryMetadataError, msg, "PatientId.java", null);
//	}
}
