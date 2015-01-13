package gov.nist.hit.ds.testClient.transactions;

import gov.nist.hit.ds.ebMetadata.Metadata;
import gov.nist.hit.ds.ebMetadata.MetadataParser;
import gov.nist.hit.ds.ebMetadata.MetadataSupport;
import gov.nist.hit.ds.testClient.engine.StepContext;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.commondatatypes.client.MetadataTypes;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ProvideAndRegisterTransaction extends RegisterTransaction {
	boolean use_xop = true;
	HashMap<String, String> document_id_filenames = new HashMap<String, String>();

	public ProvideAndRegisterTransaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
	}

	protected  String getBasicTransactionName() {
		return "pr";
	}

	public void run(OMElement metadata_element)
			throws XdsException {

		validate_xds_version();

		if (metadata_filename == null)
			throw new XdsInternalException("No MetadataFile element found for RegisterTransaction instruction within step " + this.s_ctx.get("step_id"));


		OMElement body = null;
		OMElement pnr = XmlUtil.om_factory.createOMElement("ProvideAndRegisterDocumentSetRequest", MetadataSupport.xdsB);
		if (no_convert)
			pnr.addChild(metadata_element);
		else {
			Metadata metadata = MetadataParser.parse(metadata_element);
			pnr.addChild(metadata.getV3SubmitObjectsRequest());
		}
		for (String id : document_id_filenames.keySet() ) {
			String filename = (String) document_id_filenames.get(id);

			if (nameUuidMap != null) {
				String newId = nameUuidMap.get(id);
				if (newId != null && !id.equals(""))
					id = newId;
			}

			DataHandler dataHandler = new DataHandler(new FileDataSource(filename));
			OMText t = MetadataSupport.om_factory.createOMText(dataHandler, true);
			t.setOptimize(use_xop);
			OMElement document = MetadataSupport.om_factory.createOMElement("Document", MetadataSupport.xdsB);
			document.addAttribute("id", id, null);
			document.addChild(t);
			pnr.addChild(document);
		}

//			log_metadata(pnr);

		if (testConfig.prepare_only)
			return;

		body = pnr;
		try {
			OMElement result = null;

//				setMetadata(body);
			useMtom = use_xop;
			useAddressing = true;

			soapCall(body);
			result = getSoapResult();


			if (result == null) {
				testLog.add_name_value(instruction_output, "Result", "None");
				s_ctx.set_error("Result was null");
			} else {
				testLog.add_name_value(instruction_output, "Result", result);

				validate_registry_response(
						result,
						(xds_version == xds_a) ? MetadataTypes.METADATA_TYPE_R : MetadataTypes.METADATA_TYPE_SQ);
			}

		}
		catch (Exception e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e));
		}
	}

	ArrayList<String> singleton(String value) {
		ArrayList<String> al = new ArrayList<String>();
		al.add(value);
		return al;
	}

	String htmlize(String xml) {
		return xml.replaceAll("<", "&lt;");
	}

	String file_extension(String path) {
		String[] parts = path.split("/");
		String filename;
		if (parts.length < 2)
			filename = path;
		else
			filename = parts[parts.length-1];
		int dot = filename.indexOf(".");
		if (dot == -1)
			return "";
		return filename.substring(dot+1);
	}

	OMElement getHeader(OMElement envelope) {
		return XmlUtil.firstChildWithLocalName(envelope, "Header");
	}

	OMElement getBody(OMElement envelope) {
		return XmlUtil.firstChildWithLocalName(envelope, "Body");
	}

	protected void parseInstruction(OMElement part) throws XdsInternalException {
		String part_name = part.getLocalName();
		if (part_name.equals("Document")) {
			String id = part.getAttributeValue(MetadataSupport.id_qname);
			if (id == null || id.equals("")) throw new XdsInternalException("ProvideAndRegisterTransaction: empty id attribute on Document element");
			String filename = part.getText();
			if (filename == null || filename.equals("")) throw new XdsInternalException("ProvideAndRegisterTransaction: Document with id " + id + " has no filename specified");
			document_id_filenames.put(id, testConfig.testplanDir + File.separator + filename);
		}
		else if (part_name.equals("XDSb")) {
			xds_version = BasicTransaction.xds_b;
		}
		else if (part_name.equals("XDSa")) {
			xds_version = BasicTransaction.xds_a;
		}
		else if (part_name.equals("NoXOP")) {
			this.use_xop = false;
		}
		else {
			parseBasicInstruction(part);
		}
	}

	protected String getRequestAction() {
		if (xds_version == BasicTransaction.xds_b) {
			if (async) {
				return SoapActionFactory.pnr_b_async_action;
			} else {
				return SoapActionFactory.pnr_b_action;
			}
		} else {
			return SoapActionFactory.anon_action;
		}
	}


}
