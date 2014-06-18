package edu.tn.xds.metadata.editor.client.parse;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import edu.tn.xds.metadata.editor.shared.model.Author;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;
import edu.tn.xds.metadata.editor.shared.model.CodingScheme;
import edu.tn.xds.metadata.editor.shared.model.DTM;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;
import edu.tn.xds.metadata.editor.shared.model.IdentifierOID;
import edu.tn.xds.metadata.editor.shared.model.IdentifierString256;
import edu.tn.xds.metadata.editor.shared.model.InternationalString;
import edu.tn.xds.metadata.editor.shared.model.LanguageCode;
import edu.tn.xds.metadata.editor.shared.model.NameValueDTM;
import edu.tn.xds.metadata.editor.shared.model.NameValueInteger;
import edu.tn.xds.metadata.editor.shared.model.NameValueString256;
import edu.tn.xds.metadata.editor.shared.model.OID;
import edu.tn.xds.metadata.editor.shared.model.String256;
import edu.tn.xds.metadata.editor.shared.model.String256Exception;

/**
 *
 * <b>This class parses a XML file to build the model</b>
 *
 * <p>
 * To do it, here are the following variables required
 * </p>
 * <ul>
 * <li>{@link #instance}: The parse object, this class is a singleton (Parse) ;</li>
 * <li>{@link #documentXml}: The String which contains the XML data (String) ;</li>
 * <li>{@link #myModel}: The document model which will be completed.</li>
 * </ul>
 * </p>
 *
 * <p>
 * This class also contains getters/setters.<br>
 * In addition, it contains several methods such as buildMyModel, findElements,
 * getDocumentParsed, generalMethod and finally one method per element's
 * document.</br>
 * </p>
 *
 * <p>
 * <b>How it works ?</b><br>
 * The method {@link #doParse(String)} return the completed model, indeed it
 * calls {@link #findElements()} whose aim is to call the appropriate method
 * parseType where Type correspond to a specific type describe in the
 * {@link DocumentModel} (InternationalString, Author ...).</br>All this method
 * throws String256Exception if there is a String256 which is larger than 256
 * characters.<br>
 * The method entitled getDocumentParsed instantiates a Preparse object from the
 * singleton Preparse class to delete unexpected escape and to make it UTF-8
 * compatible.</br>
 * </p>
 *
 *
 * <p>
 * <b>See below each method mentioned above.</b> <br>
 * {@link #doParse(String)}</br> {@link #findElements()} <br>
 * {@link #generalMethod(String)}</br> {@link #getDocumentParsed()}
 * </p>
 *
 * <p>
 * <b>See also the parseType methods:</b><br>
 * {@link #parseArrayInternationalString(String)}<br>
 * {@link #methodParseAuthors()}<br>
 * {@link #parseCodedTerm(String)}<br>
 * {@link #parseArrayCodedTerm(String)}<br>
 * {@link #parseIdentifierString256(String)}<br>
 * {@link #parseIdentifierOID(String)}<br>
 * {@link #parseNameValueString256(String)}<br>
 * {@link #parseNameValueInteger(String)}<br>
 * {@link #parseNameValueDTM(String)}<br>
 * {@link #parseString256(String)}
 * </p>
 *
 * @see DocumentModel
 * @see PreParse
 * @see String256
 *
 *
 */
public class Parse {
	private static Logger logger = Logger.getLogger(Parse.class.getName());
	/**
	 * <b>String documentXml</b> - The data taken from the XML document and send
	 * by the server, this is the String to parse.<br>
	 * Type: String</br>
	 *
	 * @see Parse
	 */
	private String documentXml;

	/**
	 * <b>Parse myParse</b> - The instance of Parse class (it's a singleton
	 * class).<br>
	 * Type: {@link Parse}</br>
	 *
	 * @see Parse
	 */
	private final static Parse instance = new Parse();

