package edu.tn.xds.metadata.editor.shared.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import edu.tn.xds.metadata.editor.client.parse.Parse;

/**
 * 
 * <b>This class represents the model which have to be respected by the xml
 * document</b>
 * 
 * <p>
 * An XML document should have this different parameters
 * <ul>
 * <li>{@link #titles}: A title (ArrayList({@link InternationalString})) ;</li>
 * <li>{@link #comments}: Comments (ArrayList({@link InternationalString})) ;</li>
 * <li>{@link #authors}: One or several author(s) (ArrayList({@link Author})) ;</li>
 * <li>{@link #classCode}: A class code ({@link CodedTerm}) ;</li>
 * <li>{@link #confidentialityCodes}: A confidentiality code (ArrayList(
 * {@link CodedTerm})) ;</li>
 * <li>{@link #creationTime}: A creation time ({@link NameValueDTM}) ;</li>
 * <li>{@link #id}: An id ({@link String256}) ;</li>
 * <li>{@link #eventCode}: An event code (ArrayList({@link CodedTerm})) ;</li>
 * <li>{@link #formatCode}: A format code ({@link CodedTerm}) ;</li>
 * <li>{@link #hash}: An hash ({@link String256}) ;</li>
 * <li>{@link #healthcareFacilityType}: An health care facility type(
 * {@link CodedTerm}) ;</li>
 * <li>{@link #languageCode}: A language code {@link LanguageCode}) ;</li>
 * <li>{@link #legalAuthenticator}: A legal authenticator (
 * {@link NameValueString256}) ;</li>
 * <li>{@link #mimeType}: A mime type ({@link String256}) ;</li>
 * <li>{@link #patientID}: A patient id ({@link IdentifierString256}) ;</li>
 * <li>{@link #practiceSettingCode}: A practice setting code ({@link CodedTerm}
 * ) ;</li>
 * <li>{@link #repoUId}: A repository unique id ({@link String256}) ;</li>
 * <li>{@link #serviceStartTime}: A service start time ({@link NameValueDTM}) ;</li>
 * <li>{@link #serviceStopTime}: A service stop time ({@link NameValueDTM}) ;</li>
 * <li>{@link #size}: A size ({@link NameValueInteger}) ;</li>
 * <li>{@link #sourcePatientId}: A source patient id ({@link NameValueString256}
 * ) ;</li>
 * <li>{@link #typeCode}: A type code ({@link CodedTerm}) ;</li>
 * <li>{@link #uniqueId}: A unique id ({@link IdentifierOID}) ;</li>
 * <li>{@link #uri}: An uri ({@link String256}).</li>
 * </ul>
 * </p>
 * 
 * <p>
 * This class also contains getters/setters, toXml() method to return
 * information in XML format and verify method to check syntax's document.
 * </p>
 * 
 * 
 * <p>
 * <b> See below each type of element you can find in this model</b><br>
 * {@link Author}</br> {@link CodedTerm} <br>
 * {@link IdentifierOID}</br> {@link IdentifierString256}<br>
 * {@link InternationalString} </br> {@link NameValueString256}<br>
 * {@link NameValueDTM}</br>{@link NameValueInteger}<br>
 * {@link String256} </br> {@link OID}<br>
 * {@link DTM}
 * </p>
 * 
 * <p>
 * <b>See below each method mentioned above.</b> <br>
 * {@link #verify() method verify}</br> {@link #toXML() method toXML} <br>
 * </p>
 * 
 * <p>
 * DocumentModel is used in {@link Parse} class to complete the local
 * DocumentModel.
 * </p>
 * 
 * @see ModelElement class ModelElement
 */
