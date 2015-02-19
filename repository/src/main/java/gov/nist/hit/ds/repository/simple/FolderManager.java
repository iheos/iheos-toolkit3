package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Parameter;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.shared.PropertyKey;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * The purpose of this class is to facilitate folder management features that help to: 
 * 1) Keep assets independent from each other (vs. relying on path structure that would make assets dependent of file system folders)
 * 2) Rely on parentId property
 * 3) Folder names can easily be relocated.
 *
 */

/**
 * TODO
 * Think about potential race conditions or synchronization issues arising with the same parent asset being by two different threads.  
 *
 */
public class FolderManager {

	private static final String PARENT_FILE_EXT = Configuration.PARENT_TAG + Configuration.DOT_SEPARATOR + Configuration.PROPERTIES_FILE_EXT;
	// Use when folder name cannot be determined
	public static final String LOST_AND_FOUND = "lost_and_found";
	private static Logger logger = Logger.getLogger(FolderManager.class.getName());

    public boolean doesParentAssetFolderExist(File fileName) throws RepositoryException {
        Parameter param = new Parameter("filename");
        param.assertNotNull(fileName);

        return (fileName.getName().endsWith(Configuration.DOT_SEPARATOR +  PARENT_FILE_EXT));

    }
	
	/**
	 * 
	 * @param sa SimpleAsset instance
	 * @param folderName The folder name
	 * @return
	 * @throws RepositoryException
	 */
	public File[] makeFolder(SimpleAsset sa, String folderName) throws RepositoryException {

		Parameter param = new Parameter();

		param.setDescription("asset");
		param.assertNotNull(sa);
		
		File assetPath = sa.getPath();

		File residingFolder = assetPath.getParentFile();

		// FileFilter pff = new FileFilter();
		// pff.setValueToMatch(getAssetIdFromFilename(assetPath.toString()));

		// File[] assetPaths = getAssetPath(pff, residingFolder);
		
		File[] assetPaths = new File[] {sa.getPath(),sa.getContentFile()};
		
		if (assetPaths!=null && assetPaths[0]!=null) {			

			// Parent folder already exists, use it
			if (doesParentAssetFolderExist(assetPath)) {
				return new File[]{residingFolder,assetPaths[1]};
			}
			
			// Parent folder does not yet exist, create it
		
			try {
				File newParentFolder = new File(residingFolder + File.separator + folderName);
				File newContentFile = null;
				
				if (!newParentFolder.exists()) {
					if (newParentFolder.mkdir()) {
						String baseNameWoExt = null;
						if (assetPaths[0].exists()) {
							baseNameWoExt = newParentFolder + File.separator
							+ assetPaths[0].getName()
							.replaceAll(Configuration.PROPERTIES_FILE_EXT + "$" // Last index
									,"");
							File newLoc = new File(baseNameWoExt + PARENT_FILE_EXT);
							if (assetPaths[0].renameTo(newLoc)) {
								sa.setPath(newLoc);
							}
						}
                        logger.info("parent content path is: " + assetPaths[1]);
						if (assetPaths[1]!=null && assetPaths[1].exists()) {
							newContentFile = new File(newParentFolder, assetPaths[1].getName());

                            Path srcPath = assetPaths[1].toPath();
                            Path dstPath = newContentFile.toPath();

                            Files.move(srcPath, dstPath); // This API call is platform independent

//							if (assetPaths[1].renameTo(newContentFile)) {
//                                logger.info("moved parent content path is: " + assetPaths[1] + " to " + newContentFile);
//								sa.setContentPath(newContentFile);
//							}	else {
//                                logger.info("could not move parent content path : " + assetPaths[1] + " to " + newContentFile);
//                            }
						}							
					} else {
						logger.warning("newParentFolder could not be created. " + newParentFolder);
					}						
				} 
				
				return new File[]{newParentFolder,newContentFile};
			} catch (Exception ex) {
				logger.warning("makeFolder failed for assetPath: <" + assetPath + "> because: " + ex.toString());
			}
		}
 
		return new File[]{residingFolder,null};
	}
	