	/**
	 * <b>Document document</b> - The XML file which is contained in a Document
	 * so as to be parsed.<br>
	 * Type: {@link Document}</br>
	 *
	 * @see Parse
	 */
	private Document document;

	/**
	 * <b>DocumentModel myModel</b> - The model which will be completed by
	 * buildMyModel using the data's XML file.<br>
	 * Type: {@link DocumentModel}</br> </p>
	 *
	 * @see {@link #documentXml}
	 * @see {@link #doParse(String)}
	 * @see DocumentModel
	 * @see Parse
	 */
	private final DocumentModel myModel = new DocumentModel();

	/**
	 * <b>Method doParse</b> <br>
	 * Firstly, it calls {@link #getDocumentParsed()} method to parse the String
	 * {@link #documentXml} and to complete the {@link DocumentModel}
	 * {@link #myModel} thanks to the {@link #findElements()} method. </br>
	 *
	 * @param newDocumentXml
	 *            - The String which contains the XML content
	 * @return myModel - The {@link DocumentModel}
	 *
	 * @see Parse
	 */
	public DocumentModel doParse(String newDocumentXml) {
		documentXml = newDocumentXml;
		getDocumentParsed();
		try {
			findElements();
		} catch (String256Exception e) {
			e.printStackTrace();
		}
		return myModel;
	}

	public static Parse getInstance() {
		return instance;
	}

	private enum NodesEnum {
		titles, comments, authors, mimetype, hash, id, classcode, confidentialitycode, creationtime, eventcode, formatcode, healthcarefacilitytype, languagecode, legalauthenticator, patientid, practicesettingcode, repositoryuniqueid, servicestarttime, servicestoptime, size, sourcepatientid, sourcepatientinfo, typecode, uniqueid, uri;
	}

	public String getDocumentXml() {
		return documentXml;
	}

	public void setDocumentXml(String msg) {
		this.documentXml = msg;
	}

	/**
	 * <b>Method getDocumentParsed</b> <br>
	 * Get the instance of PreParse object to prepare the String parsing. </br>
	 *
	 * @see PreParse
	 * @see Parse
	 *
	 */
	public void getDocumentParsed() {
		PreParse preParse = PreParse.getInstance();
		documentXml = preParse.doPreParse(documentXml);

		// parse the XML document into a DOM
		document = XMLParser.parse(documentXml);
	}

	public DocumentModel getMyModel() {
		return myModel;
	}

	/**
	 * <b>Method findElements</b> <br>
	 * It calls all parseType methods on each element from {@link DocumentModel}
	 * through {@link #generalMethod(String)}. </br>
	 *
	 *
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 *
	 * @see Parse
	 *
	 */
	public void findElements() throws String256Exception {
		generalMethod("titles");
		generalMethod("comments");
		generalMethod("authors");
		generalMethod("classcode");
		generalMethod("confidentialitycode");
		generalMethod("creationtime");
		generalMethod("eventcode");
		generalMethod("formatcode");
		generalMethod("healthcarefacilitytype");
		generalMethod("languagecode");
		generalMethod("legalauthenticator");
		generalMethod("patientid");
		generalMethod("practicesettingcode");
		generalMethod("repositoryuniqueid");
		generalMethod("servicestarttime");
		generalMethod("servicestoptime");
		generalMethod("size");
		generalMethod("sourcepatientid");
		generalMethod("sourcepatientinfo");
		generalMethod("typecode");
		generalMethod("uniqueid");
		generalMethod("uri");
		generalMethod("mimetype");
		generalMethod("id");
		generalMethod("hash");
	}

