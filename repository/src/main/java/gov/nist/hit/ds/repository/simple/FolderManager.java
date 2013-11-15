package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.Parameter;
import gov.nist.hit.ds.repository.api.PropertyKey;
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
public class FolderManager {

	private static final String PARENT_FILE_EXT = Configuration.PARENT_TAG + Configuration.DOT_SEPARATOR + Configuration.PROPERTIES_FILE_EXT;
	// Use when folder name cannot be determined
	private static final String LOST_AND_FOUND = "lost_and_found";
	private static Logger logger = Logger.getLogger(FolderManager.class.getName());
	
	/**
	 * 
	 * @param reposDir
	 * @param assetId
	 * @return
	 * @throws RepositoryException
	 */
	public File makeFolder(File reposDir, String assetId) throws RepositoryException {

		Parameter param = new Parameter();

		param.setDescription("reposDir");
		param.assertNotNull(reposDir);
		
		param.setDescription("assetId");
		param.assertNotNull(assetId);
		
		FileFilter pff = new FileFilter();
		pff.setFileNamePart(assetId); 

		File[] assetPath = getAssetPath(pff, reposDir, assetId);
		if (assetPath!=null && assetPath[0]!=null) {

			
			File residingFolder = assetPath[0].getParentFile();
			// Parent folder already exists, use it
			if (assetPath[0].getName().endsWith(PARENT_FILE_EXT)) {
				return residingFolder;
			}
		
			// Parent folder does not yet exist, create it
			Properties props = new Properties();
			
			try {
				loadProps(props, assetPath[0]);
				// Make a folder name out of the possible values whichever is non-null first
				String folderName = getSafeName(new String[]{
						props.getProperty(PropertyKey.DISPLAY_NAME.toString())
						,props.getProperty(PropertyKey.DESCRIPTION.toString())
						,props.getProperty(PropertyKey.ASSET_ID.toString())
						,LOST_AND_FOUND
				});				

				File newParentFolder = new File(residingFolder + File.separator + folderName);
				
				if (!newParentFolder.exists()) {
					if (newParentFolder.mkdir()) {
						if (assetPath[0].exists())
							assetPath[0].renameTo(new File(newParentFolder + File.separator 
									+ assetPath[0].getName()
									.replaceAll(Configuration.PROPERTIES_FILE_EXT + "$" // Last index
											, PARENT_FILE_EXT)));									
						if (assetPath[1]!=null && assetPath[1].exists())
							assetPath[1].renameTo(new File(newParentFolder + File.separator + assetPath[1].getName()));
					} else {
						logger.warning("newParentFolder could not be created. " + newParentFolder);
					}						
				} 
				
				return newParentFolder;
			} catch (Exception ex) {
				logger.warning("makeFolder failed for assetId: <" + assetId + "> because: " + ex.toString());
			}
		}
 
		return reposDir;
	}
	
	/**
	 * 
	 * @param reposDir
	 * @param assetId
	 * @return
	 * @throws RepositoryException
	 */
	public File[] findById(File reposDir, String assetId, String[] names) throws RepositoryException {

		Parameter param = new Parameter();

		param.setDescription("reposDir");
		param.assertNotNull(reposDir);
		
		param.setDescription("assetId");
		param.assertNotNull(assetId);


		FileFilter pff = new FileFilter();
		pff.setFileNamePart(assetId); 

		File[] assetPath = getAssetPath(pff, reposDir, assetId);
		
		// Does it exist?
		if (assetPath!=null && assetPath[0]!=null) {
			return assetPath; 
		} else {// New file
			String safeName = null;
			try {
				safeName = FolderManager.getSafeName(names);
				safeName += "_";
			} catch (RepositoryException re) {
				if (assetId!=null && !"".equals(assetId)) {
					safeName = ""; // no usable properties were available, just use id for now and update filename later when a property becomes available
				} else { 
					throw new RepositoryException(RepositoryException.NULL_ARGUMENT + ": " + re.toString());
				}
			}
			String assetFile = reposDir + File.separator + safeName + assetId + Configuration.DOT_SEPARATOR;
			return new File[]{new File(assetFile + Configuration.PROPERTIES_FILE_EXT),null};
		}
//		else {		
//		File assetFile = new File(reposDir + File.separator + assetId + "." + Configuration.PROPERTIES_FILE_EXT);
//		return new File[]{assetFile,null};
//	}


		
	}
	
	/**
	 * 
	 * @param filename
	 * @return
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
					return s.replaceAll("[^@A-Za-z0-9-_. ]+", "");
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

	private static File[] getAssetPath(FileFilter pff, File dir, String parentId) throws RepositoryException {
		
		File[] assetFileNames = dir.listFiles(pff);
		
		if (pff.getDirectMatch()!=null && pff.getDirectMatch()[0]!=null) {
			return pff.getDirectMatch();	
		} else {
			for (File f : assetFileNames) {
				File[] nestedMatch = getAssetPath(pff, f, parentId);
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
	public static File moveChildToParent(File[] src, File[] dst)
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
				return src[0]; // Props
		} 
		if (src[1].exists()) {
				if (!src[1].renameTo(dst[1])) {
					logger.warning("Asset content relocation failed after its parent was associated with a new folder from <" + src[1].toString() + "> to <" + dst[1].toString() + ">");
				}			
		}
		return dst[0]; // Props
		

	}

}
