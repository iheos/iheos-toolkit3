package gov.nist.hit.ds.eventLog.assertion
import gov.nist.hit.ds.repository.AssetHelper
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.repository.shared.ValidationLevel
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.utilities.csv.CSVEntry
import gov.nist.hit.ds.utilities.csv.CSVTable
import groovy.util.logging.Log4j
import org.apache.log4j.Logger


class AssertionGroupDAO {
    private static Logger log = Logger.getLogger(AssertionGroupDAO);
    Asset parentAsset
    AssertionGroup ag
    int order = 1

    AssertionGroupDAO(AssertionGroup _ag, Asset _parentAsset, int _order) {
        ag = _ag
        parentAsset = _parentAsset
        order = _order
        log.debug("New AssertionGroupDAO")
    }

    Asset getAsset() { ag.asset }

    // return AssertionGroup (csv file) Asset
    Asset save() throws RepositoryException {
        log.debug("AssertionGroupDAO - save")
        if (!ag.isLogable()) return null
        if (!ag.saveInLog) return null
        if (!ag.validatorName) return null
        if (!parentAsset) { log.debug('Not flushing - no parent asset'); return null }

        def assertions = (ag.validationLevel != ValidationLevel.INFO) ? assertions = scrubAssertions(ag.assertions) : ag.assertions

//        if (ag.validatorName == 'TopLevel') assert ag.validationLevel == ValidationLevel.ERROR && ag.worstStatus == AssertionStatus.SUCCESS
        if (ag.asset) {
            // update
            log.debug("AssertionGroupDAO - Setting status on ${ag.asset.getId().idString}")
            Asset a = ag.asset
            a.updateContent(asTable(assertions).toString().getBytes())
            return a
        } else {
            // create
            log.debug("AssertionGroupDAO - create")
            Asset a = AssetHelper.createChildAsset(parentAsset, ag.validatorName, "", new SimpleType("assertionGroup"))
            ag.asset = a
            log.debug("AssertionGroupDAO - order is ${order}")
            a.setOrder(order++)
            a.setContent(asTable(assertions).toString().getBytes(), "text/csv")
            return a
        }
    }

    // if a ...text assertion (msg) is an error, keep previous ... elements and previous no ... element
    static scrubAssertions(List<Assertion> aa) {
        def ra = aa.reverse()
        def partOfError = false
        return ra.findAll {
            if (isError(it)) {
                if(isContinuation(it)) partOfError = true
                return true
            }
            if (partOfError) {
                if (!isError(it) && !isContinuation(it)) partOfError = false
                return true
            }
            return false
        }.reverse()
    }

    static isContinuation(Assertion a) { a.msg.startsWith('.')}

    static isError(Assertion a) { a.status in [AssertionStatus.ERROR, AssertionStatus.FAULT, AssertionStatus.INTERNALERROR, AssertionStatus.WARNING]}

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
