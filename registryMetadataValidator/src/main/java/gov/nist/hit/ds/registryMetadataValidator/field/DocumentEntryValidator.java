package gov.nist.hit.ds.registryMetadataValidator.field;

import gov.nist.hit.ds.registryMetadataValidator.object.ExternalIdentifier;
import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.Arrays;
import java.util.List;

import org.apache.axiom.om.OMElement;

public class DocumentEntryValidator extends ValidatorCommon {
	List<String> doc_externalIdentifiers =
		Arrays.asList(
				MetadataSupport.XDSDocumentEntry_patientid_uuid,
				MetadataSupport.XDSDocumentEntry_uniqueid_uuid
				);
	
	List<String> doc_classifications = 
		Arrays.asList(
				MetadataSupport.XDSDocumentEntry_author_uuid,
				MetadataSupport.XDSDocumentEntry_classCode_uuid,
				MetadataSupport.XDSDocumentEntry_confCode_uuid,
				MetadataSupport.XDSDocumentEntry_eventCode_uuid,
				MetadataSupport.XDSDocumentEntry_formatCode_uuid,
				MetadataSupport.XDSDocumentEntry_hcftCode_uuid,
				MetadataSupport.XDSDocumentEntry_psCode_uuid,
				MetadataSupport.XDSDocumentEntry_typeCode_uuid
				);

	public DocumentEntryValidator(ValidatorCommon vc)  {
		cloneEnvironment(vc);
	}

	void validate_source_patient_id(OMElement spi_slot) {
		OMElement value_list = XmlUtil.firstChildWithLocalName(spi_slot, "ValueList");
		List<OMElement> valueEles = XmlUtil.childrenWithLocalName(value_list, "Value");
		if (valueEles.size() != 1) {
			err("sourcePatientId must have exactly one value");
			return;
		}
		String msg = validate_CX_datatype(valueEles.get(0).getText());
		if (msg != null)
			err("Slot sourcePatientId format error: " + msg);
	}

	void validate_externalIds()  {
		List<String> doc_ids = m.getExtrinsicObjectIds();

		for (int i=0; i<doc_ids.size(); i++) {
			String id = (String) doc_ids.get(i);
			try {
				List<OMElement> ext_ids = m.getExternalIdentifiers(id);
				
				for (OMElement extid : ext_ids) {
					ExternalIdentifier ei = new ExternalIdentifier(m, extid);
					if (!doc_externalIdentifiers.contains(ei.getIdentificationScheme()))
						err("DocumentEntry(" + id + ") contains unidentified ExternalIdentifier with identificationScheme of " + ei.getIdentificationScheme());
				}


				//											name							identificationScheme                    OID required
				validate_ext_id_present("DocumentEntry", id, ext_ids, "XDSDocumentEntry.patientId", MetadataSupport.XDSDocumentEntry_patientid_uuid, false);
				// the oid^ext format is tested in UniqueId.java?
				validate_ext_id_present("DocumentEntry", id, ext_ids, "XDSDocumentEntry.uniqueId", MetadataSupport.XDSDocumentEntry_uniqueid_uuid,  false);
			} catch (XdsInternalException e) {
				err(e);
			} catch (MetadataException e) {
				err(e);
			}
		}
	}



}