	/**
	 * <b>Method generalMethod</b> <br>
	 * It calls all parseMethod on each element from {@link DocumentModel}.
	 *
	 * @param nodeString
	 *            (String): The first node to match.
	 *
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 *
	 * @see Parse
	 *
	 */
	public void generalMethod(String nodeString) throws String256Exception {
		NodesEnum nodesEnum = NodesEnum.valueOf(nodeString);
		switch (nodesEnum) {
		case titles:
			myModel.setTitles(parseArrayInternationalString("titles"));
			break;

		case comments:
			myModel.setComments(parseArrayInternationalString("comments"));
			break;

		case authors:
			methodParseAuthors();
			break;

		case classcode:
			myModel.setClassCode(parseCodedTerm("classcode"));
			break;

		case confidentialitycode:
			myModel.setConfidentialityCodes(parseArrayCodedTerm("confidentialitycode"));
			break;

		case creationtime:
			myModel.setCreationTime(parseNameValueDTM("creationtime"));
			break;

		case eventcode:
			myModel.setEventCode(parseArrayCodedTerm("eventcode"));
			break;

		case formatcode:
			myModel.setFormatCode(parseCodedTerm("formatcode"));
			break;

		case healthcarefacilitytype:
			myModel.setHealthcareFacilityType(parseCodedTerm("healthcarefacilitytype"));
			break;

		case languagecode:
			myModel.setLanguageCode(LanguageCode.getValueOf(parseString256(
					"languagecode").getString()));
			break;

		case legalauthenticator:
			myModel.setLegalAuthenticator(parseNameValueString256("legalauthenticator"));
			break;

		case patientid:
			myModel.setPatientID(parseIdentifierString256("patientid"));
			break;

		case practicesettingcode:
			myModel.setPracticeSettingCode(parseCodedTerm("practicesettingcode"));
			break;

		case repositoryuniqueid:
			myModel.setRepoUId(parseOID("repositoryuniqueid"));
			break;

		case servicestarttime:
			myModel.setServiceStartTime(parseNameValueDTM("servicestarttime"));
			break;

		case servicestoptime:
			myModel.setServiceStopTime(parseNameValueDTM("servicestoptime"));
			break;

		case size:
			myModel.setSize(parseNameValueInteger("size"));
			break;

		case sourcepatientid:
			myModel.setSourcePatientId(parseNameValueString256("sourcepatientid"));
			break;

        case sourcepatientinfo:
			myModel.setSourcePatientInfo(parseNameValueString256("sourcepatientinfo"));
			break;

		case typecode:
			myModel.setTypeCode(parseCodedTerm("typecode"));
			break;

		case uniqueid:
			myModel.setUniqueId(parseIdentifierOID("uniqueid"));
			break;

		case uri:
			myModel.setUri(parseString256("uri"));
			break;

		case mimetype:
			myModel.setMimeType(parseString256("mimetype"));
			break;

		case id:
			myModel.setId(parseString256("id"));
			break;

		case hash:
			myModel.setHash(parseString256("hash"));
			break;

		default:
			break;
		}

	}

