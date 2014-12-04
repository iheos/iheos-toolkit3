package gov.nist.hit.ds.dsSims.eb.msgs
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.repository.AssetHelper
import gov.nist.hit.ds.repository.shared.PropertyKey
import gov.nist.hit.ds.repository.shared.SearchCriteria
import gov.nist.hit.ds.repository.shared.SearchTerm
import gov.nist.hit.ds.repository.shared.aggregation.AssertionAggregation
import gov.nist.hit.ds.repository.shared.data.CSVRow
import gov.nist.hit.ds.repository.shared.id.AssetId
import gov.nist.hit.ds.repository.shared.id.RepositoryId
import gov.nist.hit.ds.repository.shared.id.SimpleTypeId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.utilities.xml.XmlUtil
import groovy.util.logging.Log4j
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 10/7/14.
 */
// TODO Errors not extracted from Repository correctly
@Log4j
class RegistryResponseGenerator {
    SimHandle simHandle
    List<CSVRow> data
    String[] displayColumns = ["ID","STATUS", "EXPECTED", "FOUND", "MSG", "CODE"]
    def IDindex = 0
    def STATUSindex = 1
    def EXPECTEDindex = 2
    def FOUNDindex = 3
    def MSGindex = 4
    def CODEindex = 5

    def id(r) {r.columns[IDindex]}
    def status(r) {r.columns[STATUSindex]}
    def msg(r) {r.columns[MSGindex]}
    def code(r) {r.columns[CODEindex]}
    def found(r) {r.columns[FOUNDindex]}
    def expected(r) {r.columns[EXPECTEDindex]}

    def safe(String x) { x.replaceAll('<', '&lt;')}

    def statusToReturn = ['ERROR']

    RegistryResponseGenerator(SimHandle _simHandle) {
        simHandle = _simHandle
    }

    OMElement toXml() {
        queryForErrors()
        OMElement response = XmlUtil.om_factory.createOMElement("RegistryResponse", MetadataSupport.ebRSns3)
        response.addAttribute('status', getStatus(), null)
        if (data.size() > 0) {
            OMElement rel = XmlUtil.om_factory.createOMElement("RegistryErrorList", MetadataSupport.ebRSns3)
            response.addChild(rel)
            data.each { CSVRow r ->
                if (! (status(r) in statusToReturn)) return
                OMElement er = XmlUtil.om_factory.createOMElement("RegistryError", MetadataSupport.ebRSns3)
                rel.addChild(er)
                er.addAttribute('errorCode', '', null)
                er.addAttribute('codeContext', "EXPECTED: ${safe(expected(r))}; FOUND: ${safe(found(r))} : MSG ${safe(msg(r))}", null)
                er.addAttribute('location', code(r), null)
            }
        }
        return response
    }

    // TODO: This does not consider cross-community or Retrieve yet
    String getStatus() {
        if (data.size() > 0) return MetadataSupport.status_failure
        return MetadataSupport.status_success
    }

    def queryForErrors() {
        log.debug("Query Event for errors")

        // 1. Build a search criteria 'filter' (which can be null to indicate no filter restriction)
        SearchCriteria criteria = new SearchCriteria(SearchCriteria.Criteria.OR);
        criteria.append(new SearchTerm(
                PropertyKey.STATUS,
                SearchTerm.Operator.EQUALTOANY,
                statusToReturn as String[]
                // TODO removed WARNING since getStatus() sets status_failure - still need to report
        ));

        log.debug("Event asset is ${simHandle.event.eventAsset.id.idString}")

        // 2. Call the API and get the AssertionAggregation result
        AssertionAggregation assertionAggregation = AssetHelper.aggregateAssertions(
                new RepositoryId(simHandle.repository.getId().idString),
                new AssetId(simHandle.event.eventAsset.id.idString),
                new SimpleTypeId("validators"),
                new SimpleTypeId("assertionGroup"),
                criteria,
                displayColumns)

        // 3. Use the result
        data = assertionAggregation.getRows()
        log.debug("Found ${data.size()} errors to report")
    }

}
