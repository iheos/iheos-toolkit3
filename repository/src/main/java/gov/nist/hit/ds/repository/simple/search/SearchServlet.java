package gov.nist.hit.ds.repository.simple.search;

import gov.nist.hit.ds.utilities.initialization.installation.Installation;
import gov.nist.hit.ds.utilities.initialization.installation.PropertyServiceManager;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.SimpleRepository;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria.Criteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm.Operator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author Sunil.Bhaskarla
 * 
 * Simple Asset Report
 * 	Supports multiple levels of hierarchy
 */
public class SearchServlet extends HttpServlet {

	private static final String USAGE_STR = "Usage: ?reposSrc=<Resident|External>&reposId=value&<asset=filenamewithextension|assetId=value>&[level=<1,2,3>]&[reportType=<1,2>]";
	// private static Logger logger = Logger.getLogger(SearchServlet.class.getName());

	
	private static final long serialVersionUID = 8326366092753151300L;
	private static int reportType = 2; 
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		 /*		
		  * 
		http://127.0.0.1:8888/repository/list?reposSrc=?&reposId=ee332a45-4c5f-4762-a62d-c6f7e217e93a&assetId=172-7ce2-4c5b-b994-a0123&level=2
		
		http://localhost:8080/xdstools3/repository/list?reposSrc=RW_EXTERNAL&reposId=ee332a45-4c5f-4762-a62d-c6f7e217e93a&assetId=172-7ce2-4c5b-b994-a0123&level=2
		  *
		  */
		 
		String reposSrc = request.getParameter("reposSrc");
		String reposId = request.getParameter("reposId");

		String assetId = request.getParameter("assetId");
		String assetLoc = request.getParameter("asset");

		String levelStr = request.getParameter("level"); // 1 (top level only) or 2 (multiple depth)
		String reportTypeStr = request.getParameter("reportType"); // 1 or 2
		Access acs = null; 
		

		SimpleRepository repos = null; 
		try {
			
			try {
				Installation.installation();
				
				if (Installation.installation().getExternalCache()==null) {
					File tpPath = new File(getServletContext().getRealPath("WEB-INF/"+ Installation.TOOLKIT_PROPERTIES));
					
					Properties props = new Properties();
					
					FileReader fr = new FileReader(tpPath);
					props.load(fr);
					fr.close();
					String ecDir = props.getProperty(PropertyServiceManager.EXTERNAL_CACHE);
					
					if (ecDir!=null) {
						Installation.installation().setExternalCache(new File(ecDir));
					} else {
						throw new ServletException("Undefined "+PropertyServiceManager.EXTERNAL_CACHE + " property in " + Installation.TOOLKIT_PROPERTIES);
					}
										
					Installation.installation().initialize();					
				}				
				
			} catch (Exception ex) {
				throw new ServletException("Request could not be completed." + ex.toString());
			}
						
			Configuration.configuration();
			
			repos = new SimpleRepository(new SimpleId(reposId));
			if (reposSrc==null) {
				throw new ServletException("Missing required reposSrc. " +USAGE_STR);
			} else {
				acs = getAccessType(reposSrc);
				if (acs==null) {
					throw new ServletException("Invalid reposSrc. " + USAGE_STR);
				}
			}			
			repos.setSource(Configuration.getRepositorySrc(acs));			
		} catch (RepositoryException e) {			
			throw new ServletException(e.toString());
		}
				
		if (reportTypeStr!=null) {
			int rpValue = Integer.parseInt(reportTypeStr);
			if (rpValue>0 && rpValue <3) {
				reportType = rpValue;
			}
		}
		
		/**
		 *  
		levels=n

		which indicates the depth of the display.  
		levels=1 would show the asset requested.  
		levels=2 would show the the asset requested and its immediate children (and grandchildren) (updated from original desc).
		The default value should be levels=1.  if levels=0 is given, interpret it as levels=1

		(Bill)

		 */

		int level = 0;
		if (levelStr!=null && !"".equals(levelStr)) {
			level = Integer.parseInt(levelStr);
			if (level<2) {
				level = 0;
			} else {
				level = 2;
			}
		}
		
