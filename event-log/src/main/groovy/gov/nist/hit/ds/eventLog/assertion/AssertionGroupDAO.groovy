package gov.nist.hit.ds.eventLog.assertion;

import gov.nist.hit.ds.repository.AssetHelper;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.utilities.csv.CSVEntry
import gov.nist.hit.ds.utilities.csv.CSVTable;
import org.apache.log4j.Logger;

public class AssertionGroupDAO {
    Asset parent;
    Asset assertionGroupAsset
    int counter = 1;
    static Logger logger = Logger.getLogger(AssertionGroupDAO);

    public Asset init(Asset parent) throws RepositoryException {
        this.parent = parent
        assertionGroupAsset =  AssetHelper.createChildAsset(parent, "Validators", "", new SimpleType("simAssertions"))
        return assertionGroupAsset
    }

    public void setAssertionGroup(AssertionGroup ag) throws RepositoryException {
        this.ag = ag
    }

    public void save(AssertionGroup ag) throws RepositoryException {
        logger.trace("AssertionGroup " + ag.toString());
        if (!ag.saveInLog) return;
        Asset a = AssetHelper.createChildAsset(assertionGroupAsset, ag.getValidatorName(), "", new SimpleType("simpleType"))
        a.setOrder(counter++)
        a.setProperty(PropertyKey.STATUS, ag.getWorstStatus().name())
        logger.debug("flushing CSVTable")
        a.setContent(asTable(ag.assertions).toString(), "text/csv")
    }

    AssertionStatus getAssertionStatus() { (ag == null) ? AssertionStatus.SUCCESS : ag.getWorstStatus() }

    boolean hasErrors() { getAssertionStatus().isError() }

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