	/**
	 * <b>Method methodParseAuthors</b> <br>
	 * To obtain the author(s) of the XML file (ArrayList of {@link Author}
	 * ).</br>Called by {@link #generalMethod(String)}.
	 *
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 *
	 * @see Parse
	 *
	 */
	public void methodParseAuthors() throws String256Exception {
		NodeList authorsNode = document.getElementsByTagName("authors");
		ArrayList<Author> authors = new ArrayList<Author>();

		if (!authorsNode.toString().isEmpty()) {
			int titleLength = authorsNode.item(0).getChildNodes().getLength();
			for (int i = 0; i < titleLength; i++) {
				Author intern_temp = new Author();

				ArrayList<String256> authorInstitutions_temp = new ArrayList<String256>();
				ArrayList<String256> authorRoles_temp = new ArrayList<String256>();
				ArrayList<String256> authorSpecialities_temp = new ArrayList<String256>();

				// Set authorPerson
				String256 authorPerson256 = new String256();
				authorPerson256.setString(authorsNode.item(0).getChildNodes()
						.item(i).getChildNodes().item(0).getFirstChild()
						.getNodeValue());
				intern_temp.setAuthorPerson(authorPerson256);

				// Set authorInstitutions
				NodeList authorInstitutionsNodes = authorsNode.item(0)
						.getChildNodes().item(i).getChildNodes().item(1)
						.getChildNodes();

				if (!authorInstitutionsNodes.toString().isEmpty()) {
					for (int j = 0; j < authorInstitutionsNodes.getLength(); j++) {
						String256 str = new String256();
						str.setString(authorInstitutionsNodes.item(j)
								.getFirstChild().getNodeValue());
						if (str.verify(str.getString())) {
							authorInstitutions_temp.add(str);
						}
					}
					intern_temp.setAuthorInstitutions(authorInstitutions_temp);

				} else {
					logger.warning("AuthorInstitutions node is empty for author named "
							+ authorsNode.item(0).getChildNodes().item(i)
									.getChildNodes().item(0).getFirstChild()
									.getNodeValue()
							+ "!\nCheck your XML Document");
				}

				// Set authorRoles
				NodeList authorRolesNodes = authorsNode.item(0).getChildNodes()
						.item(i).getChildNodes().item(2).getChildNodes();

				if (!authorRolesNodes.toString().isEmpty()) {
					for (int j = 0; j < authorRolesNodes.getLength(); j++) {
						String256 str = new String256();
						str.setString(authorRolesNodes.item(j).getFirstChild()
								.getNodeValue());

						if (str.verify(str.getString())) {
							authorRoles_temp.add(str);
						}

					}
					intern_temp.setAuthorRoles(authorRoles_temp);

				} else {
					logger.warning("AuthorRoles node is empty for author named "
							+ authorsNode.item(0).getChildNodes().item(i)
									.getChildNodes().item(0).getFirstChild()
									.getNodeValue()
							+ "!\nCheck your XML Document");
					// TODO Fire an event for this error that could be handled
					// in the view
				}

				// Set authorSpecialities
				NodeList authorSpecialitiesNodes = authorsNode.item(0)
						.getChildNodes().item(i).getChildNodes().item(3)
						.getChildNodes();

				if (!authorSpecialitiesNodes.toString().isEmpty()) {
					for (int j = 0; j < authorSpecialitiesNodes.getLength(); j++) {
						String256 str = new String256();
						str.setString(authorSpecialitiesNodes.item(j)
								.getFirstChild().getNodeValue());
						if (str.verify(str.getString())) {
							authorSpecialities_temp.add(str);
						}
					}
					intern_temp.setAuthorSpecialities(authorSpecialities_temp);

				} else {
					logger.warning("AuthorSpecialities node is empty for author named "
							+ authorsNode.item(0).getChildNodes().item(i)
									.getChildNodes().item(0).getFirstChild()
									.getNodeValue()
							+ "!\nCheck your XML Document");
					// TODO fire an event to handle the issue in the view
				}
				// Add this Element to the model
				authors.add(intern_temp);

			}
			// Set title to myModel
			myModel.setAuthors(authors);
		} else {
			myModel.setAuthors(null);
			logger.warning("Authors node is empty!\nCheck your XML Document");
			// TODO Fire an event for this error that could be handled in the
			// view
		}
	}

	/**
	 * <b>Method parseString256</b> <br>
	 * To obtain the element of type {@link String256}.</br>Called by
	 * {@link #generalMethod(String)} on each {@link String256} element.
	 *
	 * @param node
	 *            (String): The name of the root node.
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 * @return {@link String256}
	 * @see Parse
	 *
	 */
	public String256 parseString256(String node) throws String256Exception {
		NodeList nodeList = document.getElementsByTagName(node);
		if (!nodeList.toString().isEmpty()) {
			String256 string256 = new String256();
			if (string256.verify(nodeList.item(0).getChildNodes().item(0)
					.getNodeValue())) {
				string256.setString(nodeList.item(0).getChildNodes().item(0)
						.getNodeValue());

				return string256;
			} else {
				// FIXME Not sure it should be done here but maybe with the
				// verify method which may return something
				logger.warning(node + " node is larger than 256 characters");
				return null;
			}

		} else {
			logger.warning(node + " node is empty!\nCheck your XML Document");
			return null;
		}
	}

