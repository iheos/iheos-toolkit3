package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.api.TypeIterator;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class SimpleTypeIterator implements TypeIterator, FilenameFilter {
	/**
	 * Iteration of types must be restricted to a repository source to avoid cross-repository source linkage.
	 */
	private static final long serialVersionUID = -5027811189603265527L;
	File typesDir;
	String[] typesFileNames;
	int typesFileNamesIndex;	
	private String typeFilter="";
	private String domain=null;

	/**
	 * Types native to writable repository
	 * @throws RepositoryException
	 */
	public SimpleTypeIterator(File sourcePath) throws RepositoryException {
		setupTypeIterator(sourcePath);
	}
	public SimpleTypeIterator(RepositorySource rs) throws RepositoryException {
		setupTypeIterator(rs.getLocation());
	}
	/**
	 * A specific type native to a writable repository
	 * @param t
	 * @throws RepositoryException
	 */
	public SimpleTypeIterator(File sourcePath, Type t) throws RepositoryException {
		setupSpecificTypeIterator(sourcePath, t, null);
	}
	public SimpleTypeIterator(RepositorySource rs, Type t) throws RepositoryException {
		setupSpecificTypeIterator(rs.getLocation(), t, null);
	}	
	public SimpleTypeIterator(RepositorySource rs, Type t, String domain) throws RepositoryException {
		setupSpecificTypeIterator(rs.getLocation(), t, domain);
	}	

	private void setupSpecificTypeIterator(File sourcePath, Type t, String domain) throws RepositoryException {
		typesDir = Configuration.getRepositoryTypesDir(sourcePath);
		setTypeFilter(t.getKeyword());
		setDomain(domain);
		
		typesFileNames = typesDir.list(this);
		typesFileNamesIndex = 0;
		
	}
	
	private void setupTypeIterator(File sourcePath) throws RepositoryException {
		typesDir = Configuration.getRepositoryTypesDir(sourcePath);
		typesFileNames = typesDir.list(this);
		typesFileNamesIndex = 0;
	}
	
	public SimpleTypeIterator(ArrayList<RepositorySource> rss, Type t) throws RepositoryException {
		setupTypeIterator(rss, null, t);
	}

	private void setupTypeIterator(ArrayList<RepositorySource> rss, File sourcePath, Type t) throws RepositoryException {
		if (t!=null && t.getKeyword()!=null) {
			setTypeFilter(t.getKeyword());
		}
		
		if (sourcePath!=null) {
			typesDir = Configuration.getRepositoryTypesDir(sourcePath);
			typesFileNames = typesDir.list(this);
		} else if (rss!=null) {
			String[] mergedItems = getMergedItems(rss);
		
			typesFileNames = mergedItems;
		}

		typesFileNamesIndex = 0;
	}

	private String[] getMergedItems(ArrayList<RepositorySource> rss)
			throws RepositoryException {
		int numRs = rss.size();
		int listCt = 0;
		
		String[][][] allTypes = new String[1][numRs][];
		int cx=0;
		for (RepositorySource rs : rss) {				
			 allTypes[0][cx] = Configuration.getRepositoryTypesDir(rs).list(this);
			 listCt += allTypes[0][cx].length; 
			 cx++;
		}
		
		String[] mergedItems = new String[listCt];
		int elementCtr = 0;
		for (int i = 0; i < numRs; i++ ) {
			for (int j = 0; j < allTypes[0][i].length; j++) {
				mergedItems[elementCtr++] = allTypes[0][i][j];
			}
		}
		return mergedItems;
	}
	
	
	
	@Override
	public boolean hasNextType() throws RepositoryException {
		return typesFileNamesIndex < typesFileNames.length;
	}

	@Override
	public Type nextType() throws RepositoryException {
		if (!hasNextType())
			throw new RepositoryException(RepositoryException.NO_MORE_ITERATOR_ELEMENTS);		
		Properties typeProps = loadProperties(new File(typesDir + File.separator + typesFileNames[typesFileNamesIndex++]));
		String keyword = typeProps.getProperty("keyword");
		String description = typeProps.getProperty("description");
		String domain = typeProps.getProperty("domain");

		SimpleType st = null;
		if (domain!=null) {
			String indexes = typeProps.getProperty("indexes");
			st = new SimpleType(domain, keyword, description, indexes);
		} else {		
			st = new SimpleType(domain, keyword, description);
		}
		
		st.setLifetime(typeProps.getProperty("lifetime"));
		
		return st;

	}
	
	// For use with File.list(filter) above. This is the filter
	@Override
	public boolean accept(File file, String arg1) {
		String filter = Configuration.PROPERTIES_FILE_EXT; // base filter
		
		if (!"".equals(getTypeFilter())) { // Apply type specific filter here
			filter = getTypeFilter() + "." + filter;
		}
				
		boolean val = arg1.endsWith(filter);

		if (val) {
			if (getDomain()!=null && !"".equals(getDomain())) {
				if (!filter.equals(Configuration.PROPERTIES_FILE_EXT)) {
					try {
						Properties typeProps = loadProperties(new File(typesDir + File.separator + filter));
						return getDomain().equalsIgnoreCase(typeProps.getProperty("domain").trim());
						
					} catch (Exception e) {				
						return false;
					}	
				}
				
			}			
		}

				
		return val;
	}

	/**
	 * Load the individual type definitions so they can be iterated through.
	 * @param propFile
	 * @return Properties object
	 * @throws RepositoryException if properties cannot be loaded
	 */
	Properties loadProperties(File propFile) throws RepositoryException {
		Properties properties = new Properties();
		try {
			FileReader fr = new FileReader(propFile);
			properties.load(fr);
			fr.close();
		} catch (IOException e) {
			throw new RepositoryException(RepositoryException.IO_ERROR + " : " +
					"Cannot load repository type description from [" + propFile + "]");
		}
		return properties;

	}

	/**
	 * @return the typeFilter
	 */
	public String getTypeFilter() {
		return typeFilter;
	}

	/**
	 * @param typeFilter the typeFilter to set
	 */
	public void setTypeFilter(String typeFilter) {
		this.typeFilter = typeFilter.trim();
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		if (domain!=null)
			this.domain = domain.trim();
	}

}
