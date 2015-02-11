package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.xdsExceptions.ExceptionUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SimpleAssetIterator implements AssetIterator, FilenameFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -679485554932669997L;
    private File reposDir = null;
    private File[] assetFileNames;
	private int assetFileNamesIndex = 0;
    private ArtifactId repositoryId = null;
    private boolean[] selections = null;
    private Type type = null;


    private String time = null;
	private Repository repository;

    private static Logger logger = Logger.getLogger(SimpleAssetIterator.class.getName());

	public SimpleAssetIterator(Repository repos) throws gov.nist.hit.ds.repository.api.RepositoryException {
		setRepository(repos);
		this.repositoryId = repos.getId();
		// reposDir =  new File(Configuration.getRepositoriesDataDir(repos.getSource()).toString()  + File.separator + repositoryId.getIdString());
		reposDir = repos.getRoot();
		List<File> fList = setupList(reposDir);
		
		assetFileNames = fList.toArray(new File[fList.size()]);		
	}
	
	/**
	 * Filter a repository by asset type
	 * @param repos
	 * @param type (AssetType)
	 * @throws RepositoryException
	 */
	public SimpleAssetIterator(Repository repos, Type type) throws RepositoryException {
		setRepository(repos);
		this.repositoryId = repos.getId();
		this.type = type;
		// reposDir =  new File(Configuration.getRepositoriesDataDir(repos.getSource()).toString()  + File.separator + repositoryId.getIdString());
		reposDir = repos.getRoot();
		List<File> fList = setupList(reposDir);
		
		assetFileNames = fList.toArray(new File[fList.size()]);				
	}

    public SimpleAssetIterator(Repository repos, String time) throws RepositoryException {
        setRepository(repos);
        this.repositoryId = repos.getId();
        this.time = time;
        // reposDir =  new File(Configuration.getRepositoriesDataDir(repos.getSource()).toString()  + File.separator + repositoryId.getIdString());
        reposDir = repos.getRoot();
        List<File> fList = setupList(reposDir);

        assetFileNames = fList.toArray(new File[fList.size()]);
    }
	
	private List<File> setupList(File dir) {		
 		ArrayList<File> af = new ArrayList<File>();
		File[] assetFiles = dir.listFiles(this);
				
		for (File f : assetFiles) {
			if (f.isDirectory()) {
				af.addAll(setupList(f));
			} else {
				af.add(f);				
			}
		}
		
		return af;
		// assetFileNames = reposDir.list(this);
	}
	
	@Override
	public boolean hasNextAsset() throws RepositoryException {
		if (repositoryId == null)
			throw new RepositoryException(RepositoryException.UNKNOWN_REPOSITORY + " : " +
		"repository not set via API");
		boolean withinRange = assetFileNamesIndex < assetFileNames.length;
        boolean locatableAsset = false;

        if (withinRange) {

            do {
                File filename = assetFileNames[assetFileNamesIndex];
                try {
                    // Asset a = repository.getAssetByPath(filename);
                    locatableAsset = filename.exists();
                } catch (Throwable t) {
                   /* Possible causes:
                    Asset could have been relocated/deleted because
                    * 1) Asset was converted to a folder
                    * 2) Asset was deleted through the API/ or manually by the filesystem user
                    * Remedy:
                    * Find next locatable asset in the snapshot view
                    * */
                    logger.warning("hasNextAsset [" + filename + "] may have been relocated: " + t.toString());
                }
                if (!locatableAsset) {
                    logger.info("hasNextAsset [" + filename + "] could not be located. Skipping file...");
                }
            } while (!locatableAsset && ++assetFileNamesIndex < assetFileNames.length);
        }

        return locatableAsset;
 	}

	@Override
	public Asset nextAsset() throws RepositoryException {
		if (!hasNextAsset())
			throw new RepositoryException(RepositoryException.NO_MORE_ITERATOR_ELEMENTS);
		File filename = assetFileNames[assetFileNamesIndex++];

        try {
            return repository.getAssetByPath(filename);
        } catch (RepositoryException re) {
            /* Asset could have been relocated/deleted because
            * 1) Asset was converted to a folder
            * 2) Asset was deleted through the API/ or manually by the filesystem user
            * */
            logger.warning("nextAsset ["+ filename +"] failed: " + re.toString());
            throw re;
        }
		
		// This should not be required because the filename is already available for direct loading purposes
		// Id assetId = Configuration.getAssetIdFromFilename(filename.getName());
		// return repos.getAsset(assetId);
	}
	
	public boolean reset() throws RepositoryException {
		if (repositoryId == null)
			throw new RepositoryException(RepositoryException.UNKNOWN_REPOSITORY + " : " +
		"reset failed via API");
		
		assetFileNamesIndex = 0;		
		return true;		
	}
	
	/**
	 * Marks the current selection
	 * @throws RepositoryException 
	 */
	public void select() throws RepositoryException {  // marks the current selection
							// -1 offset necessary since index already incremented
		if (selections == null)
			selections = new boolean[assetFileNames.length];
		for (int i=0; i<assetFileNames.length; i++) selections[i] = false;
		int i = assetFileNamesIndex - 1;
		if (i < 0 || i >= assetFileNames.length)
			throw new RepositoryException(RepositoryException.OPERATION_FAILED + " : " + 
		"no current asset selected");
		selections[i] = true;
	}

	public SimpleAssetIterator getAssetIteratorFromSelections() throws RepositoryException {
		SimpleAssetIterator it = new SimpleAssetIterator(getRepository());
		it.repositoryId = repositoryId;
		it.assetFileNames = (File[]) getSelectedFileNames().toArray();
		return it;
	}
	
	List<File> getSelectedFileNames() {
		List<File> selectedNames = new ArrayList<File>();
		for (int i=0; i<assetFileNames.length; i++) {
			if (selections[i])
				selectedNames.add(assetFileNames[i]);
		}
		return selectedNames;
	}

	@Override
	public boolean accept(File dir, String name) {
		if (name.equals("repository." + Configuration.PROPERTIES_FILE_EXT)) return false;  // not an asset
		File f = new File(dir + File.separator + name);

		if (f.isDirectory()) {

            if (time!=null) {
                return (f.toString().indexOf(time)>-1);

            }
            return true; // Look for child assets
        }

		if (f.isFile() && !name.endsWith(Configuration.PROPERTIES_FILE_EXT)) return false;  // not an asset property file
		// parent restriction?


        /* if (time == null) {
            return true;    // no time restriction
        } else {

            SimpleDateFormat sdf = new SimpleDateFormat(Hl7Date.parseFmt);

            String assetLastModifiedDate = sdf.format(f.lastModified());
            if (assetLastModifiedDate.compareTo(time)<0) {
                return false;

        } */

		if (type == null) {
            return true;    // no type restriction
        } else {
		// Id assetId = Configuration.getAssetIdFromFilename(name);
		
            try {
                // This should not be required because the filename is already available for direct loading purposes
                // SimpleAsset a =  (SimpleAsset) getRepository().getAsset(assetId);

                Asset a =  getRepository().getAssetByPath(new File(dir + File.separator + name));

                String aTypeStr = a.getProperty("type");
                Type aType = new SimpleType(aTypeStr);
                return type.isEqual(aType);
            } catch (RepositoryException e) {
                System.out.println(ExceptionUtil.exception_details(e));
            }
        }
        return false;

	}

	/**
	 * 
	 * @return
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * 
	 * @param repository
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	/**
	 * 
	 * @return
	 */
	public int getSize() {	
		if (assetFileNames!=null) {
			return assetFileNames.length;	
		}
		else
			return -1;
	}
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