	/**
	 * <b>Method parseOID</b> <br>
	 * To obtain the element of type {@link OID}.</br>Called by
	 * {@link #generalMethod(String)} on each OID element.
	 *
	 * @param node
	 *            (String): The name of the root node.
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 * @return {@link OID}
	 * @see Parse
	 *
	 */
	public OID parseOID(String node) throws String256Exception {
		NodeList nodeList = document.getElementsByTagName(node);
		if (!nodeList.toString().isEmpty()) {
			String256 string256 = new String256();
			OID oid = new OID();
			if (string256.verify(nodeList.item(0).getChildNodes().item(0)
					.getNodeValue())) {
				string256.setString(nodeList.item(0).getChildNodes().item(0)
						.getNodeValue());
				oid.setOid(string256);
				return oid;
			} else {
				// FIXME Not sure it should be done that way (abstraction issue,
				// string256 validation check)
				logger.warning(node + " node is larger than 256 characters");
				return null;
			}

		} else {
			// TODO Fire an event for this error that could be handled in the
			// view
			logger.warning(node + " node is empty!\nCheck your XML Document");
			return null;
		}
	}

	/**
	 * <p>
	 * <b>Method parseIdentifierString256</b> <br>
	 * To obtain the element of type {@link IdentifierString256}.</br>Called by
	 * {@link #generalMethod generalMethod} on each {@link IdentifierString256}
	 * element.
	 * </p>
	 *
	 * @param node
	 *            (String) : The name of the root node.
	 *
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 * @return {@link IdentifierString256}
	 * @see Parse class Parse
	 *
	 */
	public IdentifierString256 parseIdentifierString256(String node)
			throws String256Exception {
		NodeList nodeList = document.getElementsByTagName(node);
		String256 identifier256_value = new String256();
		String256 identifier256_type = new String256();

		IdentifierString256 identifier = new IdentifierString256();

		if (!nodeList.toString().isEmpty()) {
			// Set value
			identifier256_value.setString(nodeList.item(0).getChildNodes()
					.item(0).getChildNodes().item(0).getFirstChild()
					.getNodeValue());
			if (identifier256_value.verify(identifier256_value.getString())) {
				identifier.setValue(identifier256_value);

			}

			// Set type
			identifier256_type.setString(nodeList.item(0).getChildNodes()
					.item(0).getChildNodes().item(1).getFirstChild()
					.getNodeValue());

			if (identifier256_type.verify(identifier256_type.getString())) {

				identifier.setIdType(identifier256_type);
			}

			// Set IDPatient element to myModel
			return identifier;
		} else {
			logger.warning(node + " node is empty!\nCheck your XML Document");
			return null;
		}
	}

	/**
	 * <b>Method parseIdentifierOID</b> <br>
	 * To obtain the element of type {@link IdentifierOID}.</br>Called by
	 * {@link #generalMethod(String)} on each {@link IdentifierOID} element.
	 * </p>
	 *
	 * @param node
	 *            (String) : The name of the root node.
	 *
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 * @return {@link IdentifierOID}
	 * @see Parse
	 *
	 */
	public IdentifierOID parseIdentifierOID(String node)
			throws String256Exception {
		NodeList nodeList = document.getElementsByTagName(node);
		String256 identifier256_value = new String256();
		String256 identifier256_type = new String256();
		IdentifierOID identifier = new IdentifierOID();
		OID oid_type = new OID();

		if (!nodeList.toString().isEmpty()) {
			// Set value
			identifier256_value.setString(nodeList.item(0).getChildNodes()
					.item(0).getChildNodes().item(0).getFirstChild()
					.getNodeValue());
			if (identifier256_value.verify(identifier256_value.getString())) {
				identifier.setValue(identifier256_value);

			}

			// Set type
			identifier256_type.setString(nodeList.item(0).getChildNodes()
					.item(0).getChildNodes().item(1).getFirstChild()
					.getNodeValue());

			if (identifier256_type.verify(identifier256_type.getString())) {
				oid_type.setOid(identifier256_type);
				identifier.setIdType(oid_type);
			}
			// Set IDPatient element to myModel
			return identifier;
		} else {
			logger.warning(node + " node is empty!\nCheck your XML Document");
			return null;
		}
	}

