package gov.nist.hit.ds.eventLog.assertion
import gov.nist.hit.ds.repository.AssetHelper
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.shared.PropertyKey
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.utilities.csv.CSVEntry
import gov.nist.hit.ds.utilities.csv.CSVTable
import groovy.util.logging.Log4j

@Log4j
public class AssertionGroupDAO {
    Asset parentAsset
    AssertionGroup ag
    int order = 1;

    AssertionGroupDAO(AssertionGroup _ag, Asset _parentAsset) {
        ag = _ag
        parentAsset = _parentAsset
    }
//    def init(Asset parentAsset) { this.parentAsset = parentAsset }

    Asset getAsset() { ag.asset }

    // return AssertionGroup (csv file) Asset
    public Asset save() throws RepositoryException {
        if (!ag.saveInLog) return null;
        if (!ag.validatorName) return null
        if (!parentAsset) { log.debug('Not flushing'); return null }
        if (ag.asset) {
            // update
            Asset a = ag.asset
            a.setProperty(PropertyKey.STATUS, ag.getWorstStatus().name())
            a.updateContent(asTable(ag.assertions).toString().getBytes())
//            a.setContent(asTable(ag.assertions).toString(), "text/csv")
            return a
        } else {
            // create
            Asset a = AssetHelper.createChildAsset(parentAsset, ag.validatorName, "", new SimpleType("assertionGroup"))
            ag.asset = a
            a.setOrder(order++)
            a.setProperty(PropertyKey.STATUS, ag.getWorstStatus().name())
            a.setContent(asTable(ag.assertions).toString().getBytes(), "text/csv")
            return a
        }
    }

    AssertionStatus worstAssertionStatus() { (ag == null) ? AssertionStatus.SUCCESS : ag.getWorstStatus() }

    boolean hasErrors() { worstAssertionStatus().isError() }

    CSVTable asTable(List<Assertion> assertions) {
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

    List<Assertion> load(Asset a) {
        String table = a.getContent().toString()
        def entries = []
        table.eachLine { line ->
            entries << new CSVEntry(line)
        }
        entries = entries[1..entries.size()-1]

        AssertionDAO dao = new AssertionDAO()
        return entries.collect { dao.asAssertion(it) }
    }

}
