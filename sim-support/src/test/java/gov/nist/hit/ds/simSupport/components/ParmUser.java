package gov.nist.hit.ds.simSupport.components;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent;
import gov.nist.hit.ds.soapSupport.SoapFaultException;

/**
 * Created by bmajur on 4/21/14.
 */
public class ParmUser implements ValComponent {
    String lang;

    public void setAssertionGroup(AssertionGroup ag) {

    }

    public void setLang(String lang) { this.lang = lang; }

    public String getLang() { return lang; }

    public void setEvent(Event event) {

    }

    public String getName() {
        return null;
    }

    public void setName(String name) {

    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {

    }

    public void run() throws SoapFaultException, RepositoryException {

    }

    public boolean showOutputInLogs() {
        return false;
    }
}
