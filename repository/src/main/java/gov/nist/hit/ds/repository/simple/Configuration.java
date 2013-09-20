package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.api.Type;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
* Singleton that represents the repository configuration
*
*/
public class Configuration {
	public static final String STD_PROPERTIES_FILE_EXT = "properties";
	public static final String PROPERTIES_FILE_EXT = "props.txt";
	public static final String CONTENT_FILE_EXT = "bytes";
	public static final String CONTENT_TEXT_EXT = "text";
	public static final String REPOSITORY_TYPES_DIR = "types";
	public static final String REPOSITORY_DATA_DIR = "data";
	public static final String REPOSITORY_PROP_FILE_BASENAME = "repository";
	public static final String REPOSITORY_PROP_FILE = REPOSITORY_PROP_FILE_BASENAME + PROPERTIES_FILE_EXT;
	public static final String REPOSITORY_SOURCE_ATTRIBUTES = "access." + STD_PROPERTIES_FILE_EXT;
    /* Configuration warning */
    public static final String CONFIGURATION_WARNING = "Configuration warning";

	private static ArrayList<RepositorySource> repositorySources = new ArrayList<RepositorySource>();
	static final Logger logger = Logger.getLogger(Configuration.class);
	private static final String REPOSITORY_SOURCE_DIRNAME = "repositories";

	
	static Configuration me = null;

	static public Configuration configuration() throws RepositoryException {
		if (me == null) {
			me = new Configuration();
		}
		return me;
	}	
	
	private Configuration() throws RepositoryException {
		File f = null;
		try {
			
			f = new File(getClass().getClassLoader().getResource("WEB-INF/" + REPOSITORY_SOURCE_DIRNAME).getFile());
			addSource(f,Access.RO_RESIDENT);
			
		} catch (NullPointerException npe) {
			logger.info("No resident repository source found in WEB-INF/" + REPOSITORY_SOURCE_DIRNAME);
		}
				
		String ecDir = Installation.installation().getExternalCache().toString();
		
		f = new File( ecDir + File.separator + REPOSITORY_SOURCE_DIRNAME); 
		addSource(f,Access.RW_EXTERNAL);		
	}
		
	private void addSource(File f, Access access) throws RepositoryException {
		if (f.exists()) {
			RepositorySource rs = new RepositorySource(f,access);
			rs.setValid(true);

			getRepositorySources().add(rs);
		} else {
			throw new RepositoryException(RepositoryException.IO_ERROR + ": path does not exist (" + f.toString() + ")");
			// logger.warn(CONFIGURATION_WARNING + ": no "+ access.toString() +" repository source found ("+ f.toString() +")");
		}
	}
	

	/***
	public Configuration(File[] locations) throws RepositoryException {
		
		validateLocations(locations);	 
	}

	
	private ArrayList<RepositorySource> validateLocations(File[] repositoryLocations)
			throws RepositoryException {
		
		if (repositoryLocations == null)
			throw new RepositoryException(RepositoryException.MANAGER_INSTANTIATION_ERROR + 
					" : repositoryLocation(s) may not be null. At least one repository location is required for successful initialization.");
				
		// Root cannot be changed during operation		
		ArrayList<RepositorySource> sourceList = new ArrayList<RepositorySource>();
		
		// Iterate locations and build a source reference
		// All locations must be valid to complete initialization
		boolean writeableSourceExists = false;
		
		for (File f : repositoryLocations) {
			if (exists(f)) {				

				
//				1) The data folder might not exist as it could be dynamically created (junit), or copied-in manually
//				2) types might not exist and it might be create dynamically (junit test case)
//				
//				Warnings are to be converted to exceptions when operating in strict mode.
//				
				
				if (!isConfigured(f)) {
					throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR  					 
							+ " : Repository filesystem validation failed - one of the repository config pairs "  
							+ " are not setup.");										
				} 
				
				RepositorySource rs = new RepositorySource();
				rs.setLocation(f);
				
				try {
					writeableSourceExists = setRepositoryAccessMode(writeableSourceExists, f, rs);
				} catch (IOException ex) {
					// Ignore error on optional access properties
				}				
				
				// System.out.println("*** setting reposConfig source: repos("+ (rs.isReadOnly()?"ro":"rw") +"):" + rs.getLocation());
				
				sourceList.add(rs);									
					
			}

		}
				
		setRepositorySources(sourceList);
		return getRepositorySources();

	}
	

	private static boolean setRepositoryAccessMode(
			boolean writeableSourceExists, File f, RepositorySource rs)
			throws RepositoryException, IOException {
		try {
			
			   Properties p = loadPropertyFile(getSourceAttributesFile(f));
			   if (p!=null) {
				   String propVal = p.getProperty("readOnly");
				   if ("true".equalsIgnoreCase(propVal) || "1".equals(propVal) || "on".equalsIgnoreCase(propVal) ||"yes".equalsIgnoreCase(propVal)) {					   
					   rs.setAccess(Access.RO_RESIDENT);
				   } 
			   }  
			} catch (FileNotFoundException ex) {				
				// Ignore missing source attributes and use default access mode (read-write)
			}		
		
		   if (writeableSourceExists && !rs.getAccess().equals(Access.RO_RESIDENT)) { // This is a safety catch
			   throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR  
					   + " : More than one writeable repository source is not permitted. "
					   + "Read-only repository sources must be configured with "+ REPOSITORY_SOURCE_ATTRIBUTES  
					   + " to avoid false signaling of multiple writeable repositories.");
		   } else {
			   writeableSourceExists = true;
		   }

		return writeableSourceExists;
	}
*/	
	
	
    static public boolean exists(File f) throws RepositoryException {
    	if (f==null)
			throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR + 
					" : The following repository location does not exist or may not be accessible: " + f);

