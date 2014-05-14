package gov.nist.hit.ds.simSupport.components;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.soapSupport.SoapFaultException;

/**
 * Created by bmajur on 4/21/14.
 */
public class ParmUser implements SimComponent {
    String lang;

    @Override
    public void setAssertionGroup(AssertionGroup ag) {

    }

    public void setLang(String lang) { this.lang = lang; }

    public String getLang() { return lang; }

    @Override
    public void setEvent(Event event) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void run() throws SoapFaultException, RepositoryException {

    }

    @Override
    public boolean showOutputInLogs() {
        return false;
    }
}