	public synchronized File[] getFile(File dir, String[] names, ArtifactId id, boolean retrieveById) throws RepositoryException {

        Parameter param = new Parameter();

        param.setDescription("id");
        param.assertNotNull(id);


        String safeName = null;
		try {
			safeName = FolderManager.getSafeName(names);
		} catch (RepositoryException re) {
				throw new RepositoryException(RepositoryException.NULL_ARGUMENT + ": " + re.toString());				
		}
		
		String assetBaseFile = dir + File.separator + safeName;
		File assetPropFile = new File(assetBaseFile + Configuration.DOT_SEPARATOR + Configuration.PROPERTIES_FILE_EXT);
		
		if (retrieveById) {
			File[] f = findById(dir, id); // Exception when file does not exist
			if (f!=null && f[0]!=null && f[0].exists()) {
				File parentDir = f[0].getParentFile();
				return new File[]{f[0] 									// file w/ extension 			 			[0]
						,new File(parentDir + File.separator + safeName) // Base name file part w/o extension 		[1]
						,new File(safeName) 						// name-id only									[2]
					};

			} else 
				throw new RepositoryException(RepositoryException.ASSET_NOT_FOUND + ": fn=" + assetPropFile);
	}
		
		String newId = safeName;
		// Append suffix to make a new file



		int cx = 0;


		while (assetPropFile.exists() && cx++ <50) { // safe loop limit
            logger.info("file already exists: " + assetPropFile.toString() + " id: " + id.getIdString());
//			newId = safeName + "_" + (cx);
			assetBaseFile = dir + File.separator + safeName + "_" + (cx) + ((id!=null)?"_" + id.getIdString() :"");
			assetPropFile = new File(assetBaseFile +  Configuration.DOT_SEPARATOR + Configuration.PROPERTIES_FILE_EXT);  			
		}

		
		
		return new File[]{assetPropFile // file w/ extension 							 [0]
			,new File(assetBaseFile) // Base name file part w/o extension				 [1]
			,new File(newId) // name-id only											 [2]
			,(cx>0)?assetPropFile:null // just a non-null flag  to indicate counter trip [3]
		};

	}
	
	/**
	 * 
	 * @param dir
	 * @param id
	 * @return
	 * @throws RepositoryException
	 */
	public File[] findById(File dir, ArtifactId id) throws RepositoryException {

		Parameter param = new Parameter();

		param.setDescription("dir");
		param.assertNotNull(dir);		
		
		File[] assetPaths = getAssetFileById(id,dir);
		
		if (assetPaths!=null && assetPaths[0]!=null) {
			return assetPaths; 
		} else if (assetPaths==null) {
			throw new RepositoryException(RepositoryException.ASSET_NOT_FOUND + ": Id=" + id.getIdString());
		}

		return new File[]{dir,null};
		
		
	}
	
	
	public File[] getAssetFileById(ArtifactId id, File baseDir) throws RepositoryException {
        FileFilter pff = new FileFilter();
        pff.setValueToMatch(id.getIdString());

        return getAssetPath(pff, baseDir, null);

	}

    /**
     *
     * @param name Matches part of the name
     * @param baseDir
     * @return
     * @throws RepositoryException
     */
    public File[] getAssetFileByName(String name, File baseDir, String parentId) throws RepositoryException {
        FileFilter pff = new FileFilter();
        pff.setValueToMatch(name);
        pff.setSearchPropertyKey(PropertyKey.ASSET_NAME);


        return getAssetPath(pff, baseDir, parentId);
    }
	
	
	/**
	 * 
	 * @param filename The filename to extract the value from
	 * @return The Id
	 */
	public static String getAssetIdFromFilename(String filename) throws RepositoryException {
		Parameter param = new Parameter();
		param.setDescription("fn");
		param.assertNotNull(filename);

		String fnPart = filename.replace("." + Configuration.PARENT_TAG + ".", Configuration.DOT_SEPARATOR);
		fnPart = fnPart.replace(Configuration.DOT_SEPARATOR + Configuration.PROPERTIES_FILE_EXT, Configuration.DOT_SEPARATOR);
		
		int extIdx = fnPart.lastIndexOf(Configuration.DOT_SEPARATOR);
		
		if (extIdx>-1) {
			return fnPart.substring(0, extIdx);
		} 
		
		throw new RepositoryException(RepositoryException.OPERATION_FAILED + ": fn=" + filename);
	}

	/**
	 * Provide at least one non-null value. The first non-null value will be returned as a safeName.
	 * @param str A list of potentially unsafe strings
	 * @return A safe name
	 * @throws RepositoryException 
	 */
	public static String getSafeName(String[] str) throws RepositoryException {
		if (str!=null) {
			for (String s : str) {
				if (s!=null) {
					String safeName = s.replaceAll("[^@A-Za-z0-9-_. ]+", " ");
					return safeName.trim();
				}
			}
		}
		throw new RepositoryException(RepositoryException.NULL_ARGUMENT + "Must provide at least one non-null value.");
	}


	/**
	 * 
	 * @author skb1
	 *
	 */
	private class FileFilter implements FilenameFilter {
		private String valueToMatch = "";
		private File[] directMatch = new File[]{null,null};		

        private PropertyKey searchPropertyKey = PropertyKey.ASSET_ID; // Default

        // Internal use only -- to be set by getAssetPath
        private String parentId;