		if (reposId!=null && (assetLoc!=null || assetId!=null)) {
				
				String result = getAsset(repos, assetLoc, assetId, level, level);
				response.getWriter().write(printReport(result,acs));			
		}
		else {
			throw new ServletException(USAGE_STR);			
		}
		
	}

	private Access getAccessType(String reposSrc) throws RepositoryException {
		for (Access a : Access.values()) {
			if (a.toString().toLowerCase().contains((reposSrc.toLowerCase()))) {
				return a;
			}
		}
		throw new RepositoryException("Access type "+ reposSrc +" not found");
	}
	
	private String printReport(String rpt, Access acs)  {
		
		
		String pathStr;
		try {
			pathStr = Configuration.getRepositorySrc(acs).getLocation().toString();
		} catch (RepositoryException e) {
			pathStr = e.toString();
		}
		
		// Exclude this wrapper if snippet is requested 
		if (rpt==null || "".equals(rpt)) {
			rpt = "<ul><li>No results found: <ol>" +
					"<li>Has the repository folder been renamed? " +
					"<ul><li>Assets within a repository should have the " + PropertyKey.REPOSITORY_ID + " coded within the properties file and the corresponding folder name on the filesystem must match the specified property value at all times." +
							"</li></ul></li></ol></li></ul><ul>" +
							"<li>Configuration:<ul>" +  acs.name() +  " source:"+ pathStr  +"</ul></li></ul>";					
			
		}
		return "<html><body style='font-family:arial,verdana,sans-serif;'>" + rpt + "</body></html>";
	}

	private String getAsset(Repository repos, String assetLoc, String assetId, int topLevel, int level) {
		StringBuffer sb = new StringBuffer();
		
		try {
			SearchCriteria criteria = new SearchCriteria(Criteria.AND);
			
			
			
			PropertyKey nest = null;
			
			if (assetId!=null) {				
				nest = (topLevel==level)? PropertyKey.ASSET_ID : PropertyKey.PARENT_ID;
				criteria.append(new SearchTerm(nest,Operator.EQUALTO,assetId));
			} else if (assetLoc!=null) {				
				nest = (topLevel==level)? PropertyKey.LOCATION : PropertyKey.PARENT_ID;
				criteria.append(new SearchTerm(nest,Operator.EQUALTO,assetLoc));
			} else 
				return null;
			
			
			
			AssetIterator iter = new SearchResultIterator(new Repository[]{repos}, criteria, PropertyKey.DISPLAY_ORDER);
			
			if (iter.hasNextAsset()) {
				reportBeginHeader(topLevel, level, sb); // ul
				
			} 
			
			
			int rowCt=0;
			while (iter.hasNextAsset()) {
				int levelCt = topLevel;
				Asset a = iter.nextAsset();
				

				reportBeginAddDetail(sb,a);			// li
				
				rowCt = reportAddDetail(sb, rowCt, a);
				
				while (--levelCt>0) {
					String child = getAsset(repos,a.getPropFileRelativePart(), a.getId().getIdString(),topLevel,levelCt);		// ul
					
					if (child!=null && !"".equals(child)) {
						reportAddChild(sb, child);
					}					
				}
				
				reportEndAddDetail(sb);			// li

				
			}
			
			/*
			 * ul
			 * 	li parent
			 * 		ul li child
			 * 		li ul
			 * 	li
			 * ul
			 */
			
			/*
			 * 
			 * <html><body><ul><li>172-7ce2-4c5b-b994-a0123 - This is my document
			 * 							<ul><li>172-7ce2-4c5b-b994-a0125 - This is my document</li>
			 * 										<li>172-7ce2-4c5b-b994-a0124 - This is my document</li>
			 * 									</ul>
			 * 							</li></ul></body></html>
			 */
			
			if (rowCt>0) {

				reportEndHeader(sb);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return sb.toString();

	}

	/**
	 * @param sb
	 */
	protected void reportEndHeader(StringBuffer sb) {
		if (reportType==1) {
			sb.append("</table>");
		} else {
			sb.append("</ul>");
		}
	}

	/**
	 * @param sb
	 * @param child
	 */
	protected void reportAddChild(StringBuffer sb, String child) {
		if ("".equals(child)) return;
		
		if (reportType==1) {
			sb.append("<tr><td align='right' colspan=4>" + child + "</td><tr>");
		} else {
			sb.append( child );
		}
	}
	
	protected void reportBeginAddDetail(StringBuffer sb, Asset a) throws RepositoryException {
		if (reportType==2) {
			
			
			sb.append("<li title='"); 
			try {
				sb.append(FileUtils.readFileToString(a.getPropFile()));
			} catch (IOException e) {
				sb.append("Error: Properties file could not be loaded. file=" + a.getPropFile());
				// e.printStackTrace();
			}
			/*
			sb.append(  
					 "Type: " + a.getAssetType().getKeyword() 
					+ ((a.getCreatedDate()!=null)?"&nbsp;Created Date: "+a.getCreatedDate():"")
					+((a.getExpirationDate()!=null)?"&nbsp;Expiration Date: "+a.getExpirationDate():"")					 
					+((a.getMimeType()!=null)?"&nbsp;Mime Type: " +a.getMimeType():"")
					);
			*/
			sb.append("'>");
		}
	}
	protected void reportEndAddDetail(StringBuffer sb) {
		if (reportType==2) {
			sb.append("</li>");
		}
	}

	/**
	 * @param sb
	 * @param rowCt
	 * @param a
	 * @return
	 * @throws RepositoryException
	 */
	protected int reportAddDetail(StringBuffer sb, int rowCt, Asset a)
			throws RepositoryException {
		rowCt++;
		
		if (reportType==1) {
		sb.append(
				"<tr bgcolor='" + ((rowCt%2 == 0)?"#E6E6FA":"")   + "'><td>"	+ a.getId().toString() + "</td>"
				+ "<td>"	+ ((a.getAssetType()!=null)?a.getAssetType().getKeyword():"") + "</td>"
				+ "<td>"	+ ((a.getCreatedDate()!=null)?a.getCreatedDate():"") + "</td>"
				+ "<td>"	+ ((a.getMimeType()!=null)?a.getMimeType():"") + "</td>"
				+"</tr>"
				);
		} else {
			sb.append( a.getDisplayName()); // a.getId().toString() + " - " +
			if (a.getMimeType()!=null) {
				sb.append("&nbsp;<font ");
				
				if (a.getMimeType().toLowerCase().startsWith("text/")) {
					sb.append("title='" + StringEscapeUtils.escapeHtml4(new String(a.getContent())) + "' ");
				}
				
				sb.append("family='arial,verdana,sans-serif' size='2'><sup><a href='" + this.getServletContext().getContextPath() + "/repository/downloadAsset?reposSrc="+ a.getSource().getAccess().name() +"&reposId=" + a.getRepository().getIdString() + "&assetId=" + a.getId().getIdString() + "'>" 
				+ a.getMimeType()
				+ "</a></sup></font>");
			}
		}
		return rowCt;
	}

	/**
	 * @param topLevel
	 * @param level
	 * @param sb
	 */
	protected void reportBeginHeader(int topLevel, int level, StringBuffer sb) {
		
		if (reportType==1) {
			sb.append("<table "+ ((topLevel!=level)? "style='margin-left:"+ 25*(topLevel-level) + "px'":"")  +">");
			sb.append("<tr bgcolor='#d2b48c'><td>"+ ((topLevel!=level)?"Child ":"")   +" Asset Id</td><td>Type</td><td>Created Date</td><td>Mime Type</td></tr>");			
		} else {
			sb.append("<ul>");
		}
		
	}
	
//	private void print(String s) {
//		System.out.println(s);
//		
////		System.out.println("found asset: " +  a.getId().toString() + ", of type: "+ a.getAssetType().getKeyword() +", in repos:" + a.getRepository().getIdString() );
////		System.out.println("life: " + a.getAssetType().getLifetime());
//
//	}

}