public class DocumentModel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * <b>ArrayList(InternationalString title</b> - The title of the document
	 * [Optional].<br>
	 * Type: ArrayList of {@link InternationalString}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..1 </p>
	 * 
	 * @see InternationalString
	 * @see DocumentModel
	 */
	@Nullable
	private ArrayList<InternationalString> titles;

	/**
	 * <b>ArrayList(InternationalString) comments</b> - The comments of the
	 * document [Optional].<br>
	 * Type: ArrayList of {@link InternationalString}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..1 </p>
	 * 
	 * @see InternationalString
	 * @see DocumentModel
	 */
	@Nullable
	private ArrayList<InternationalString> comments;

	/**
	 * <b>ArrayList(Author) authors</b> - The author(s) of the document
	 * [Optional].<br>
	 * Type: ArrayList of {@link Author}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..n </p>
	 * 
	 * @see Author
	 * @see DocumentModel
	 */
	@Nullable
	private ArrayList<Author> authors;

	/**
	 * <b>CodedTerm classCode</b> - The class code of the document [Mandatory].<br>
	 * Type: {@link CodedTerm}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..1 </p>
	 * 
	 * @see CodedTerm
	 * @see DocumentModel
	 */
	@NotNull
	private CodedTerm classCode;

	/**
	 * <b>CodedTerm confidentialityCode</b> - The confidentiality code of the
	 * document [Mandatory].<br>
	 * Type: ArrayList of {@link CodedTerm}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..n </p>
	 * 
	 * @see CodedTerm
	 * @see DocumentModel
	 */
	@NotNull
	@NotEmpty
	private ArrayList<CodedTerm> confidentialityCodes;

	/**
	 * <b>NameValueDTM creationTime</b> - The creation time of the document
	 * [Mandatory].<br>
	 * Type: {@link NameValueDTM}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..1 </p>
	 * 
	 * @see NameValueDTM
	 * @see DocumentModel
	 */
	@NotNull
	private NameValueDTM creationTime;

	/**
	 * <b>String256 id</b> - The id of the document [Mandatory].<br>
	 * Type: {@link String256}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..1 </p>
	 * 
	 * @see String256
	 * @see DocumentModel
	 */
	@NotNull
	private String256 id;

	/**
	 * <b>ArrayList(CodedTerm) eventCode</b> - The event code of the document
	 * [Optional].<br>
	 * Type: {@link CodedTerm}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..n </p>
	 * 
	 * @see CodedTerm
	 * @see DocumentModel
	 */
	@Nullable
	private ArrayList<CodedTerm> eventCode;

	/**
	 * <b>CodedTerm formatCode</b> - The format code of the document
	 * [Mandatory].<br>
	 * Type: {@link CodedTerm}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..1 </p>
	 * 
	 * @see CodedTerm
	 * @see DocumentModel
	 */
	@NotNull
	private CodedTerm formatCode;

	/**
	 * <b>String256 hash</b> - The hash of the document [Optional].<br>
	 * Type: {@link String256}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..1 </p>
	 * 
	 * @see String256
	 * @see DocumentModel
	 */
	@Nullable
	private String256 hash;

	/**
	 * <b>CodedTerm healthcareFacilityType</b> - The health care facility type
	 * of the document [Mandatory].<br>
	 * Type: {@link CodedTerm}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..n </p>
	 * 
	 * @see CodedTerm
	 * @see DocumentModel
	 */
	@NotNull
	@NotEmpty
	private CodedTerm healthcareFacilityType;

	/**
	 * <b>LanguageCode languageCode</b> - The language code of the document
	 * [Mandatory].<br>
	 * Type: {@link LanguageCode}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..1 </p>
	 * 
	 * @see LanguageCode
	 * @see DocumentModel
	 */
	@NotNull
	// private String256 languageCode;
	private LanguageCode languageCode;

	/**
	 * <b>NameValueString256 legalAuthenticator</b> - The legal authenticator of
	 * the document [Optional].<br>
	 * Type: {@link NameValueString256}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..1 </p>
	 * 
	 * @see NameValueString256
	 * @see DocumentModel
	 */
	@Nullable
	private NameValueString256 legalAuthenticator;

	/**
	 * <b>String256 mimeType</b> - The mimeType of the document [Mandatory].<br>
	 * Type: {@link String256}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..1 </p>
	 * 
	 * 
	 * @see String256
	 * @see DocumentModel
	 */
	@NotNull
	private String256 mimeType;

	/**
	 * <b>IdentifierString256 patientID</b> - The patient id of the document
	 * [Mandatory].<br>
	 * Type: {@link IdentifierString256}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..1
	 * 
	 * @see IdentifierString256
	 * @see DocumentModel
	 */
	@NotNull
	private IdentifierString256 patientID;

	/**
	 * <b>CodedTerm practiceSettingCode</b> - The practice setting code of the
	 * document [Mandatory].<br>
	 * Type: {@link CodedTerm}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..1
	 * 
	 * @see CodedTerm
	 * @see DocumentModel
	 */
	@NotNull
	private CodedTerm practiceSettingCode;

	/**
	 * <b>OID repositoryUId</b> - The repository unique id of the document
	 * [Optional].<br>
	 * Type: {@link String256}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..1 </p>
	 * 
	 * @see OID
	 * @see DocumentModel
	 */
	@Nullable
	private OID repoUId;

	/**
	 * <b>NameValueDTM serviceStartTime</b> - The service start time of the
	 * document [Optional].<br>
	 * Type: {@link NameValueDTM}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..1
	 * 
	 * @see NameValueDTM
	 * @see DocumentModel
	 */
	@Nullable
	private NameValueDTM serviceStartTime;

	/**
	 * <b>NameValueDTM serviceStopTime</b> - The service stop time of the
	 * document [Optional].<br>
	 * Type: {@link NameValueDTM}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..1 </p>
	 * 
	 * @see NameValueDTM
	 * @see DocumentModel
	 */
	@Nullable
	private NameValueDTM serviceStopTime;

	/**
	 * 
	 * <b>NameValueInteger size</b> - The size of the document [Optional].<br>
	 * Type: {@link NameValueInteger}</br> </p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..1 </p>
	 * 
	 * @see NameValueInteger
	 * @see DocumentModel
	 */
	@Nullable
	private NameValueInteger size;

	/**
	 * <b>NameValueString256 sourcePatientId</b> - The source patient id of the
	 * document [Optional].<br>
	 * Type: {@link NameValueString256}</br></p>
	 * 
	 * <b>Cardinality:</b> 0..1</p>
	 * 
	 * @see NameValueString256
	 * @see DocumentModel
	 */
	@Nullable
	private NameValueString256 sourcePatientId;

	/**
	 * <b>CodedTerm typeCode</b> - The type code of the document [Mandatory].<br>
	 * Type: {@link CodedTerm}</br></p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..1</p>
	 * 
	 * @see CodedTerm
	 * @see DocumentModel
	 */
	@NotNull
	private CodedTerm typeCode;

	/**
	 * 
	 * 
	 * <b> IdentifierOID uniqueId</b> - The unique id of the document
	 * [Mandatory].<br>
	 * Type: {@link IdentifierOID}</br></p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 1..1</p>
	 * 
	 * @see IdentifierOID
	 * @see DocumentModel
	 */
	@NotNull
	private IdentifierOID uniqueId;

	/**
	 * <b>String256 uri</b> - The uri of the document [Optional].<br>
	 * Type: {@link String256}</br></p>
	 * 
	 * <b>Cardinality:</b><br>
	 * 0..1</p>
	 * 
	 * @see String256
	 * @see DocumentModel
	 */
	@Nullable
	private String256 uri;

	/**
	 * <b>ArrayList(ArrayList(String)) validationErrors</b> - The error(s) which
	 * occure(s) when verify() method is called.<br>
	 * Type: ArrayList<ArrayList<String>></br> </p>
	 * 
	 * <p>
	 * This array represents (("document_variable_name", errorMessage)
	 * </p>
	 * 
	 * @see DocumentModel class DocumentModel
	 */
	private final ArrayList<ArrayList<String>> validationErrors = new ArrayList<ArrayList<String>>();

	public DocumentModel() {
		classCode = new CodedTerm();
		creationTime = new NameValueDTM();
		creationTime.setName(new String256().setString("creationTime"));
		id = new String256();
		formatCode = new CodedTerm();
		hash = new String256();
		healthcareFacilityType = new CodedTerm();
		// languageCode = new String256();
		legalAuthenticator = new NameValueString256();
		legalAuthenticator.setName(new String256()
				.setString("legalAuthenticator"));
		mimeType = new String256();
		patientID = new IdentifierString256();
		practiceSettingCode = new CodedTerm();
		repoUId = new OID();
		serviceStartTime = new NameValueDTM();
		serviceStartTime.setName(new String256().setString("serviceStartTime"));
		serviceStopTime = new NameValueDTM();
		serviceStopTime.setName(new String256().setString("serviceStopTime"));
		size = new NameValueInteger();
		size.setName(new String256().setString("size"));
		sourcePatientId = new NameValueString256();
		sourcePatientId.setName(new String256().setString("sourcePatientId"));
		typeCode = new CodedTerm();
		uniqueId = new IdentifierOID();
		uri = new String256();

		titles = new ArrayList<InternationalString>();
		comments = new ArrayList<InternationalString>();
		authors = new ArrayList<Author>();
		confidentialityCodes = new ArrayList<CodedTerm>();
		eventCode = new ArrayList<CodedTerm>();
	}

	public ArrayList<InternationalString> getTitles() {
		return titles;
	}

	public void setTitles(ArrayList<InternationalString> titles) {
		this.titles = titles;
	}

	public ArrayList<InternationalString> getComments() {
		return comments;
	}

	public void setComments(ArrayList<InternationalString> comments) {
		this.comments = comments;
	}

	public ArrayList<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(ArrayList<Author> authors) {
		this.authors = authors;
	}

	public CodedTerm getClassCode() {
		return classCode;
	}

	public void setClassCode(CodedTerm classCode) {
		this.classCode = classCode;
	}

	public ArrayList<CodedTerm> getConfidentialityCodes() {
		return confidentialityCodes;
	}

	public void setConfidentialityCodes(
			ArrayList<CodedTerm> confidentialityCodes) {
		this.confidentialityCodes = confidentialityCodes;
	}

	public NameValueDTM getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(NameValueDTM creationTime) {
		this.creationTime = creationTime;
	}

	public String256 getId() {
		return id;
	}

	public void setId(String256 id) {
		this.id = id;
	}

	public ArrayList<CodedTerm> getEventCode() {
		return eventCode;
	}

	public void setEventCode(ArrayList<CodedTerm> eventCode) {
		this.eventCode = eventCode;
	}

	public CodedTerm getFormatCode() {
		return formatCode;
	}

	public void setFormatCode(CodedTerm formatCode) {
		this.formatCode = formatCode;
	}

	public String256 getHash() {
		return hash;
	}

	public void setHash(String256 hash) {
		this.hash = hash;
	}

	public CodedTerm getHealthcareFacilityType() {
		return healthcareFacilityType;
	}

	public void setHealthcareFacilityType(CodedTerm healthcareFacilityType) {
		this.healthcareFacilityType = healthcareFacilityType;
	}

	public LanguageCode getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(LanguageCode languageCode) {
		this.languageCode = languageCode;
	}

	public NameValueString256 getLegalAuthenticator() {
		return legalAuthenticator;
	}

	public void setLegalAuthenticator(NameValueString256 legalAuthenticator) {
		this.legalAuthenticator = legalAuthenticator;
	}

	public String256 getMimeType() {
		return mimeType;
	}

	public void setMimeType(String256 mimeType) {
		this.mimeType = mimeType;
	}

	public IdentifierString256 getPatientID() {
		return patientID;
	}

	public void setPatientID(IdentifierString256 patientID) {
		this.patientID = patientID;
	}

	public CodedTerm getPracticeSettingCode() {
		return practiceSettingCode;
	}

	public void setPracticeSettingCode(CodedTerm practiceSettingCode) {
		this.practiceSettingCode = practiceSettingCode;
	}

	public OID getRepoUId() {
		return repoUId;
	}

	public void setRepoUId(OID repoUId) {
		this.repoUId = repoUId;
	}

	// public String256 getRepositoryUniqueId() {
	// return repoUId;
	// }
	//
	// public void setRepositoryUniqueId(OID repositoryUniqueId) {
	// this.repoUId = repositoryUniqueId;
	// }

	public NameValueDTM getServiceStartTime() {
		return serviceStartTime;
	}

	public void setServiceStartTime(NameValueDTM serviceStartTime) {
		this.serviceStartTime = serviceStartTime;
	}

	public NameValueDTM getServiceStopTime() {
		return serviceStopTime;
	}

	public void setServiceStopTime(NameValueDTM serviceStopTime) {
		this.serviceStopTime = serviceStopTime;
	}

	public NameValueInteger getSize() {
		return size;
	}

	public void setSize(NameValueInteger size) {
		this.size = size;
	}

	public NameValueString256 getSourcePatientId() {
		return sourcePatientId;
	}

	public void setSourcePatientId(NameValueString256 sourcePatientId) {
		this.sourcePatientId = sourcePatientId;
	}

	public CodedTerm getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(CodedTerm typeCode) {
		this.typeCode = typeCode;
	}

	public IdentifierOID getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(IdentifierOID uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String256 getUri() {
		return uri;
	}

	public void setUri(String256 uri) {
		this.uri = uri;
	}

	/**
	 * <p>
	 * <b>Method toXML</b> <br>
	 * This method will be called to build a XML file with the information taken
	 * from the local DocumentModel.<br>
	 * It uses methods toXML() from each model class.
	 * </p>
	 * 
	 * 
	 * @return String which contains the document in XML format
	 * 
	 * @see DocumentModel class DocumentModel
	 */
	public String toXML() {
		StringBuilder answer = new StringBuilder();
		answer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Document>\n");
		System.out.println(getTitles());
		if (!(getTitles() == null)) {
			answer.append("\t<titles>\n");
			for (InternationalString str : titles) {
				answer.append(str.toXML());
			}
			answer.append("\t</titles>\n");
		}

		if (!getComments().equals(null)) {
			answer.append("\t<comments>\n");
			for (InternationalString str : comments) {
				answer.append(str.toXML());
			}
			answer.append("\t</comments>\n");
		}

		if (!getAuthors().equals(null)) {
			answer.append("\t<authors>\n");
			for (Author auth : authors) {
				answer.append(auth.toXML());
			}
			answer.append("\t</authors>\n");
		}

		answer.append("\t<classcode>\n");
		answer.append(classCode.toXML());
		answer.append("\t</classcode>\n\t<confidentialitycode>\n");

		for (CodedTerm ct : confidentialityCodes) {
			answer.append(ct.toXML());
		}
		answer.append("\t</confidentialitycode>\n");

		answer.append("\t<creationtime>\n");
		answer.append(creationTime.toXML());
		answer.append("\t</creationtime>\n");

		answer.append("\t<id>");
		answer.append(id.toString());
		answer.append("</id>\n");

		if (!getEventCode().equals(null)) {
			answer.append("\t<eventcode>\n");
			for (CodedTerm ct : eventCode) {
				answer.append(ct.toXML());
			}
			answer.append("\t</eventcode>\n");
		}

		answer.append("\t<formatcode>\n");
		answer.append(formatCode.toXML());
		answer.append("\t</formatcode>\n");

		if (!getHash().equals(null)) {
			answer.append("\t<hash>");
			answer.append(hash.toString());
			answer.append("</hash>\n");
		}

		answer.append("\t<healthcarefacilitytype>\n");
		answer.append(healthcareFacilityType.toXML());
		answer.append("\t</healthcarefacilitytype>\n");

		answer.append("\t<languagecode>");
		answer.append(languageCode.toString());
		answer.append("</languagecode>\n");

		if (!getLegalAuthenticator().equals(null)) {
			answer.append("\t<legalauthenticator>\n");
			answer.append(legalAuthenticator.toXML());
			answer.append("\t</legalauthenticator>\n");
		}

		answer.append("\t<mimetype>");
		answer.append(mimeType.toString());
		answer.append("</mimetype>\n");

		answer.append("\t<patientid>\n");
		answer.append(patientID.toXML());
		answer.append("\t</patientid>\n");

		answer.append("\t<practicesettingcode>\n");
		answer.append(practiceSettingCode.toXML());
		answer.append("\t</practicesettingcode>\n");

		if (!getRepoUId().equals(null)) {
			answer.append("\t<repositoryuniqueid>");
			answer.append(repoUId.toString());
			answer.append("</repositoryuniqueid>\n");
		}

		if (!getServiceStartTime().equals(null)) {
			answer.append("\t<servicestarttime>\n");
			answer.append(serviceStartTime.toXML());
			answer.append("\t</servicestarttime>\n");
		}

		if (!getServiceStopTime().equals(null)) {
			answer.append("\t<servicestoptime>\n");
			answer.append(serviceStopTime.toXML());
			answer.append("\t</servicestoptime>\n");
		}

		if (!getSize().equals(null)) {
			answer.append("\t<size>\n");
			answer.append(size.toXML());
			answer.append("\t</size>\n");
		}

		if (!getSourcePatientId().equals(null)) {
			answer.append("\t<sourcepatientid>\n");
			answer.append(sourcePatientId.toXML());
			answer.append("\t</sourcepatientid>\n");
		}

		answer.append("\t<typecode>\n");
		answer.append(typeCode.toXML());
		answer.append("\t</typecode>\n");

		answer.append("\t<uniqueid>\n");
		answer.append(uniqueId.toXML());
		answer.append("\t</uniqueid>\n");

		if (!getUri().equals(null)) {
			answer.append("\t<uri>");
			answer.append(uri.toString());
			answer.append("</uri>\n");
		}

		answer.append("</Document>");

		String newXmlFile = answer.toString().replaceAll("&", "&amp;");
		return newXmlFile;
	}

	/**
	 * <p>
	 * <b>Method verify</b> <br>
	 * This method will be called to check syntax's document<br>
	 * It uses methods verify() from each model class</br> TODO (and save all
	 * error message in validationError.)
	 * </p>
	 * 
	 * 
	 * @return boolean true if all the document is available, else return false
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 * @see DocumentModel
	 * @see #validationErrors
	 */
	public boolean verify() throws String256Exception {
		// FIXME verification issue answer isn't well assigned along the process
		boolean answer = true;
		int total = 0;
		for (InternationalString is : titles) {
			if (!is.verify()) {
				ArrayList<String> titleError = new ArrayList<String>();
				titleError.set(0, "title");
				// titleError
				// .set(1,
				// "message d'erreur, ou appel de fonction qui analyse la faute dans la classe InternationalString et retourne le msg d'erreur, ou encore intervention d' exception");

				// titleError.set(1, is.getError());

				validationErrors.set(total, titleError);
				total = +1;
			}
		}

		for (InternationalString is : comments) {
			answer = is.verify();
		}

		for (Author auth : authors) {
			answer = auth.verify();
		}

		answer = classCode.verify();
		for (CodedTerm ct : confidentialityCodes) {
			answer = ct.verify();
		}

		answer = creationTime.verify();
		answer = id.verify();

		for (CodedTerm ct : eventCode) {
			answer = ct.verify();
		}

		answer = formatCode.verify();
		answer = hash.verify();
		answer = healthcareFacilityType.verify();
		// answer = languageCode.verify();
		answer = legalAuthenticator.verify();
		answer = mimeType.verify();
		answer = patientID.verify();
		answer = practiceSettingCode.verify();
		answer = repoUId.verify();
		answer = serviceStartTime.verify();
		answer = serviceStopTime.verify();
		answer = size.verify();
		answer = sourcePatientId.verify();
		answer = typeCode.verify();
		answer = uniqueId.verify();
		answer = uri.verify();

		return answer;
	}

	@Override
	public String toString() {
		return "Model [Class Code= Code: " + classCode.getCode().toString()
				+ "Display Name: " + classCode.getDisplayName().toString()
				+ "CodingScheme: "
				+ classCode.getCodingScheme().getCodingScheme().toString()
				+ ", Patient ID= ID Type: " + patientID.getIdType().toString()
				+ "Value: " + patientID.getValue().toString() + "]";
	}

}
