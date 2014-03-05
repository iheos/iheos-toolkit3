package gov.nist.hit.ds.repository.api;

import gov.nist.hit.ds.repository.api.ObjectIterator;
import gov.nist.hit.ds.repository.api.Type;

/**
 * Properties is a mechanism for returning read-only data about an object.  An
 * object can have data associated with a PropertiesType.  For each
 * PropertiesType, there are Properties which are Serializable values
 * identified by a key.
 * 
 * <p>
 * OSID Version: 2.0
 * </p>
 * 
 * <p>
 * Licensed under the {@link org.osid.SidLicense MIT
 * O.K.I&#46; OSID Definition License}.
 * </p>
 */
public interface Properties extends java.io.Serializable {
    /**
     * Get the Type for this Properties instance.
     *
     * @return Type
     *
     * @throws gov.nist.hit.ds.repository.api.SharedException An exception with one of the
     *         following messages defined in gov.nist.hit.ds.repository.api.SharedException
     *         may be thrown:  {@link
     *         gov.nist.hit.ds.repository.api.SharedException#UNKNOWN_TYPE UNKNOWN_TYPE},
     *         {@link gov.nist.hit.ds.repository.api.SharedException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         gov.nist.hit.ds.repository.api.SharedException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         gov.nist.hit.ds.repository.api.SharedException#UNIMPLEMENTED UNIMPLEMENTED}
     */
    Type getType() throws gov.nist.hit.ds.repository.api.RepositoryException;

    /**
     * Get the Property associated with this key.
     *
     * @return java.io.Serializable
     *
     * @throws gov.nist.hit.ds.repository.api.SharedException An exception with one of the
     *         following messages defined in gov.nist.hit.ds.repository.api.SharedException
     *         may be thrown:  {@link
     *         gov.nist.hit.ds.repository.api.SharedException#UNKNOWN_TYPE UNKNOWN_TYPE},
     *         {@link gov.nist.hit.ds.repository.api.SharedException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         gov.nist.hit.ds.repository.api.SharedException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         gov.nist.hit.ds.repository.api.SharedException#UNIMPLEMENTED UNIMPLEMENTED},
     *         {@link gov.nist.hit.ds.repository.api.SharedException#UNKNOWN_KEY UNKNOWN_KEY}
     */
    java.io.Serializable getProperty(java.io.Serializable key)
        throws gov.nist.hit.ds.repository.api.RepositoryException;

    /**
     * Get the Keys associated with these Properties.
     *
     * @return ObjectIterator
     *
     * @throws gov.nist.hit.ds.repository.api.SharedException An exception with one of the
     *         following messages defined in gov.nist.hit.ds.repository.api.SharedException
     *         may be thrown:  {@link
     *         gov.nist.hit.ds.repository.api.SharedException#UNKNOWN_TYPE UNKNOWN_TYPE},
     *         {@link gov.nist.hit.ds.repository.api.SharedException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         gov.nist.hit.ds.repository.api.SharedException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         gov.nist.hit.ds.repository.api.SharedException#UNIMPLEMENTED UNIMPLEMENTED}
     */
    ObjectIterator getKeys() throws gov.nist.hit.ds.repository.api.RepositoryException;