	/**
	 * <b>Method parseArrayInternationalString</b> <br>
	 * To obtain the element of type ArrayList(InternationalString) .</br>Called
	 * by {@link #generalMethod(String)} on each ArrayList(InternationalString)
	 * element.
	 *
	 * @param node
	 *            (String) : The name of the root node.
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 * @return ArrayList(InternationalString)
	 * @see Parse
	 *
	 */
	public ArrayList<InternationalString> parseArrayInternationalString(
			String node) throws String256Exception {
		NodeList nodeList = document.getElementsByTagName(node);
		ArrayList<InternationalString> array = new ArrayList<InternationalString>();

		if (!nodeList.toString().isEmpty()) {
			int nodeLength = nodeList.item(0).getChildNodes().getLength();
			for (int i = 0; i < nodeLength; i++) {
				InternationalString internationalString = new InternationalString();

				String256 international256_value = new String256();

				// Set languageCode
				LanguageCode international256_langCode = LanguageCode
						.getValueOf(nodeList.item(0).getChildNodes().item(i)
								.getChildNodes().item(0).getFirstChild()
								.getNodeValue());
				// if
				// (international256_langCode.verify(international256_langCode.toString()))
				// {
				internationalString.setLangCode(international256_langCode);
				// }

				// Set information
				international256_value.setString(nodeList.item(0)
						.getChildNodes().item(i).getChildNodes().item(1)
						.getFirstChild().getNodeValue());
				if (international256_value.verify(international256_value
						.getString())) {
					internationalString.setValue(international256_value);
				}

				// Add this Element to the model
				array.add(internationalString);
			}
			// Set title to myModel
			return array;
		} else {
			logger.warning(node + " node is empty!\nCheck your XML Document");
			return null;
		}
	}

	/**
	 * <b>Method parseCodedTerm</b> <br>
	 * To obtain the element of type CodedTerm.</br>Called by
	 * {@link #generalMethod(String)} on each {@link CodedTerm} element. </p>
	 *
	 * @param node
	 *            (String) : The name of the root node.
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 * @return {@link CodedTerm}
	 * @see Parse
	 *
	 */
	public CodedTerm parseCodedTerm(String node) throws String256Exception {
		NodeList nodeList = document.getElementsByTagName(node);

		if (!nodeList.toString().isEmpty()) {
			CodingScheme codingScheme = new CodingScheme();
			CodedTerm codedTerm = new CodedTerm();
			String256 codedTerm256_name = new String256();
			String256 codedTerm256_code = new String256();
			String256 codedTerm256_scheme = new String256();

			// Set displayName
			codedTerm256_name.setString(nodeList.item(0).getChildNodes()
					.item(0).getChildNodes().item(0).getFirstChild()
					.getNodeValue());

			if (codedTerm256_name.verify(codedTerm256_name.getString())) {
				codedTerm.setDisplayName(codedTerm256_name);
			}

			// Set code
			codedTerm256_code.setString(nodeList.item(0).getChildNodes()
					.item(0).getChildNodes().item(1).getFirstChild()
					.getNodeValue());
			if (codedTerm256_code.verify(codedTerm256_code.getString())) {
				codedTerm.setCode(codedTerm256_code);
			}

			// Set codingScheme
			codedTerm256_scheme.setString(nodeList.item(0).getChildNodes()
					.item(0).getChildNodes().item(2).getFirstChild()
					.getNodeValue());
			if (codedTerm256_scheme.verify(codedTerm256_scheme.getString())) {
				codingScheme.setCodingScheme(codedTerm256_scheme);
				codedTerm.setCodingScheme(codingScheme);
			}
			return codedTerm;

		} else {
			logger.warning(node + " node is empty!\nCheck your XML Document");
			return null;
		}
	}

