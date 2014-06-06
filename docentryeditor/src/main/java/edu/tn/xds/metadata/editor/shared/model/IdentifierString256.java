package edu.tn.xds.metadata.editor.shared.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * <b> This class represents an Identifier.</b>
 * </p>
 * <p>
 * An Identifier has the following parameters:
 * <ul>
 * <li>{@link #value}: The value of the identifier ({@link String256}) ;</li>
 * <li>{@link #idType}: The id type ({@link String256});</li>
 * </li>
 * </ul>
 * </p>
 * <p>
 * </p>
 * <p>
 * It contains a toXML method to return the Identifier in XML format.<br>
 * This class also contains getters/setters.</br> In addition, it has verify
 * method to check its syntax.
 * </p>
 * 
 * <p>
 * <b>See below each method mentioned above.</b> <br>
 * {@link #verify() method verify}</br> {@link #toXML() method toXML} <br>
 * </p>
 * 
 * 
 * 
 * 
 * @see DocumentModel class DocumentModel
 * @see ModelElement class ModelElement <
 * 
 * 
 * 
 * 
 */
public class IdentifierString256 implements ModelElement, Serializable {

	private static final long serialVersionUID = -1717453248217705389L;

	/**
	 * <p>
	 * 
	 * <b>String256 value</b> - The value of the identifier [Mandatory].<br>
	 * Type: {@link String256}</br>
	 * </p>
	 * 
	 * 
	 * @see String256 class String256
	 * @see IdentifierString256
	 */
	@NotNull
	private String256 value;

	/**
	 * <b>String256 idType</b> - The type of the identifier [Mandatory].<br>
	 * Type: {@link String256}</br>
	 * 
	 * @see IdentifierString256
	 */
	@NotNull
	private String256 idType;

	public IdentifierString256(String256 string256, String256 idString256) {
		value = string256;
		idType = idString256;
	}

	public IdentifierString256() {
		value = new String256();
		idType = new String256();
	}

	public String256 getValue() {
		return value;
	}

	public void setValue(String256 value) {
		this.value = value;
	}

	public void setIdType(String256 element) {
		idType = element;
	}

	public String256 getIdType() {
		return idType;
	}

	/**
	 * 
	 * <p>
	 * <b>Method toXML</b> <br>
	 * This method will be called to build a XML file by the
	 * {@link DocumentModel} with the information taken from the local
	 * Identifier.<br/>
	 * </p>
	 * 
	 * @return String which contains the Identifier in XML format
	 * 
	 * @see IdentifierString256
	 */
	public String toXML() {
		String answer = null;
		answer = "\t\t<identifier>\n\t\t\t<value>" + value.getString() + "</value>\n\t\t\t<idtype>" + idType.toString()
				+ "</idtype>\n\t\t</identifier>\n";

		return answer;
	}

	/**
	 * <p>
	 * <b>Method verify</b> <br>
	 * This method will be called to check whether the syntax's
	 * {@link IdentifierString256} is correct </br>
	 * </p>
	 * 
	 * @return boolean true if the syntax is correct, else return false
	 * @throws String256Exception
	 *             if there is a String256 with more than 256 characters
	 * 
	 * @see IdentifierString256
	 * 
	 */
	@Override
	public boolean verify() throws String256Exception {
		boolean answer = true;
		answer = value.verify();
		answer = idType.verify();
		return answer;
	}

}
