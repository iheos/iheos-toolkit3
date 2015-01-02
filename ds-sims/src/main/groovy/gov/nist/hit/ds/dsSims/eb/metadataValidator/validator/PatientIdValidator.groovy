package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class PatientIdValidator extends ValComponentBase {
    Set<String> patient_ids = new HashSet<String>()
    SimHandle simHandle
    Metadata m


    public PatientIdValidator(SimHandle _simHandle, Metadata _m) {
        super(_simHandle.event)
        m = _m;
    }

    public void run()  {
        gather_patient_ids(m, m.getSubmissionSetIds(),   MetadataSupport.XDSSubmissionSet_patientid_uuid);
        gather_patient_ids(m, m.getExtrinsicObjectIds(), MetadataSupport.XDSDocumentEntry_patientid_uuid);
        gather_patient_ids(m, m.getFolderIds(),          MetadataSupport.XDSFolder_patientid_uuid);
        runValidationEngine()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ropid010', msg='Submission shall contain a single Patient ID', ref='ITI TF-3: 4.1.4.1')
    def ropid010() {
        infoFound("Patient IDs found = ${patient_ids}")
        if (patient_ids.size() > 1)
            fail("Multiple Patient IDs found in submission: " + patient_ids)
    }

        void gather_patient_ids(Metadata m, List<String> parts, String uuid)  {
        String patient_id;
        for (String id : parts) {
            patient_id = m.getExternalIdentifierValue(id, uuid);
            if (patient_id == null) continue;
            patient_ids.add(patient_id);
        }
    }
}