        return f.exists() && f.isDirectory();
    }
    
	static public boolean isConfigured(File reposSourceDir) throws RepositoryException {
		
		// Validate source root sub-directory pair
			File typesDir = getRepositoryTypesDir(reposSourceDir);
			if (!(typesDir.exists() && typesDir.isDirectory())) { 
				throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR +
						": " + Configuration.REPOSITORY_TYPES_DIR + " folder ["+ typesDir.toString() +"] does not exist.");
			}
			File dataDir = getRepositoriesDataDir(reposSourceDir);
			if (!(dataDir.exists() && dataDir.isDirectory())) {
				logger.info("Notice" +
						": " + Configuration.REPOSITORY_DATA_DIR + " folder ["+ dataDir.toString() +"] does not yet exist.");
			}
		
		return true;
	}
		

	public boolean isRepositorySystemInitialized() throws RepositoryException {
		return (getRepositoryLocations()!=null);
	}



	public ArrayList<RepositorySource> getRepositoryLocations() throws RepositoryException {
		if (getRepositorySources() == null)
			throw new RepositoryException(RepositoryException.MANAGER_NOT_FOUND + " : " +
					"Repository Configuration object not initialized");
		return getRepositorySources();
	}
	
	public File getRepositoryLocation(File repositoryLocation, Id id) throws RepositoryException {
		File hostLocation = new File(getRepositoriesDataDir(repositoryLocation) + File.separator + id.getIdString());
		if (exists(hostLocation)) {
			return hostLocation;
		}
		
		throw new RepositoryException(
			RepositoryException.UNKNOWN_REPOSITORY + " : " +
					"Repository source host not found. Please check to see if the repositoryId <" + id.getIdString() + "> exists within the repository source parameters.");
	}
	
	/**
	 * Get directory holding the contents of the repository.   
	 * @param reposId
	 * @return
	 * @throws RepositoryException
	 */
	public RepositorySource getRepositorySource(Id reposId) throws RepositoryException {
		assert(reposId != null);
		assert(getRepositorySources() != null);
		
		// Iterate locations and find the residing data dir
		// All we have is the id, if multiple sources exists, we have to find the host
		
		File hostLocation = null;
				
		for (RepositorySource rs : getRepositorySources()) {
			hostLocation = new File(getRepositoriesDataDir(rs.getLocation()) + File.separator + reposId.getIdString());
			if (exists(hostLocation)) {
				return rs;
			}
		}
				
		throw new RepositoryException(
				RepositoryException.REPOSITORY_SRC_NOT_FOUND + " : " +
						"Repository source host not found. Please check to see if the repositoryId <" + reposId.getIdString() + "> exists within the repository source parameters.");
		
	}

	/**
	 * A new repository attaches to a writable repository
	 * @param id
	 * @return
	 * @throws RepositoryException
	 */
	public File getNewRepositoryLocation(Id id) throws RepositoryException {  
		if (id==null) {
			throw new RepositoryException(RepositoryException.UNKNOWN_ID + " : " +
					"Repository Id may not be null.");
	
		}
				return  new File(getRepositoriesDataDir(getRepositorySrc(Access.RW_EXTERNAL).getLocation()) + File.separator + id.getIdString()); 		
	}

	public boolean repositoryExists(Id id) throws RepositoryException {
		RepositorySource rs = getRepositorySource(id);
		return rs.getLocation().exists() && rs.getLocation().isDirectory();
	}
	
	static public boolean repositoryExists(RepositorySource source, Id id) throws RepositoryException {
		File repositoryRoot = 
				new File(Configuration.getRepositoriesDataDir(source.getLocation()).toString()  + File.separator + id.getIdString());
		return repositoryRoot.exists() && repositoryRoot.isDirectory();
	}

	public Properties getRepositoryTypeProperties(File rsPath, Type type) throws RepositoryException {
		File propFile = getRepositoryTypeFile(rsPath,type);
		
		Properties props = null;
		try {
			loadPropertyFile(propFile);
		} catch (IOException e) {
			throw new RepositoryException(
					RepositoryException.UNKNOWN_TYPE + " : " +
							"Repository type [" + type.getDomain() + "] is unknown");
		}	
		return props;
	}

	static private Properties loadPropertyFile(File propFile)
			throws IOException {
		Properties props = new Properties();
		
			FileReader fr = new FileReader(propFile);
			props.load(fr);
			fr.close();
		
		return props;
	}
	
	File getRepositoryTypeFile(File sourcePath, Type type) throws RepositoryException {
		return new File(
				sourcePath + File.separator + 
				Configuration.REPOSITORY_TYPES_DIR + File.separator + 
				type.getDomain() + "." + Configuration.PROPERTIES_FILE_EXT);
	}	

	public static File getRepositoryTypesDir(RepositorySource rs) throws RepositoryException {
		return getRepositoryTypesDir(rs.getLocation());
	}
	
	public static File getRepositoryTypesDir(File sourcePath) throws RepositoryException {
		return new File(
				sourcePath + File.separator + 
				Configuration.REPOSITORY_TYPES_DIR);
	}
	
	public static File getRepositoriesDataDir(RepositorySource rs) throws RepositoryException {
		return getRepositoriesDataDir(rs.getLocation());
		
	}

	public static File getRepositoriesDataDir(File sourcePath) throws RepositoryException {
		return new File(
				sourcePath + File.separator + 
				Configuration.REPOSITORY_DATA_DIR);
	}
	
	/*
	private static File getSourceAttributesFile(File sourcePath) throws RepositoryException {
		return new File(
				sourcePath + File.separator + 
				Configuration.REPOSITORY_SOURCE_ATTRIBUTES);
	}
	*/
	
	

	public static Id getAssetIdFromFilename(String filename) {
		File fn = new File(filename);
		String fullName = fn.getName();
		// extract id from filename
		String[] parts = fullName.split("\\.");
		if (parts != null && parts.length > 0)
			return new SimpleId(parts[0]);
		else
			return new SimpleId(fullName);
	}

	public static ArrayList<RepositorySource> getRepositorySources() {
		return repositorySources;
	}

	public static void setRepositorySources(
			ArrayList<RepositorySource> rs) {
		repositorySources = rs;
	}

	public static RepositorySource getRepositorySrc(Access access) throws RepositoryException {
		
		for (RepositorySource rs : getRepositorySources()) {
			if (rs.getAccess().equals(access)) {
				isConfigured(rs.getLocation());
				return rs;
			}
		}
		String errorHeader = RepositoryException.REPOSITORY_SRC_NOT_FOUND + ": The ("+ access.toString() +") " + REPOSITORY_SOURCE_DIRNAME ;
		if (Access.RO_RESIDENT.equals(access)) {
			throw new RepositoryException(errorHeader + " folder is missing in WEB-INF.");	
		} else {
			throw new RepositoryException(errorHeader + " folder is missing in External Cache.");
		}
		
	}

	
}
