package gov.nist.hit.ds.eventLog.assertion

import gov.nist.hit.ds.eventLog.EventDAO
import gov.nist.hit.ds.repository.AssetHelper
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.PropertyKey
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.utilities.csv.CSVEntry
import gov.nist.hit.ds.utilities.csv.CSVTable
import groovy.util.logging.Log4j

@Log4j
public class AssertionGroupDAO {
    Asset validatorsAsset
    int order = 1;
    EventDAO eventDAO

    void init(EventDAO eventDAO) throws RepositoryException {
        this.eventDAO = eventDAO
    }

    public void save(AssertionGroup ag) throws RepositoryException {
        if (!ag.saveInLog) return;
        assert ag.validatorName
        if (!eventDAO.validatorsAsset) { log.debug('Not flushing'); return }
        Asset a = AssetHelper.createChildAsset(eventDAO.validatorsAsset, ag.validatorName, "", new SimpleType("assertionGroup"))
        a.setOrder(order++)
        a.setProperty(PropertyKey.STATUS, ag.getWorstStatus().name())
        a.setContent(asTable(ag.assertions).toString(), "text/csv")
    }

    AssertionStatus worstAssertionStatus() { (ag == null) ? AssertionStatus.SUCCESS : ag.getWorstStatus() }

    boolean hasErrors() { worstAssertionStatus().isError() }

    def asTable(List<Assertion> assertions) {
        AssertionDAO aDao = new AssertionDAO()
        CSVTable assertionTable = new CSVTable()
        addRow(assertionTable, aDao.columnNames)
        assertions.each {
            assertionTable.add(aDao.asCVSEntry(it))
        }
        assertionTable
    }

    def addRow(CSVTable assertionTable, List<String> values) {
        CSVEntry entry = new CSVEntry(values.size())
        for (int i=0; i<values.size(); i++) {
            entry.set(i, values.get(i))
        }
        assertionTable.add(entry)
    }

}
