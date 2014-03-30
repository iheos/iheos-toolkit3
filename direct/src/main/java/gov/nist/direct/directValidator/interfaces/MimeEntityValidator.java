/**
 This software was developed at the National Institute of Standards and Technology by employees
of the Federal Government in the course of their official duties. Pursuant to title 17 Section 105 of the
United States Code this software is not subject to copyright protection and is in the public domain.
This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
modified freely provided that any derivative works bear some notice that they are derived from it, and any
modified versions bear some notice that they have been modified.

Project: NWHIN-DIRECT
Authors: William Majurski
		 Frederic de Vaulx
		 Diane Azais
		 Julien Perugini
		 Antoine Gerardin
		
 */

package gov.nist.direct.directValidator.interfaces;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;

import javax.mail.Part;


public interface MimeEntityValidator  {
	
	
	// ************************************************
	// *********** MIME entity header checks **********
	// ************************************************
	
	/** 
	 * DTS 190, All Mime Header Fields, Required
	 */
	public void validateAllMimeHeaderFields(IAssertionGroup er, String header);
	
	/**
	 *  DTS 102a, MIME-Version
	 * @param er
	 * @param mimeVersion
	 */
	public void validateAllMIMEVersion(IAssertionGroup er, String mimeVersion);
		
	/**
	 * DTS 133-145-146, Content-Type, Required
	 */
	public void validateContentType(IAssertionGroup er, String contentType);
		
	/**
	 *  DTS 191, Content-Type Subtype, Required
	 */
	public void validateContentTypeSubtype(IAssertionGroup er, String subtype);
		
	/**
	 *  DTS 192, Content-Type name, Conditional
	 */
	public void validateContentTypeName(IAssertionGroup er, String contentTypeName);
		
	/**
	 *  DTS 193, Content-Type S/MIME-Type, Conditional
	 */
	public void validateContentTypeSMIMEType(IAssertionGroup er, String contentTypeSMIMEType);
		
	/**
	 *  DTS 137-140, Content-Type Boundary, Conditional
	 */
	public void validateContentTypeBoundary(IAssertionGroup er, String contentTypeBoundary);
		
	/**
	 *  DTS 156, Content-type Disposition, Conditional
	 */
	public void validateContentTypeDisposition(IAssertionGroup er, String contentTypeDisposition, String contentType);
		
	/**
	 *  DTS 161-194, Content-Disposition filename, Optional
	 * @param er
	 * @param content
	 */
	public void validateContentDispositionFilename(IAssertionGroup er, String content);
	
	/**
	 *  DTS 134-143, Content-Id, Optional
	 * @param er
	 * @param content
	 */
	public void validateContentId(IAssertionGroup er, String content);
	
	/**
	 *  DTS 135-142-144, Content-Description, Optional
	 * @param er
	 * @param content
	 */
	public void validateContentDescription(IAssertionGroup er, String content);
	
	/**
	 *  DTS 136-148-157, Content-Transfer-Encoding, Optional
	 * @param er
	 * @param content
	 */
	public void validateContentTransferEncodingOptional(IAssertionGroup er, String contentTransfertEncoding, String contentType);
	
	/**
	 *  DTS 138-149, Content-*, Optional
	 * @param er
	 * @param content
	 */
	public void validateContentAll(IAssertionGroup er, String content);
	
	
	// xxxxxxxxxxxxxxx MIME Body  xxxxxxxxxxxxxxx
	
	
	/**
	 *  DTS 195, Body, Required
	 * @param er
	 * @param body
	 */
	public void validateBody(IAssertionGroup er, Part p, String body);
	
	
}
