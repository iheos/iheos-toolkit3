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

import gov.nist.hit.ds.errorRecording.IAssertionGroup;

import javax.mail.Address;


public interface MessageHeadersValidator{
	
	// ************************************************
	// *********** Message headers checks *************
	// ************************************************
	
	
	
	/**
	 *  DTS 196, All Headers, Required
	 */
	public void validateAllHeaders(IAssertionGroup er, String[] header, String[] headerContent, boolean wrapped);
	
	/**
	 *  DTS 103-105, Return Path, Conditional
	 * @param er
	 * @param returnPath
	 */
	public void validateReturnPath(IAssertionGroup er, String returnPath, boolean wrapped);
		
	/**
	 *  DTS 104-106, Received, Conditional
	 */
	public void validateReceived(IAssertionGroup er, String received, boolean wrapped);
	
	/**
	 *  DTS 197, Resent Fields, Required
	 */
	public void validateResentFields(IAssertionGroup er, String[] resentField, boolean wrapped);
	
	/**
	 *  DTS 107, Resent-Date, Conditional
	 */
	public void validateResentDate(IAssertionGroup er, String resentDate, boolean wrapped);
	
	/**
	 *  DTS 108, Resent-From, Conditional
	 */
	public void validateResentFrom(IAssertionGroup er, String resentFrom, boolean wrapped);
	
	/**
	 *  DTS 109, Resent-Sender, Conditional
	 */
	public void validateResentSender(IAssertionGroup er, String resentSender, String resentFrom, boolean wrapped);
	
	/**
	 *  DTS 110, Resent-to, Optional
	 * @param resentTo
	 */
	public void validateResentTo(IAssertionGroup er, String resentTo, boolean wrapped);
	
	/**
	 *  DTS 111, Resent-cc, Optional
	 * @param resentCc
	 */
	public void validateResentCc(IAssertionGroup er, String resentCc, boolean wrapped);
	
	/**
	 *  DTS 112, Resent-bcc, Optional
	 * @param resentBcc
	 */
	public void validateResentBcc(IAssertionGroup er, String resentBcc, boolean wrapped);
		
	/**
	 *  DTS 113, Resent-Msg-Id, Conditional
	 */
	public void validateResentMsgId(IAssertionGroup er, String resentMsgId, boolean wrapped);
	
	/**
	 *  DTS 114, Orig-Date, Required
	 */
	public void validateOrigDate(IAssertionGroup er, String origDate, boolean wrapped);
		
	/**
	 *  DTS 115, From, Required
	 */
	public void validateFrom(IAssertionGroup er, String from, boolean wrapped);
	
	/**
	 *  DTS 116, Sender, Conditional
	 * @param er
	 * @param sender
	 */
	public void validateSender(IAssertionGroup er, String sender, Address[] from, boolean wrapped);
		
	/**
	 *  DTS 117, Reply-To, Optional
	 * @param replyTo
	 */
	public void validateReplyTo(IAssertionGroup er, String replyTo, boolean wrapped);
		
	/**
	 *  DTS 118, To, Required
	 */
	public void validateTo(IAssertionGroup er, String to, boolean wrapped);
	
	/**
	 *  DTS 119, cc, Optional
	 * @param cc
	 */
	public void validateCc(IAssertionGroup er, String cc, boolean wrapped);
	
	/**
	 *  DTS 120, Bcc, Optional
	 * @param bcc
	 */
	public void validateBcc(IAssertionGroup er, String bcc, boolean wrapped);
		
	/**
	 *  DTS 121, Message-Id, Required
	 */
	public void validateMessageId(IAssertionGroup er, String messageId, boolean wrapped);
	
	/**
	 *  DTS 122, In-reply-to, Optional
	 * @param inReplyTo
	 */
	public void validateInReplyTo(IAssertionGroup er, String inReplyTo, String date, boolean wrapped);
	
	/**
	 *  DTS 123, References, Optional
	 * @param references
	 */
	public void validateReferences(IAssertionGroup er, String references, boolean wrapped);
	
	/**
	 *  DTS 124, Subject, Optional
	 * @param subject
	 * @param filename
	 */
	public void validateSubject(IAssertionGroup er, String subject, String filename, boolean wrapped);
	
	/**
	 *  DTS 125, Comments, Optional
	 * @param comments
	 */
	public void validateComments(IAssertionGroup er, String comments, boolean wrapped);
	
	/**
	 *  DTS 126, Keywords, Optional
	 * @param keyword
	 */
	public void validateKeywords(IAssertionGroup er, String keyword, boolean wrapped);
	
	/**
	 *  DTS 127, Optional-field, Optional
	 * @param optionalField
	 */
	public void validateOptionalField(IAssertionGroup er, String optionalField, boolean wrapped);
	
	/**
	 *  DTS 128, Disposition-Notification-To, Optional
	 * @param dispositionNotificationTo
	 */
	public void validateDispositionNotificationTo(IAssertionGroup er, String dispositionNotificationTo, boolean wrapped);
		
	/**
	 *  DTS 102b, MIME-Version, Required
	 */
	public void validateMIMEVersion(IAssertionGroup er, String MIMEVersion, boolean wrapped);
}
