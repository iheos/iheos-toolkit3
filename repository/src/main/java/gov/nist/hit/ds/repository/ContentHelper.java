package gov.nist.hit.ds.repository;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.shared.PropertyKey;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.utilities.csv.CSVEntry;
import gov.nist.hit.ds.utilities.csv.CSVParser;
import gov.nist.hit.ds.utilities.xml.XmlFormatter;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * This class contains content related helper methods.
 * Created by skb1 on 10/1/14.
 */
public class ContentHelper {

    static Logger logger = Logger.getLogger(ContentHelper.class.getName());

    /**
     * Sorts the properties map into text format.
     * @param p The properties object.
     * @return The sorted String.
     */
    public static String getSortedMapString(java.util.Properties p) {

        if (p==null) return "";

        StringBuffer sb = new StringBuffer();

        SortedMap<String, String> smap = new TreeMap<String,String>();

        for (String key : p.stringPropertyNames()) {
            smap.put(key, p.getProperty(key));
        }

        Iterator<String> iterator =  smap.keySet().iterator();
        while (iterator.hasNext()) {
            String propName = (String) iterator.next();
            sb.append(propName + "=" + p.getProperty(propName) + System.getProperty("line.separator"));
        }
        return sb.toString();
    }


    /**
     * Gets the text content of an asset.
     * @param an The asset node.
     * @return A new asset node with possible text content.
     * @throws gov.nist.hit.ds.repository.api.RepositoryException
     */
    public static AssetNode getContent(AssetNode an) throws RepositoryException {
        Repository repos = RepositoryHelper.composeRepositoryObject(an.getRepId(), an.getReposSrc());

        Asset aSrc = null;
        try {
            if (an.getRelativePath()!=null) {
                aSrc = repos.getAssetByRelativePath(new File(an.getRelativePath()));

            } else if (an.getFullPath()!=null) {
                aSrc = repos.getAssetByPath(new File(an.getFullPath()));
            }
        } catch (Exception ex) {
               logger.info(ex.toString());
        }


        if (aSrc==null) {
            throw new RepositoryException(RepositoryException.IO_ERROR + "Asset not found by either location: " + an.getRelativePath() + " fullPath:" + an.getFullPath());
        }

		/* this should not be required
		 * else if (an.getAssetId()!=null) {

			aSrc = repos.getAsset(new SimpleId(an.getAssetId()));
		}
		*/

        AssetNode aDst = new AssetNode();

        aDst.setRepId(aSrc.getRepository().getIdString());
        aDst.setAssetId(aSrc.getId().getIdString());
        aDst.setParentId(aSrc.getProperty(PropertyKey.PARENT_ID));
        if (aSrc.getAssetType()!=null)
            aDst.setType(aSrc.getAssetType().getKeyword());
        aDst.setDescription(aSrc.getDescription());
        aDst.setDisplayName(aSrc.getDisplayName());
        aDst.setMimeType(aSrc.getMimeType());
        aDst.setReposSrc(aSrc.getSource().getAccess().name());
        aDst.setColor(aSrc.getProperty(PropertyKey.COLOR));
        aDst.setExtendedProps(an.getExtendedProps()); // This is from the original assetNode

        if (aSrc.getPath()!=null)
            aDst.setFullPath(aSrc.getPath().toString());

        aDst.setRelativePath(aSrc.getPropFileRelativePart());

        if (aSrc.getContent()!=null) {
            logger.fine("*** has got content" + an.getType() + " aSrc.getMimeType():" + aSrc.getMimeType());
            try {
                String content = new String(aSrc.getContent());
                aDst.setRawContent(aSrc.getContent());
                if (an.getType()!=null && an.getType().startsWith("raw")) {
                    content = SafeHtmlUtils.fromString(content).asString();
                    aDst.setTxtContent(content);
                } else if ("text/xml".equals(aSrc.getMimeType()) || "application/soap+xml".equals(aSrc.getMimeType())) {
                    aDst.setTxtContent(XmlFormatter.htmlize(content));
                } else if ("text/csv".equals(aSrc.getMimeType())) {
                    aDst.setCsv(processCsvContent(content));


                    // Transfer a property from its type to an individual asset level
                    if (aDst.getExtendedProps()!=null && aSrc.getAssetTypeProperties()!=null) {

                        // Debug
//                        logger.info(aSrc.getAssetTypeProperties().getProperties().getProperty(PropertyKey.DESCRIPTION.toString()));
//                        logger.info(aSrc.getAssetTypeProperties().getProperties().getProperty(PropertyKey.COLUMN_HEADER_WIDTHS.toString()));

                        aDst.getExtendedProps().put(PropertyKey.COLUMN_HEADER_WIDTHS.toString()
                                , aSrc.getAssetType().getProperties().getProperty(PropertyKey.COLUMN_HEADER_WIDTHS.toString()));
                    }
                } else if ("text/json".equals(aSrc.getMimeType())) {
                    aDst.setTxtContent(prettyPrintJson(content));
                } else {
                    content = SafeHtmlUtils.fromString(content).asString();
                    aDst.setTxtContent(content);
                }
                aDst.setContentAvailable(true);
            } catch (Exception ex) {
                logger.info("No content found for <"+ aDst.getAssetId() +">: May not have any content (which may be okay for top-level assets): " + ex.toString());
                ex.printStackTrace();
            }
        } else {
            logger.fine("*** getContent -- noContent" + an.getType() + " aSrc.getMimeType():" + aSrc.getMimeType());
        }

        try {
            // aDst.setProps(FileUtils.readFileToString(aSrc.getPropFile()));

            aDst.setProps(getSortedMapString(aSrc.getProperties()));

        } catch (Exception e) {
            aDst.setProps(aSrc.getPropFile() + " could not be loaded.");
            logger.warning(e.toString());
        }

        return aDst;
    }


    /**
     * Pretty prints JSON text format.
     * @param content The JSON text.
     * @return The formatted text.
     */
    private static String prettyPrintJson(String content) {

//	   JSONValue jsonValue = JSONParser.parseStrict(content);
//
//       if (jsonValue != null) {
        try {

            ObjectMapper mapper = new ObjectMapper();

            Object json = mapper.readValue(content, Object.class);

            String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

            return pretty;

        } catch (Exception ex) {
            return content;
        }

//         } else {
//           throw new RepositoryException(RepositoryException.IO_ERROR + "Could not parse JSON");
//         }
//
    }

    /**
     * Realigns CSV structure to fix missing column headers or short rows.
     * @param content The content.
     * @return Realigned arrays.
     */
    public static String[][] processCsvContent(String content) {
        CSVParser parser = new CSVParser(content);
        int sz = parser.size();

        if (sz>0) {
            int maxCol = 0;
            // fix missing headers by finding the widest row
            for (int cx=0; cx<sz; cx++) {
                int rowItemSz = parser.get(cx).getItems().size();
                maxCol = (maxCol>rowItemSz?maxCol:rowItemSz);
            }

            // copy to array
            String[][] records = new String[parser.size()][maxCol];
            int rowIdx =0;
            int colIdx =0;
            for (CSVEntry e : parser.getTable().entries()) {

                for (String s : e.getItems()) {
//                    logger.info("row: " + rowIdx + " colIdx:" + colIdx +1 + " val: " +s.toString());
                    records[rowIdx][colIdx++] = s;
                }
                // fix row width
                while (colIdx<maxCol) {
                    records[rowIdx][colIdx++] = "";
                }
                rowIdx++;
                colIdx=0;
            }
            return records;
        } else {
            return new String[][]{{"No data to display: CSV parser returned zero records."}};
        }
    }




}
