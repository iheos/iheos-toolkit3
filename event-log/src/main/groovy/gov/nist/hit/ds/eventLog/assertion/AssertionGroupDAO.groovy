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
    Asset assertionsAsset;
    int counter = 1;
    AssertionGroup ag = null;
    static Logger logger = Logger.getLogger(AssertionGroupDAO.class);

    public Asset init(Asset parent) throws RepositoryException {
        return AssetHelper.createChildAsset(parent, "Validators", "", new SimpleType("simAssertions"));
    }

    public void setAssertionGroup(AssertionGroup ag) throws RepositoryException {
        this.ag = ag;
    }

    public void flush() throws RepositoryException {
        logger.trace("AssertionGroup " + ag.toString());
        if (!ag.saveInLog) return;
        Asset a = AssetHelper.createChildAsset(assertionsAsset, ag.getValidatorName(), "", new SimpleType("simpleType"));
        a.setOrder(counter++);
        a.setProperty(PropertyKey.STATUS, ag.getWorstStatus().name());
        logger.trace("flushing CSVTable:");
        logger.trace(new AssertionGroupDAO(ag.getAssertions()).getTable().toString());
        a.updateContent(new AssertionGroupDAO(ag.getAssertions()).getTable().toString(), "text/csv");
    }

    AssertionStatus getAssertionStatus() { (ag == null) ? AssertionStatus.SUCCESS : ag.getWorstStatus(); }

    boolean hasErrors() { getAssertionStatus().isError(); }

    // Doesn't get called until the end to flush out
    CSVTable getTable(List<Assertion> assertions) {
        AssertionDAO aDao = new AssertionDAO()
        CSVTable assertionTable = new CSVTable();
        addRow(assertionTable, aDao.columnNames)
        assertions.each {
            assertionTable.add(aDao.asCVSEntry(it))
        }
        assertionTable;
    }

    private addRow(CSVTable assertionTable, List<String> values) {
        CSVEntry entry = new CSVEntry(values.size());
        for (int i=0; i<values.size(); i++) {
            entry.set(i, values.get(i));
        }
        assertionTable.add(entry);
    }

}
