package gov.nist.hit.ds.dsSims.direct

import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Fault
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import org.apache.axiom.om.OMElement

import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.xml.namespace.QName

/**
 * Created by bmajur on 2/24/15.
 */
class DirectAddrBlockValidator extends ValComponentBase {
    SimHandle simHandle
    OMElement soapHeader
    OMElement addrBlock
    OMElement from
    OMElement to

    static final env = 'http://www.w3.org/2003/05/soap-envelope'
    static final directAddr = 'urn:direct:addressing'
    static final roleQName = new QName(env, 'role', 'env')
    static final relayQName = new QName(env, 'relay', 'env')

    DirectAddrBlockValidator(SimHandle _simHandle, OMElement _soapHeader) {
        super(_simHandle.event)
        simHandle = _simHandle
        soapHeader = _soapHeader
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id="dab010",  msg="Direct Addr Block exists", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab010() throws SoapFaultException {
        Iterator it = soapHeader.getChildrenWithLocalName('addressBlock')
        if (it.hasNext()) {
            addrBlock = it.next()
            assertNotNull(addrBlock)
        } else {
            assertNotNull(null)
            quit()
        }
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id="dab020",  msg="Direct Addr Block has correct XML Namespace", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab020() throws SoapFaultException {
        assertEquals(directAddr, addrBlock.getNamespace().namespaceURI)
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id="dab030",  msg="Verify role attribute", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab030() throws SoapFaultException {
        assertEquals('urn:direct:addressing:destination', addrBlock.getAttributeValue(roleQName))
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id="dab040",  msg="Verify relay attribute", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab040() throws SoapFaultException {
        assertEquals('true', addrBlock.getAttributeValue(relayQName))
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id="dab050",  msg="Verify from element exists", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab050() throws SoapFaultException {
        Iterator it = addrBlock.getChildrenWithLocalName('from')
        if (it.hasNext()) {
            from = it.next()
            assertNotNull(from)
        } else {
            fail(' ')
        }
    }

    boolean hasFrom() { from }

    @Guard(methodNames = ['hasFrom'])
    @Fault(code=FaultCode.Sender)
    @Validation(id="dab060",  msg="Verify from element namespace", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab060() throws SoapFaultException {
        assertEquals(directAddr, from.getNamespace().namespaceURI)
    }

    public static boolean validateEmail(String email) {
        boolean valid = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            valid = false;
        }
        return valid;
    }

    @Guard(methodNames = ['hasFrom'])
    @Fault(code=FaultCode.Sender)
    @Validation(id="dab070",  msg="Verify from element has 'mailto:' prefix", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab070() throws SoapFaultException {
        def fromAddr = from.getText()
        try {
            def (mailTo, addr) = fromAddr.split(':')
            assertEquals('mailto', mailTo)
        }    catch (ArrayIndexOutOfBoundsException e) { fail( ' ')}
    }

    @Guard(methodNames = ['hasFrom'])
    @Fault(code=FaultCode.Sender)
    @Validation(id="dab075",  msg="Verify from element holds email address", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab075() throws SoapFaultException {
        def fromAddr = from.getText()
        def (mailTo, addr) = fromAddr.split(':')
        found(addr)
        if (!validateEmail(addr)) fail(' ')
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id="dab080",  msg="Verify to element exists", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab080() throws SoapFaultException {
        Iterator it = addrBlock.getChildrenWithLocalName('to')
        if (it.hasNext()) {
            to = it.next()
            assertNotNull(from)
        } else {
            fail(' ')
        }
    }

    boolean hasTo() { to }

    @Guard(methodNames = ['hasTo'])
    @Fault(code=FaultCode.Sender)
    @Validation(id="dab090",  msg="Verify to element namespace", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab090() throws SoapFaultException {
        assertEquals(directAddr, to.getNamespace().namespaceURI)
    }

    @Guard(methodNames = ['hasTo'])
    @Fault(code=FaultCode.Sender)
    @Validation(id="dab100",  msg="Verify to element has 'mailto:' prefix", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab100() throws SoapFaultException {
        def toAddr = to.getText()
        try {
            def (mailTo, addr) = toAddr.split(':')
            assertEquals('mailto', mailTo)
        }    catch (ArrayIndexOutOfBoundsException e) { fail( ' ')}
    }

    @Guard(methodNames = ['hasTo'])
    @Fault(code=FaultCode.Sender)
    @Validation(id="dab105",  msg="Verify to element holds email address", ref="XDR and XDM for Direct Messaging Specification section 4.1")
    public void dab105() throws SoapFaultException {
        def toAddr = to.getText()
        def (mailTo, addr) = toAddr.split(':')
        found(addr)
        if (!validateEmail(addr)) fail(' ')
    }
}
