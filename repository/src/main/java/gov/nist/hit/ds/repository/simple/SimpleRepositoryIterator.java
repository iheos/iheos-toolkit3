package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryIterator;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.Type;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class SimpleRepositoryIterator implements RepositoryIterator, FilenameFilter {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 699742242188652792L;
	File reposDir;
	String[] reposDirNames;
	int reposDirsIndex;
	Type type = null;
	File repositorySource=null;

	/**
	 * Iterate across repositories.
	 * @throws RepositoryException
	 */
	public SimpleRepositoryIterator(File repositoriesPath) throws RepositoryException {
		setupRepositoryIterator(null, repositoriesPath, null);
	}
	
	public SimpleRepositoryIterator(RepositorySource rs) throws RepositoryException {
		setupRepositoryIterator(null, rs.getLocation(), null);
	}

	/**
	 * Iterate across repositories of specified type.
	 * @param type
	 * @throws RepositoryException
	 */
	public SimpleRepositoryIterator(File repositoriesPath, Type type) throws RepositoryException {		
		setupRepositoryIterator(null, repositoriesPath, type);
	}
	

	/**
	 * Iterate across all repository sources by given type.
	 * @param type
	 * @throws RepositoryException
	 */
	public SimpleRepositoryIterator(Type type) throws RepositoryException {
		setupRepositoryIterator(null, null, type);
	}
	
	/**
	 * Iterate across all provided repository sources and all types.
	 * @param rss
	 * @throws RepositoryException
	 */
	public SimpleRepositoryIterator(ArrayList<RepositorySource> rss) throws RepositoryException {
		setupRepositoryIterator(rss, null, null);
	}
	
	/** 
	 * Iterate across all provided repository sources and specified type.
	 * @param rss
     * @param t
	 * @throws RepositoryException
	 */
	public SimpleRepositoryIterator(ArrayList<RepositorySource> rss, Type t) throws RepositoryException {
		setupRepositoryIterator(rss, null, t);
	}

	private void setupRepositoryIterator(ArrayList<RepositorySource> rss, File repositorySource, Type t) throws RepositoryException {
		this.type = t;
		
		if (repositorySource!=null) {
			this.repositorySource = repositorySource;
			reposDir = Configuration.getRepositoriesDataDir(repositorySource);
			reposDirNames = reposDir.list(this);
		} else if (rss!=null) {
			String[] mergedRepos = getMergedItems(rss);
		
			reposDirNames = mergedRepos;
		}
		
		

		reposDirsIndex = 0;
	}

	private String[] getMergedItems(ArrayList<RepositorySource> rss)
			throws RepositoryException {
		int numRs = rss.size();
		int listCt = 0;
		
		String[][][] allRepos = new String[1][numRs][];
		int cx=0;
		for (RepositorySource rs : rss) {				
			 allRepos[0][cx] = Configuration.getRepositoriesDataDir(rs.getLocation()).list(this);
			 listCt += allRepos[0][cx].length; 
			 cx++;
		}
		
		String[] mergedRepos = new String[listCt];
		int elementCtr = 0;
		for (int i = 0; i < numRs; i++ ) {
			for (int j = 0; j < allRepos[0][i].length; j++) {
				mergedRepos[elementCtr++] = allRepos[0][i][j];
			}
		}
		return mergedRepos;
	}
	
	public int size() {
		return reposDirNames.length;
	}
	
	public int remaining() {
		int r = size() - reposDirsIndex;
		return r;
	}

	@Override
	public boolean hasNextRepository() throws RepositoryException {
		return reposDirsIndex < reposDirNames.length;
	}

	@Override
	public Repository nextRepository() throws RepositoryException {
		if (!hasNextRepository())
			throw new RepositoryException(RepositoryException.NO_MORE_ITERATOR_ELEMENTS);
		SimpleId id = new SimpleId(reposDirNames[reposDirsIndex++]);

		Repository repos = new SimpleRepository(id);
		repos.setSource(new RepositorySource(this.repositorySource));
		return repos;
	}

	@Override
	public boolean accept(File dir, String name) {

        File f = new File(dir,name);

        if (!f.isDirectory()) { // This is required to skip unrelated files such as .DS_Store
            return false;
        } else {
            String repId = basename(name);
            try {
                SimpleRepository repos = new SimpleRepository(new SimpleId(repId));
                repos.setSource(new RepositorySource(this.repositorySource)); // This is required to ensure a valid repository directory is recognized

                if (type == null) {
                    return true;
                } else {
                    String typeStr = repos.getProperty("repositoryType");
                    Type t = new SimpleType(typeStr);
                    return type.isEqual(t);
                }

            } catch (RepositoryException re) {  }

        }
        return false;
	}

	String basename(String filename) {
		int i = filename.lastIndexOf(".");
		if (i == -1) return filename;
		return filename.substring(0, i);
	}

}
