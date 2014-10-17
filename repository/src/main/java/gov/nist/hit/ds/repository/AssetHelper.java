package gov.nist.hit.ds.repository;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.Parameter;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
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
import gov.nist.hit.ds.repository.shared.id.SimpleTypeId;
import gov.nist.hit.ds.repository.simple.SimpleRepository;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.search.AssetNodeBuilder;
import gov.nist.hit.ds.repository.simple.search.SearchResultIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class contains asset related helper methods.
 */
public class AssetHelper {
    public static final int MAX_RESULTS = 500;
	static Logger logger = Logger.getLogger(AssetHelper.class.getName());
	
	public static Asset createChildAsset(Asset parent, String displayName, String description, SimpleType assetType) throws RepositoryException {
		logger.info("Creating <" + displayName + ">,  child of <" + parent.getId() + "> in repo <" + parent.getRepository() +">");
		
		RepositoryFactory fact = new RepositoryFactory(parent.getSource());
		
		Asset a = fact.getRepository(parent.getRepository()).createAsset(displayName, description, assetType);
		logger.info("Created <" + a.getId() + ">");
		a.setProperty(PropertyKey.PARENT_ID , parent.getId().getIdString());

		return parent.addChild(a);
//		return a;
	}


	/**
	 * Returns an iterator for an asset's immediate children 
	 * @return
	 */
	public static AssetIterator getChildren(final Asset a) throws RepositoryException {

        String idString = "";

        if (a.getId()!=null) {
            idString = a.getId().getIdString();
        }

        SearchCriteria criteria = new SearchCriteria(Criteria.AND);

        criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTOANY, new String[]{idString, a.getPropFileRelativePart()}));

		SimpleRepository repos = new SimpleRepository(a.getRepository());
		repos.setSource(a.getSource());		

		return new SearchResultIterator(new Repository[]{repos}, criteria, new PropertyKey[]{PropertyKey.DISPLAY_ORDER, PropertyKey.CREATED_DATE });
		
	}


    /**
     * Gets the immediate children of the parent but not the grandchildren, however, there is an indicator {@code HASCHILDREN} of the presence of children attached to the child node.
     * @param an The parent node.
     * @return Children
     * @throws RepositoryException
     */
    public static List<AssetNode> getImmediateChildren(AssetNode an) throws RepositoryException {
        return getImmediateChildren(an,null);

    }

    private static List<AssetNode> getImmediateChildren(AssetNode an, SearchCriteria searchCriteria) throws RepositoryException {
        Repository repos = RepositoryHelper.composeRepositoryObject(an.getRepId(), an.getReposSrc());

        AssetNodeBuilder anb = new AssetNodeBuilder();
        try {
            return anb.getImmediateChildren(repos, an, searchCriteria);
        } catch (RepositoryException re) {
            logger.warning(re.toString());
        }
        return null;

    }






    /**
     * Searches for the criteria in the specified repositories.
     * @param reposData An array of repositories.
     * @param sc The search criteria.
     * @return List of asset nodes containing the search results.
     */
    public static List<AssetNode> search(String[][] reposData, SearchCriteria sc) {

        ArrayList<AssetNode> result = new ArrayList<AssetNode>();

        try {

            Repository[] reposList = RepositoryHelper.getReposList(reposData);

            AssetIterator iter = null;

            iter = new SearchResultIterator(reposList, sc);

            int recordCt = 0;
            if (iter!=null && recordCt++ <= MAX_RESULTS) { // hard limit for now

                while (iter.hasNextAsset()) {
                    gov.nist.hit.ds.repository.api.Asset aSrc = iter.nextAsset();

                    AssetNode aDst = new AssetNode();

                    aDst.setRepId(aSrc.getRepository().getIdString());
                    aDst.setAssetId(aSrc.getId().getIdString());
                    aDst.setDescription(aSrc.getDescription());
                    aDst.setDisplayName(aSrc.getDisplayName());
                    aDst.setMimeType(aSrc.getMimeType());
                    aDst.setReposSrc(aSrc.getSource().getAccess().name());
                    aDst.setParentId(aSrc.getProperty(PropertyKey.PARENT_ID));
                    aDst.setCreatedDate(aSrc.getCreatedDate());
                    aDst.setColor(aSrc.getProperty(PropertyKey.COLOR)); // This is required for the target node to show up in the right color when the asset is clicked form the search result
                    if (aSrc.getPath()!=null) {
                        aDst.setLocation(aSrc.getPropFileRelativePart());
                        try {
                            if (aSrc.getContentFile()!=null && aSrc.getContentFile().exists()) {
                                aDst.setContentAvailable(true);
                            }
                        } catch (Exception ex) {
                            logger.warning("Content file not found?" + ex.toString());
                        }
                    }
                    result.add(aDst);
                }
            }

        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        return result;

    }



    /**
     * Gets the children of the parent.
     * @param an The parent node.
     * @return Parent with children.
     * @throws RepositoryException
     */
    public static AssetNode getChildren(AssetNode an) throws RepositoryException {
        Repository repos = RepositoryHelper.composeRepositoryObject(an.getRepId(), an.getReposSrc());

        AssetNodeBuilder anb = new AssetNodeBuilder(AssetNodeBuilder.Depth.CHILDREN);
        try {
            anb.getChildren(repos, an);
            return an;
        } catch (RepositoryException re) {
            logger.warning(re.toString());
        }
        return null;

    }




    /**
     *
     * @param repositoryId An external repository Id.
     * @param eventId The event id (usually the dated event).
     * @param parentAssetType The parent asset type.
     * @param detailAssetType The detail asset type.
     * @param detailAssetFilterCriteria A criteria to filter detail assets. For example, a criteria can be built to represent to mean filter by "status=ERROR." An empty criteria or a null value signifies no special restriction.
     * @param displayColumns Select the columns to display. A null value can be used display all columns.
     * @return
     * @throws gov.nist.hit.ds.repository.api.RepositoryException
     */
    public static AssertionAggregation aggregateAssertions(RepositoryId repositoryId, AssetId eventId, SimpleTypeId parentAssetType, SimpleTypeId detailAssetType, SearchCriteria detailAssetFilterCriteria, String[] displayColumns) throws RepositoryException {
        Parameter p = new Parameter("eventId");
        p.assertNotNull(eventId);
        p.setDescription("eventId.getId()");
        p.assertNotNull(eventId.getId());
        p.setDescription("Repository");
        p.assertNotNull(repositoryId);
        p.setDescription("repositoryId.getId()");
        p.assertNotNull(repositoryId.getId());

        AssertionAggregation assertionAggregation = new AssertionAggregation();

        logger.fine("entering agg main event " + eventId);

        SearchCriteria searchCriteria = new SearchCriteria(SearchCriteria.Criteria.AND);
        searchCriteria.append(new SearchTerm(PropertyKey.ASSET_ID, SearchTerm.Operator.EQUALTO, eventId.getId()));

        String[][] repository = new String[][]{{repositoryId.getId(), "RW_EXTERNAL"}};

        List<AssetNode> result = search(repository, searchCriteria);

        String resultSz = ((result==null)?"null":""+result.size());
        logger.fine("agg main event find success" + eventId + " result sz: " + resultSz);
        if (result.size()==1) { // Only one is expected
            AssetNode an = result.get(0);
            logger.fine("agg main event size match by Id success");

            List<AssetNode> children = getImmediateChildren(an);

                /* Events
                      -> child1
                      -> child2
                */
            logger.fine("Processing nodes... size:" + children.size());

            for (AssetNode child : children) {
                if (parentAssetType.toString().equals(child.getType())) { // validators
                    logger.info("processing..." + parentAssetType + " id:" + child.getAssetId());
                    try {
                        aggregateMessage(child, detailAssetType , assertionAggregation, detailAssetFilterCriteria, displayColumns); // "assertionGroup"
                    } catch (RepositoryException rce) {
                        logger.warning(rce.toString());
                    }

                }
            }
        } else {
            logger.severe("Event assetId did not match the expected size=1, got <" + resultSz + ">: Id:" + eventId);
        }

        logger.fine("assertionAggregation result size is: " + assertionAggregation.getRows().size());

        return assertionAggregation;
    }


    private static void aggregateMessage(final AssetNode an, final SimpleTypeId detailAssetType, final AssertionAggregation assertionAggregation, SearchCriteria detailAssetFilterCriteria, final String[] columnNames) throws RepositoryException {

        logger.fine("entering aggregateMessage: an displayName:" + an.getDisplayName() + " an type:" + an.getType() + " search type:" + detailAssetType + " mimeType:" + an.getMimeType() + " hasContent:" + an.isContentAvailable() +  " csv:" + (an.getCsv()!=null));
        if (detailAssetType.toString().equals(an.getType()) && "text/csv".equals(an.getMimeType()) && an.isContentAvailable() && an.getCsv()==null) {

            AssetNode resultNode = ContentHelper.getContent(an);

            logger.fine("retrieved content for:" + resultNode.getAssetId());
            String[][] csv = resultNode.getCsv();
            int rowLen = csv.length;
            int colLen = csv[0].length;

            Map<String,Integer> colIdxMap = new HashMap<String, Integer>();

            if (columnNames!=null) {
                // Index the column list
                for (int colNameIdx = 0; colNameIdx < colLen; colNameIdx++) {
                    for (int displayColIdx = 0; displayColIdx < columnNames.length; displayColIdx++) {
                        if ((!colIdxMap.containsKey(columnNames[displayColIdx]) && columnNames[displayColIdx].equals(csv[0][colNameIdx]))) {
                            colIdxMap.put(columnNames[displayColIdx],colNameIdx);
                        }
                    }
                }
            }

            logger.fine("indexed column size:" + colIdxMap.size());

//                String[][] aggregateList = new String[][];


            int columnSelectionLength =  colLen;
            if (columnNames!=null && columnNames.length>0) {
                columnSelectionLength = columnNames.length;
            }
            boolean subsetSelection = (columnSelectionLength!=colLen);


            // Populate the header
            if (assertionAggregation.getHeader()==null) {
                CSVRow headerRow = new CSVRow(columnSelectionLength);
                for (int col=0; col < columnSelectionLength; col++) { // Pick the first one, since we assume that all assertions are of the same type and have same column headers
                    int colIdx = col;
                    if (subsetSelection) {
                        colIdx = colIdxMap.get(columnNames[col]);
                    }
                    String value = csv[0][colIdx];
                    headerRow.setColumnValueByIndex(col,value);
                }
                assertionAggregation.setHeader(headerRow);
            }





            AssetId assetId = new AssetId(resultNode.getAssetId());
            assertionAggregation.getAssetNodeMap().put(assetId, resultNode); // Having it in a map doesn't necessary mean that all rows will be projected

            // Extract values
            // Populate the data rows

            boolean hasData = false;
            for (int rowNumber=1 /* skip header */; rowNumber < rowLen; rowNumber++) {
                CSVRow rowData = new CSVRow(columnSelectionLength);

                for (int col=0; col < columnSelectionLength; col++) {
                    hasData = true;
                    int colIdx = col;
                    if (subsetSelection) {
                        colIdx = colIdxMap.get(columnNames[col]);
                    }
                    String value = csv[rowNumber][colIdx];
                    rowData.setColumnValueByIndex(col,value);
                }
                rowData.setRowNumber(rowNumber);
                rowData.setAssetId(assetId);
                assertionAggregation.getRows().add(rowData);

            }
            logger.fine(assetId +  ": projected rows:" + rowLen);


        }


        if (an.getChildren().size() == 1 && "HASCHILDREN".equals(an.getChildren().get(0).getDisplayName())) {

            List<AssetNode> children = getImmediateChildren(an, detailAssetFilterCriteria);

            for (AssetNode child : children) {
                try {
                    aggregateMessage(child, detailAssetType, assertionAggregation, detailAssetFilterCriteria, columnNames);
                } catch (Exception ex) {
                    logger.warning(ex.toString());
                }
            }

        }

        return;
    }


}
