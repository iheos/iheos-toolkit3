package gov.nist.hit.ds.repository.simple.search;

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
import gov.nist.hit.ds.toolkit.installation.Installation;
import gov.nist.hit.ds.toolkit.installation.PropertyServiceManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Sunil.Bhaskarla 
 */
public class DownloadAssetServlet extends HttpServlet {

	private static final String USAGE_STR = "Usage: ?reposSrc=<Resident|External>&reposId=value&assetId=value";

	private static final long serialVersionUID = -2233759886953787817L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException  {
		
		String reposSrc = request.getParameter("reposSrc");
		String reposId = request.getParameter("reposId");
		String assetId = request.getParameter("assetId");
		String assetLoc = request.getParameter("asset");
		String contentDisp = "inline"; // default
		
		if ("attachment".equals(request.getParameter("contentDisp"))) {
			contentDisp = "attachment"; // override streamType
		}
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
					
		} catch (RepositoryException e) {			
			throw new ServletException(e.toString());
		}
		
		
		try {
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

		if (reposId!=null) {
			try {
				Asset a = null;
				 if (assetId!=null) {
					 
					a = repos.getAsset(new SimpleId(assetId));
					
					if (a==null) {
						SearchCriteria criteria = new SearchCriteria(Criteria.AND);
						criteria.append(new SearchTerm(PropertyKey.ASSET_ID,Operator.EQUALTO,assetId));
						
						AssetIterator iter = new SearchResultIterator(new Repository[]{repos}, criteria, new PropertyKey[]{PropertyKey.DISPLAY_ORDER});
						
						if (iter.hasNextAsset()) {
							a = iter.nextAsset();
						}						
					}
					 
				} else if (assetLoc!=null && !"".equals(assetLoc)) {
					a = repos.getAssetByRelativePath(new File(assetLoc + Configuration.DOT_SEPARATOR + Configuration.PROPERTIES_FILE_EXT));					
				}			
				
				if (a!=null) {
					  response.setHeader("Cache-Control", "no-cache");
					  response.setDateHeader("Expires", 0);
					  response.setHeader("Pragma", "no-cache");
					  response.setDateHeader("Max-Age", 0);
					  
					  response.setHeader("Content-Disposition", contentDisp+ ";filename=\""+ a.getContentFile().getName() + "\""); // + "." + a.getContentExtension()[2] 
					  if (a.getMimeType()!=null) {
						  response.setContentType(a.getMimeType());
					  } else {
						  response.setContentType("application/xml");
					  }
					  
					  byte[] content = a.getContent();
					  if (content==null) {
						  throw new ServletException("The requested content file does not exist or it could not be loaded.");  
					  }
					  
					  
					  OutputStream os = response.getOutputStream();

					  os.write(content);
					  os.flush();
					  os.close();

				}				
			} catch (RepositoryException re) {
				throw new ServletException("Error: Asset could not be located: " + re.toString());
			}
			
			
		
		} else {
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

	
	
}
