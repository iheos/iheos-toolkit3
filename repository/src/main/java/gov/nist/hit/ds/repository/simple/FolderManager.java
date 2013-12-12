package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.Parameter;
import gov.nist.hit.ds.repository.api.RepositoryException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
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
	
	/**
	 * 
	 * @param reposDir
	 * @param assetId
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
		// pff.setFileNamePart(getAssetIdFromFilename(assetPath.toString())); 

		// File[] assetPaths = getAssetPath(pff, residingFolder);
		
		File[] assetPaths = new File[] {sa.getPath(),sa.getContentFile(null)};
		
		if (assetPaths!=null && assetPaths[0]!=null) {			

			// Parent folder already exists, use it
			if (assetPath.getName().endsWith(Configuration.DOT_SEPARATOR +  PARENT_FILE_EXT)) {
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
						if (assetPaths[1]!=null && assetPaths[1].exists()) {
							newContentFile = new File(newParentFolder + File.separator + assetPaths[1].getName());
							if (assetPaths[1].renameTo(newContentFile)) {
								sa.setContentPath(newContentFile);
							}								
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
	
	public File[] getFile(File dir, String[] names, boolean directLoad) throws RepositoryException {
		String safeName = null;
		try {
			safeName = FolderManager.getSafeName(names);
		} catch (RepositoryException re) {
				throw new RepositoryException(RepositoryException.NULL_ARGUMENT + ": " + re.toString());				
		}
		
		String assetBaseFile = dir + File.separator + safeName;
		File assetPropFile = new File(assetBaseFile + Configuration.DOT_SEPARATOR + Configuration.PROPERTIES_FILE_EXT);

		if (directLoad) {
			return new File[]{assetPropFile // file w/ extension 			 			[0]
					,new File(assetBaseFile) // Base name file part w/o extension 		[1]
					,new File(safeName) // name-id only									[2]
				};
		}
		
		String newId = safeName;
		// Append suffix to make a new file
		int cx = 0;

		while (assetPropFile.exists() && cx++ <50) { // safe loop limit
			newId = safeName + "_" + (cx);
			assetBaseFile = dir + File.separator + newId;
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
	 * @param assetId
	 * @return
	 * @throws RepositoryException
	 */
	public File[] findById(File dir, File assetPath, String[] names) throws RepositoryException {

		Parameter param = new Parameter();

		param.setDescription("dir");
		param.assertNotNull(dir);		
		
		// Does it exist?
		if (assetPath!=null) {
			File residingFolder = assetPath.getParentFile();

			File[] assetPaths = getAssetFileById(new SimpleId(getAssetIdFromFilename(assetPath.toString())),residingFolder);
			
			if (assetPaths!=null && assetPaths[0]!=null) {
				return assetPaths; 
			} 
		} else { // New file
			return getFile(dir, names, true);
		}

		return new File[]{dir,null};
	}
	
	
	public File[] getAssetFileById(Id id, File baseDir) throws RepositoryException {
		FileFilter pff = new FileFilter();
		pff.setFileNamePart(id.getIdString()); 

		return getAssetPath(pff, baseDir);
		
	}
	
	
	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static String getAssetIdFromFilename(String fn) throws RepositoryException {
		Parameter param = new Parameter();
		param.setDescription("fn");
		param.assertNotNull(fn);

		String fnPart = fn.replace("." + Configuration.PARENT_TAG + ".", Configuration.DOT_SEPARATOR);
		fnPart = fnPart.replace(Configuration.DOT_SEPARATOR + Configuration.PROPERTIES_FILE_EXT, Configuration.DOT_SEPARATOR);
		
		int extIdx = fnPart.lastIndexOf(Configuration.DOT_SEPARATOR);
		
		if (extIdx>-1) {
			return fnPart.substring(0, extIdx);
		} 
		
		throw new RepositoryException(RepositoryException.OPERATION_FAILED + ": fn=" + fn);
	}

	/**
	 * Provide at least one non-null value. The first non-null value will be returned as a safeName.
	 * @param str
	 * @return
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
		private String fileNamePart = "";
		private File[] directMatch = new File[]{null,null};		

		@Override
		public boolean accept(File dir, String name) {
			File f = new File(dir + File.separator + name);
			if (name.contains(getFileNamePart())) {
					if (name.endsWith(Configuration.PROPERTIES_FILE_EXT)) {
						directMatch[0] = f;	// Props
					} else 
						directMatch[1] = f; // Content file ?					
				return true;
			} else {
				try {
					if (f.isDirectory()) {
						return true;
					}
				} catch (Exception ex) {
					
				}
				 
			}
				return false;
		}

		public String getFileNamePart() {
			return fileNamePart;
		}

		public void setFileNamePart(String fileName) {
			this.fileNamePart = fileName;
		}

		public File[] getDirectMatch() {
			return directMatch;
		}

		
	};

	private static File[] getAssetPath(FileFilter pff, File dir) throws RepositoryException {
		
		File[] assetFileNames = dir.listFiles(pff);
		
		if (pff.getDirectMatch()!=null && pff.getDirectMatch()[0]!=null) {
			return pff.getDirectMatch();	
		} else {
			for (File f : assetFileNames) {
				File[] nestedMatch = getAssetPath(pff, f);
				if (nestedMatch!=null && nestedMatch[0]!=null)
					return nestedMatch;
			}
		}
		
		return null;
	}

	/**
	 * @param assetPropFile
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
	 * @param initialState
	 * @param finalState
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
		

		if (!src[0].renameTo(dst[0])) {
				logger.warning("Child relocation failed after its parent was associated with a new folder from <" + src[0].toString() + "> to <" + dst[0].toString() + ">");
				return new File[]{src[0],src[1]}; // no change to Props or Cont 
		} 
		if (src[1].exists()) {
				if (!src[1].renameTo(dst[1])) {
					logger.warning("Asset content relocation failed after its parent was associated with a new folder from <" + src[1].toString() + "> to <" + dst[1].toString() + ">");
				}			
		}
		return new File[]{dst[0],dst[1]}; // Props, Cont
		

	}

}