		/**
		 * Returns the first matching file with the specified valueToMatch (Ex. Id value)
		 */
		@Override
		public boolean accept(File dir, String name) {
			File f = new File(dir, name);

				try {

					if (f.isDirectory() && getParentId()==null) {
						return true;
					} else if (name.endsWith(Configuration.PROPERTIES_FILE_EXT) && getSearchPropertyKey()!=null) { // Deep scan because files could use a displayName convention and the Id is embedded in the property file. It is probably more reliable this way anyways.
						Properties props = new Properties();
						loadProps(props, f);
						String propVal = props.getProperty(getSearchPropertyKey().toString());

                        if (propVal!=null && propVal.equals(getValueToMatch())) { // Value(Id) is case sensitive
                            if (getParentId()!=null && !"".equals(getParentId())) {
                                if ("$topLevelAsset".equals(getParentId())) {
                                    if  (!props.containsKey(PropertyKey.PARENT_ID.toString())) {
                                        directMatch[0] = f;
                                        return true;
                                    } else
                                        return false;
                                }

                                if (getParentId().equals(props.getProperty(PropertyKey.PARENT_ID.toString()))) {
                                    directMatch[0] = f;
                                    return true;
                                } else
                                    return false;
                            }
                            directMatch[0] = f;
                            return true;
						}						
					}
				} catch (Exception ex) {
					logger.warning(ex.toString());
				}
				 
				return false;
		}

		public String getValueToMatch() {
			return valueToMatch;
		}

		public void setValueToMatch(String fileName) {
			this.valueToMatch = fileName;
		}

		public File[] getDirectMatch() {
			return directMatch;
		}


        public PropertyKey getSearchPropertyKey() {
            return searchPropertyKey;
        }

        public void setSearchPropertyKey(PropertyKey searchPropertyKey) {
            this.searchPropertyKey = searchPropertyKey;
        }



        public String getParentId() {
            return parentId;
        }

        protected void setParentId(String parentId) {
            this.parentId = parentId;
        }
    }

	private static File[] getAssetPath(FileFilter pff, File dir, String parentId) throws RepositoryException {
		
		File[] assetFileNames = dir.listFiles(pff);
		
		if (pff.getDirectMatch()!=null && pff.getDirectMatch()[0]!=null) {
			return pff.getDirectMatch();	
		} else {
			if (assetFileNames!=null && assetFileNames.length>0 && assetFileNames[0]!=null) {

                if (assetFileNames!=null && assetFileNames.length>0) {
                    for (File assetFileName : assetFileNames ) { // Not sure if this is still required after relying upon directMatch[0]
                        if (assetFileName.isFile()) {
                            return new File[]{assetFileName,null};
                        }
                    }

                    // Scroll directories separately and give first preference to files since they are more of a direct match
                    for (File assetFileName : assetFileNames ) {
                        if (assetFileName.isDirectory()) {

                                // This limits the search depth to immediate children or 1 level deep if applicable where the child is also a parent
                            if (parentId!=null || !"".equals(parentId)) {
                                pff.setParentId(parentId);
                            }
                            File[] nestedMatch = getAssetPath(pff, assetFileName, null);
                            if (nestedMatch!=null && nestedMatch.length>0 && nestedMatch[0]!=null)
                                return nestedMatch;
                        }
                    }
                }

			} 


		}
		
		return null;
	}

	/**
	 * @param assetPropFile The property file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void loadProps(Properties props, File assetPropFile) throws FileNotFoundException,
			IOException, RepositoryException {
		
		Parameter p = new Parameter();		
		p.setDescription("Properties");
		p.assertNotNull(props);
		
		FileReader fr = new FileReader(assetPropFile);
		props.load(fr);
		fr.close();
		
	}
	
	/**
	 * @param src Source path
	 * @param dst Destination path
	 * @throws RepositoryException
	 */
	public static File[] moveChildToParent(File[] src, File[] dst)
			throws RepositoryException {
		
		Parameter param = new Parameter();
		param.setDescription("Src");
		param.assertNotNull(src);
		
		param.setDescription("Dst");
		param.assertNotNull(dst);
		
		param.setDescription("Src length");
		param.assertEquals(src.length, 2);
		param.setDescription("Dst length");
		param.assertEquals(dst.length, 2);


        try {
            // Props
            Files.move(src[0].toPath(), dst[0].toPath()); // This API call is platform independent

        } catch (Throwable t) {
            logger.warning("Child relocation failed after its parent was associated with a new folder from <" + src[0].toString() + "> to <" + dst[0].toString() + ">");
            t.printStackTrace();
        }

        try {
            // Content file
            if (src[1].exists()) {
                Files.move(src[1].toPath(), dst[1].toPath());
            }

        } catch (Throwable t) {
            logger.warning("Asset content relocation failed after its parent was associated with a new folder from <" + src[1].toString() + "> to <" + dst[1].toString() + ">");
            t.printStackTrace();
        }


		return new File[]{dst[0],dst[1]}; // Props, Cont
		

	}

}
