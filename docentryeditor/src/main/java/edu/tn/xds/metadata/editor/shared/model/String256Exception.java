package edu.tn.xds.metadata.editor.shared.model;

/**
 * <p>
 * <b>This class represents a String256Exception</b> <br>
 * It throws when a String256 has more than 256 characters.</br>
 * </p>
 * 
 * 
 * @see String256 class String256
 * @see DocumentModel class DocumentModel
 */
@SuppressWarnings("serial")
public class String256Exception extends Exception {

	public String256Exception() {
		super(
				"This String souldn't be larger than 256 caracters\nCheck your xml document!");
	}
}