    /**
     * <p>
     * MIT O.K.I&#46; SID Definition License.
     * </p>
     * 
     * <p>
     * <b>Copyright and license statement:</b>
     * </p>
     * 
     * <p>
     * Copyright &copy; 2003 Massachusetts Institute of     Technology &lt;or
     * copyright holder&gt;
     * </p>
     * 
     * <p>
     * This work is being provided by the copyright holder(s)     subject to
     * the terms of the O.K.I&#46; SID Definition     License. By obtaining,
     * using and/or copying this Work,     you agree that you have read,
     * understand, and will comply     with the O.K.I&#46; SID Definition
     * License.
     * </p>
     * 
     * <p>
     * THE WORK IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY     KIND, EXPRESS
     * OR IMPLIED, INCLUDING BUT NOT LIMITED TO     THE WARRANTIES OF
     * MERCHANTABILITY, FITNESS FOR A     PARTICULAR PURPOSE AND
     * NONINFRINGEMENT. IN NO EVENT SHALL     MASSACHUSETTS INSTITUTE OF
     * TECHNOLOGY, THE AUTHORS, OR     COPYRIGHT HOLDERS BE LIABLE FOR ANY
     * CLAIM, DAMAGES OR     OTHER LIABILITY, WHETHER IN AN ACTION OF
     * CONTRACT, TORT     OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
     * WITH     THE WORK OR THE USE OR OTHER DEALINGS IN THE WORK.
     * </p>
     * 
     * <p>
     * <b>O.K.I&#46; SID Definition License</b>
     * </p>
     * 
     * <p>
     * This work (the &ldquo;Work&rdquo;), including any     software,
     * documents, or other items related to O.K.I&#46;     SID definitions, is
     * being provided by the copyright     holder(s) subject to the terms of
     * the O.K.I&#46; SID     Definition License. By obtaining, using and/or
     * copying     this Work, you agree that you have read, understand, and
     * will comply with the following terms and conditions of     the
     * O.K.I&#46; SID Definition License:
     * </p>
     * 
     * <p>
     * You may use, copy, and distribute unmodified versions of     this Work
     * for any purpose, without fee or royalty,     provided that you include
     * the following on ALL copies of     the Work that you make or
     * distribute:
     * </p>
     * 
     * <ul>
     * <li>
     * The full text of the O.K.I&#46; SID Definition License in a location
     * viewable to users of the redistributed Work.
     * </li>
     * </ul>
     * 
     * 
     * <ul>
     * <li>
     * Any pre-existing intellectual property disclaimers, notices, or terms
     * and conditions. If none exist, a short notice similar to the following
     * should be used within the body of any redistributed Work:
     * &ldquo;Copyright &copy; 2003 Massachusetts Institute of Technology. All
     * Rights Reserved.&rdquo;
     * </li>
     * </ul>
     * 
     * <p>
     * You may modify or create Derivatives of this Work only     for your
     * internal purposes. You shall not distribute or     transfer any such
     * Derivative of this Work to any location     or any other third party.
     * For purposes of this license,     &ldquo;Derivative&rdquo; shall mean
     * any derivative of the     Work as defined in the United States
     * Copyright Act of     1976, such as a translation or modification.
     * </p>
     * 
     * <p>
     * THE WORK PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,     EXPRESS OR
     * IMPLIED, INCLUDING BUT NOT LIMITED TO THE     WARRANTIES OF
     * MERCHANTABILITY, FITNESS FOR A PARTICULAR     PURPOSE AND
     * NONINFRINGEMENT. IN NO EVENT SHALL     MASSACHUSETTS INSTITUTE OF
     * TECHNOLOGY, THE AUTHORS, OR     COPYRIGHT HOLDERS BE LIABLE FOR ANY
     * CLAIM, DAMAGES OR     OTHER LIABILITY, WHETHER IN AN ACTION OF
     * CONTRACT, TORT     OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
     * WITH     THE WORK OR THE USE OR OTHER DEALINGS IN THE WORK.
     * </p>
     * 
     * <p>
     * The name and trademarks of copyright holder(s) and/or     O.K.I&#46; may
     * NOT be used in advertising or publicity     pertaining to the Work
     * without specific, written prior     permission. Title to copyright in
     * the Work and any     associated documentation will at all times remain
     * with     the copyright holders.
     * </p>
     * 
     * <p>
     * The export of software employing encryption technology     may require a
     * specific license from the United States     Government. It is the
     * responsibility of any person or     organization contemplating export
     * to obtain such a     license before exporting this Work.
     * </p>
     */
}