	/**
	 * <p>
	 * <b>Method parseArrayCodedTerm</b> <br>
	 * To obtain the element of type ArrayList(CodedTerm).</br>Called by
	 * {@link #generalMethod(String)} on each ArrayList(CodedTerm) element.
	 * </p>
	 *
	 * @param node
	 *            (String) : The name of the root node.
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 * @return ArrayList(CodedTerm)
	 * @see Parse
	 *
	 */
	public ArrayList<CodedTerm> parseArrayCodedTerm(String node)
			throws String256Exception {
		NodeList nodeList = document.getElementsByTagName(node);
		ArrayList<CodedTerm> arrayCodedTerm = new ArrayList<CodedTerm>();

		if (!nodeList.toString().isEmpty()) {
			int nodeLength = nodeList.item(0).getChildNodes().getLength();

			for (int i = 0; i < nodeLength; i++) {
				CodingScheme codingScheme = new CodingScheme();
				CodedTerm codedTerm = new CodedTerm();
				String256 codedTerm256_name = new String256();
				String256 codedTerm256_code = new String256();
				String256 codedTerm256_scheme = new String256();

				// Set displayName
				codedTerm256_name.setString(nodeList.item(0).getChildNodes()
						.item(i).getChildNodes().item(0).getFirstChild()
						.getNodeValue());

				if (codedTerm256_name.verify(codedTerm256_name.getString())) {
					codedTerm.setDisplayName(codedTerm256_name);
				}

				// Set code
				codedTerm256_code.setString(nodeList.item(0).getChildNodes()
						.item(i).getChildNodes().item(1).getFirstChild()
						.getNodeValue());
				if (codedTerm256_code.verify(codedTerm256_code.getString())) {
					codedTerm.setCode(codedTerm256_code);
				}

				// Set codingScheme
				codedTerm256_scheme.setString(nodeList.item(0).getChildNodes()
						.item(i).getChildNodes().item(2).getFirstChild()
						.getNodeValue());
				if (codedTerm256_scheme.verify(codedTerm256_scheme.getString())) {
					codingScheme.setCodingScheme(codedTerm256_scheme);
					codedTerm.setCodingScheme(codingScheme);
				}

				// Add this CodedTerm
				arrayCodedTerm.add(codedTerm);
			}

			return arrayCodedTerm;

		} else {
			logger.warning(node + " node is empty!\nCheck your XML Document");
			return null;
		}
	}

	/**
	 * <b>Method parseNameValueString256</b> <br>
	 * To obtain the element of type {@link NameValueString256}.</br>Called by
	 * {@link #generalMethod(String)} on each {@link NameValueString256}
	 * element.
	 *
	 * @param node
	 *            (String) : The name of the root node.
	 *
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 * @return {@link NameValueString256}
	 * @see Parse
	 *
	 */
	public NameValueString256 parseNameValueString256(String node)
			throws String256Exception {
		NodeList nodeList = document.getElementsByTagName(node);
		String256 name256 = new String256();

		if (nodeList.toString().isEmpty()) {
			logger.warning(node + " node is empty!\nCheck your XML Document");
			return null;
		} else {
			// NameValue<String256>
			NameValueString256 nameValue = new NameValueString256();
			ArrayList<String256> values = new ArrayList<String256>();
			// Set name
			name256.setString(nodeList.item(0).getChildNodes().item(0)
					.getFirstChild().getFirstChild().getNodeValue());
			if (name256.verify(name256.getString())) {
				nameValue.setName(name256);
			}

			// Set values
			int valuesLength = nodeList.item(0).getChildNodes().item(0)
					.getChildNodes().item(1).getChildNodes().getLength();

			for (int i = 0; i < valuesLength; i++) {
				String256 value256 = new String256();
				value256.setString(nodeList.item(0).getChildNodes().item(0)
						.getChildNodes().item(1).getChildNodes().item(i)
						.getFirstChild().getNodeValue());
				values.add(value256);
			}
			nameValue.setValues(values);
			return nameValue;
		}
	}

