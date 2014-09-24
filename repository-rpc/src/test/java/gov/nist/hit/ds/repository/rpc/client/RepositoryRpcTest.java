package gov.nist.hit.ds.repository.rpc.client;

import gov.nist.hit.ds.dsSims.factories.MessageValidatorFactory;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.rpc.presentation.PresentationData;
import gov.nist.hit.ds.repository.rpc.search.client.QueryParameters;
import gov.nist.hit.ds.repository.shared.PropertyKey;
import gov.nist.hit.ds.repository.shared.SearchCriteria;
import gov.nist.hit.ds.repository.shared.SearchCriteria.Criteria;
import gov.nist.hit.ds.repository.shared.SearchTerm;
import gov.nist.hit.ds.repository.shared.SearchTerm.Operator;
import gov.nist.hit.ds.repository.shared.aggregation.AssertionAggregation;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.shared.data.CSVRow;
import gov.nist.hit.ds.repository.shared.id.AssetId;
import gov.nist.hit.ds.repository.shared.id.RepositoryId;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.toolkit.installation.Installation;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RepositoryRpcTest {


    @BeforeClass
    public static void initialize() throws Throwable {

        // Setup application-wide singletons

        Installation.reset();
        Installation.installation().initialize();

        // Test assets created will be automatically removed after the test suite run is complete

        Configuration.configuration();


        File dataDir = Configuration.getRepositoriesDataDir(Configuration.getRepositorySrc(Access.RW_EXTERNAL));

//        if (dataDir.exists()) {
//            System.out.println("Clearing TEST data before testing...");
//            FileUtils.cleanDirectory(dataDir);
//        }

    }
	
	@Test
	public void sortMapTest() {
		
		Properties p = new Properties();
		
		p.setProperty("x", "1");
		p.setProperty("a", "2");
		p.setProperty("y", "3");
		p.setProperty("l", "4");


		System.out.println("******" + PresentationData.getSortedMapString(p));

	}

    @Test
    public void saveMultipleBaseCriteriaTest() throws RepositoryException {

		/* All Search criteria constructions are based on bottom-up approach
		 * Idea is to build parts and assemble them together
		 * Example:
		 build sub criteria A
		 build sub criteria B
		 build final criteria based on A and B
		 */


        SearchCriteria subCriteriaA = new SearchCriteria(SearchCriteria.Criteria.AND);

        subCriteriaA.append(new SearchTerm("patientId", SearchTerm.Operator.EQUALTOANY,new String[]{"101","102"}));
        subCriteriaA.append(new SearchTerm("documentType", SearchTerm.Operator.EQUALTO,"C32"));

        subCriteriaA.append(new SearchTerm("status",Operator.EQUALTOANY,new String[]{"Pending","Unknown"}));

        subCriteriaA.append(new SearchTerm("startDate",Operator.GREATERTHAN,"20070101"));
        subCriteriaA.append(new SearchTerm("startDate",Operator.LESSTHANOREQUALTO,"20080101"));


        SearchCriteria subCriteriaB = new SearchCriteria(Criteria.AND);
        subCriteriaB.append(new SearchTerm("patientId",Operator.EQUALTOANY,new String[]{"103","104","105"}));
        subCriteriaB.append(new SearchTerm("documentType",Operator.EQUALTO,"C64"));

        SearchCriteria criteria = new SearchCriteria(Criteria.OR);
        criteria.append(subCriteriaA);
        criteria.append(subCriteriaB);

        System.out.println(criteria.toString());


        final Access acs = Access.RW_EXTERNAL;

        RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(acs));

        Repository repos = null;
        try {
            repos = fact.getRepository(new SimpleId("repos-search-target"));
        } catch (Exception ex) {
            // Fine
        }


        if (repos==null) {
             repos = fact.createNamedRepository(
                    "repos-search-target",
                    "Description",
                    new SimpleType("simpleRepos"),
                    "repos-search-target"
            );
        }


        String[][] selectedRepos = new String[1][2];
        selectedRepos[0][0] = repos.getId().getIdString();
        selectedRepos[0][1] = acs.name();

        QueryParameters qp = new QueryParameters(selectedRepos,criteria);
        qp.setName("firstTest");

        AssetNode an = PresentationData.saveSearchCriteria(qp);

        System.out.println(an.getAssetId());

        QueryParameters qp2 = PresentationData.getSearchCriteria(an.getRepId(),an.getReposSrc(), an.getLocation());

        assertTrue(qp.getSelectedRepos().length==qp2.getSelectedRepos().length);
        assertTrue(qp.getSearchCriteria().toString().equals(qp2.getSearchCriteria().toString()));

    }

    @Test
    public void testMessageValidation() {
//        AssetNode transaction = new AssetNode();
//        transaction.setRepId(requestViewerWidget.getRepId());
//        transaction.setReposSrc(requestViewerWidget.getRepositorySrc());
//        transaction.setAssetId(requestViewerWidget.getIoHeaderId());


        MessageValidatorFactory validatorFactory = new MessageValidatorFactory();
        List<String> names = validatorFactory.getMessageValidator().getMessageValidatorNames();

        for (String name : names) {
            System.out.println("Got validator name: " + name);
        }
    }



    @Test
    public void testAssertionAggregation()  {
        /**
         * local test only
         * test resources
         */
        String eventId = "f721daed-d17c-4109-b2ad-c1e4a8293281"; // "052c21b6-18c2-48cf-a3a7-f371d6dd6caf";
        String type = "validators";
//        String[] displayColumns = new String[]{"ID","STATUS","MSG"};

         try {


             aggregateAssertions(eventId, null);

         } catch (Exception ex) {
                fail(ex.toString());
         }


    }



    @Test
    public void testAssertionAggregationByCriteria()  {
        /**
         * local test only
         * C:\e\artrep_test_resources\Installation\IHE-Testing\xdstools2_environment\repositories\data\Sim\123\Events\2014_07_29_13_17_30_089
         */
        String eventId = "f721daed-d17c-4109-b2ad-c1e4a8293281"; // "052c21b6-18c2-48cf-a3a7-f371d6dd6caf";
        String type = "validators";
//        String[] displayColumns = new String[]{"ID","STATUS","MSG"};

        try {


            SearchCriteria criteria = new SearchCriteria(Criteria.AND);
            criteria.append(new SearchTerm(PropertyKey.STATUS, Operator.EQUALTO, "ERROR"));


            aggregateAssertions(eventId, criteria);

        } catch (Exception ex) {
            fail(ex.toString());
        }


    }

    private void aggregateAssertions(String eventId, SearchCriteria criteria) throws RepositoryException {
        AssertionAggregation assertionAggregation =  PresentationData.aggregateAssertions(new RepositoryId("Sim"), new AssetId(eventId), criteria);

        List<CSVRow> rows =  assertionAggregation.getRows();


        for (CSVRow row : rows) {
            if (row.getRowNumber()==1) {
                System.out.println("assetId: " + row.getAssetId());
            }
            System.out.print("rowNum: "  + row.getRowNumber());
            for (String col : row.getColumns()) {
                System.out.print(" " + col + " ");
            }
            System.out.println("");

        }
    }


}
