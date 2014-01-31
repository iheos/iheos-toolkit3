package gov.nist.hit.ds.repository.presentation;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositoryIterator;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.SimpleRepository;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;
import gov.nist.hit.ds.repository.simple.search.AssetNodeBuilder;
import gov.nist.hit.ds.repository.simple.search.SearchResultIterator;
import gov.nist.hit.ds.repository.simple.search.AssetNodeBuilder.Depth;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryTag;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.repository.simple.search.client.exception.RepositoryConfigException;
import gov.nist.hit.ds.utilities.csv.CSVEntry;
import gov.nist.hit.ds.utilities.csv.CSVParser;
import gov.nist.hit.ds.utilities.xml.XmlFormatter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PresentationData implements IsSerializable, Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4939311135239253727L;
	private static Logger logger = Logger.getLogger(PresentationData.class.getName());
	
	public List<RepositoryTag> getRepositoryDisplayTags() throws RepositoryConfigException {
		
        List<RepositoryTag> rtList = new ArrayList<RepositoryTag>();
		
		RepositoryIterator it;
			 
			for (Access acs : RepositorySource.Access.values()) {
				try {					
					it = new RepositoryFactory(Configuration.getRepositorySrc(acs)).getRepositories();
					Repository r =  null;
					while (it.hasNextRepository()) {
						r = it.nextRepository();
						
						rtList.add(new RepositoryTag(r.getId().getIdString(), acs.name(), r.getDisplayName(), getSortedMapString(r.getProperties())));
						
					}
				} catch (RepositoryException ex) {
					logger.warning(ex.toString());
				}			
			}
						
	        Collections.sort(rtList);
	        
	        return rtList;
	
	}
	
	public static List<String> getIndexablePropertyNames() {
		List<String> indexProps = new ArrayList<String>(); 
		for (Access acs : RepositorySource.Access.values()) {
			try {
				List<String> srcProps = DbIndexContainer.getIndexableProperties(Configuration.getRepositorySrc(acs));
				if (!indexProps.isEmpty()) {
					for (String s: srcProps) {
						if (!indexProps.contains(s)) {
							indexProps.add(s);
						}
					}					
				} else
					indexProps.addAll(srcProps);
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(indexProps);
		return indexProps;
	}
	
	
	public static List<AssetNode> getTree(String[][] reposData) {
		
		Repository[] reposList = getReposList(reposData);
		
		ArrayList<AssetNode> result = new ArrayList<AssetNode>();
		List<AssetNode> tmp = null;
		
		AssetNodeBuilder anb = new AssetNodeBuilder(Depth.PARENT_ONLY);
		for (Repository repos : reposList) {
			try {
				tmp = anb.build(repos, PropertyKey.CREATED_DATE);
				if (tmp!=null && !tmp.isEmpty()) {
					for (AssetNode an : tmp) {
						result.add(an);	
					}					
				}
			} catch (RepositoryException re) {
				re.printStackTrace();
				logger.warning(re.toString());
			}
				
		}
				
		return result;
	}
	
	public static AssetNode getParentChain(AssetNode an) throws RepositoryException {
		Repository repos = composeRepositoryObject(an.getRepId(), an.getReposSrc());
		AssetNodeBuilder anb = new AssetNodeBuilder();
		
		return anb.getParentChain(repos, an, true);		
	}
	

	public static List<AssetNode> getImmediateChildren(AssetNode an) throws RepositoryException {
		Repository repos = composeRepositoryObject(an.getRepId(), an.getReposSrc());
					
		AssetNodeBuilder anb = new AssetNodeBuilder();
		try {
			return anb.getImmediateChildren(repos, an);
		} catch (RepositoryException re) {
			logger.warning(re.toString());
		}
		return null;
				
	}

	
	/**
	 * @param repos
	 */
	private static Repository[] getReposList(String[][] repos) {
		
		Repository[] reposList = null;
		try {
		
		int reposCt = repos.length;
		reposList = new Repository[reposCt];
		
			for (int cx=0; cx<reposCt; cx++) {
				try {					
					reposList[cx] = composeRepositoryObject(repos[cx][0], repos[cx][1]); 					
				} catch (RepositoryException e) {
					e.printStackTrace();
					; // incorrect or missing data repository config file 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reposList;
	}
	
	
	public static List<AssetNode> search(String[][] reposData, SearchCriteria sc) {
		
		ArrayList<AssetNode> result = new ArrayList<AssetNode>();
		
		try {
		
		Repository[] reposList = getReposList(reposData);

		AssetIterator iter = null;
		
			iter = new SearchResultIterator(reposList, sc );
		
			int recordCt = 0;
			if (iter!=null && recordCt++ < 500) {// hard limit for now
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
					if (aSrc.getPath()!=null) {
						aDst.setLocation(aSrc.getPropFileRelativePart()); 
					}
					result.add(aDst);
				}
			}

		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}

	public static AssetNode getTextContent(AssetNode an) throws RepositoryException {
		Repository repos = composeRepositoryObject(an.getRepId(), an.getReposSrc());
		
		Asset aSrc = null;
		if (an.getLocation()!=null) {
			// aSrc = repos.getAssetByPath(new File(an.getLocation()));
			aSrc = repos.getAssetByRelativePath(new File(an.getLocation()));
			if (aSrc==null) {
				throw new RepositoryException(RepositoryException.IO_ERROR + "Asset not found by relative location: " + an.getLocation());
			}
		}
		
		
		/* this should not be required 
		 * else if (an.getAssetId()!=null) {
		 
			aSrc = repos.getAsset(new SimpleId(an.getAssetId()));	
		}
		*/
		
		AssetNode aDst = new AssetNode();
		
		aDst.setRepId(aSrc.getRepository().getIdString());
		aDst.setAssetId(aSrc.getId().getIdString());
		aDst.setDescription(aSrc.getDescription());
		aDst.setDisplayName(aSrc.getDisplayName());
		aDst.setMimeType(aSrc.getMimeType());
		aDst.setReposSrc(aSrc.getSource().getAccess().name());
		if (aSrc.getPath()!=null)
			aDst.setLocation(aSrc.getPath().toString());
		
		if (aSrc.getContent()!=null) {
			try {
				String content = new String(aSrc.getContent());
				if ("text/xml".equals(aSrc.getMimeType()) || "application/soap+xml".equals(aSrc.getMimeType())) {
					aDst.setTxtContent(XmlFormatter.htmlize(content));			
				} else if ("text/csv".equals(aSrc.getMimeType())) {
					aDst.setCsv(processCsvContent(content));
				} else if ("text/json".equals(aSrc.getMimeType())) {
					aDst.setTxtContent(prettyPrintJson(content));
				} else {
					content = SafeHtmlUtils.fromString(content).asString();
					aDst.setTxtContent(content);
				}
				aDst.setContentAvailable(true);
			} catch (Exception e) {
				logger.info("No content found for <"+ aDst.getAssetId() +">: May not have any content (which is okay for top-level assets): " + e.toString());
			}			
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
	 * 
	 * @param p
	 * @return
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
	 * @param aDst
	 * @param content
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
	 * @param aDst
	 * @param content
	 */
	private static String[][] processCsvContent(String content) {
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
	
	private static Repository composeRepositoryObject(String id, String src) throws RepositoryException {
		SimpleRepository repos 
							= new SimpleRepository(new SimpleId(id));
		repos.setSource(Configuration.getRepositorySrc(Access.valueOf(src)));
		
		return repos;
	}


}