	/**
	 * <b>Method parseNameValueInteger</b> <br>
	 * To obtain the element of type {@link NameValueInteger}.</br>Called by
	 * {@link #generalMethod(String)} on each {@link NameValueInteger} element.
	 *
	 * @param node
	 *            (String) : The name of the root node.
	 *
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 * @return {@link NameValueInteger}
	 * @see Parse
	 */
	public NameValueInteger parseNameValueInteger(String node)
			throws String256Exception {
		NodeList nodeList = document.getElementsByTagName(node);
		String256 name256 = new String256();

		if (nodeList.toString().isEmpty()) {
			logger.warning(node + " node is empty!\nCheck your XML Document");
			return null;
		} else {
			NameValueInteger nameValue = new NameValueInteger();
			ArrayList<Integer> values = new ArrayList<Integer>();

			// Set name
			name256.setString(nodeList.item(0).getChildNodes().item(0)
					.getFirstChild().getFirstChild().getNodeValue());
			if (name256.verify(name256.getString())) {
				nameValue.setName(name256);
			}

			// Set values
			int valuesLength = nodeList.item(0).getChildNodes().item(0)
					.getChildNodes().item(1).getChildNodes().getLength();

			for (int i = 0; i < valuesLength; i++) {
				String value;
				int integer;

				value = nodeList.item(0).getChildNodes().item(0)
						.getChildNodes().item(1).getChildNodes().item(i)
						.getFirstChild().getNodeValue();
				integer = Integer.decode(value);
				values.add(integer);
			}
			nameValue.setValues(values);
			return nameValue;

		}
	}

	/**
	 * <b>Method parseNameValueDTM</b> <br>
	 * To obtain the element of type {@link NameValueDTM}.</br>Called by
	 * {@link #generalMethod(String)} on each {@link NameValueDTM} element.
	 *
	 * @param node
	 *            (String) : The name of the root node.
	 *
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 *
	 * @return {@link NameValueDTM}
	 * @see Parse
	 *
	 */
	public NameValueDTM parseNameValueDTM(String node)
			throws String256Exception {
		NodeList nodeList = document.getElementsByTagName(node);
		String256 name256 = new String256();

		if (nodeList.toString().isEmpty()) {
			logger.warning(node + " node is empty!\nCheck your XML Document");
			return null;
		} else {

			NameValueDTM nameValue = new NameValueDTM();
			ArrayList<DTM> values = new ArrayList<DTM>();

			// Set name
			name256.setString(nodeList.item(0).getChildNodes().item(0)
					.getFirstChild().getFirstChild().getNodeValue());
			if (name256.verify(name256.getString())) {
				nameValue.setName(name256);
			}

			// Set values
			int valuesLength = nodeList.item(0).getChildNodes().item(0)
					.getChildNodes().item(1).getChildNodes().getLength();

			for (int i = 0; i < valuesLength; i++) {
				String256 value256 = new String256();
				DTM dtm = new DTM();

				value256.setString(nodeList.item(0).getChildNodes().item(0)
						.getChildNodes().item(1).getChildNodes().item(i)
						.getFirstChild().getNodeValue());
				dtm.setDtm(DateTimeFormat.getFormat("yyyyMMddhhmmss").parse(value256.getString()));
				values.add(dtm);
			}
			nameValue.setValues(values);
			return nameValue;
		}
	}
}
