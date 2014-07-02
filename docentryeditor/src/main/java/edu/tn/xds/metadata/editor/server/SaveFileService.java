package edu.tn.xds.metadata.editor.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * This class enables to create a xml metadata file's content on the server and
 * retrieve the name of the generated file.
 *
 * @author Olivier
 *
 */
public class SaveFileService implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Logger logger = Logger.getLogger(SaveFileService.class.getName());

	private final String FILE_REPOSITORY = "files";

	/**
	 * Distant method that save a String into a generated xml file in the
	 * server. It returns the generated file name.
	 *
	 * @param fileContent
	 * @return String filename
	 */
	public String saveAsXMLFile(String fileContent) {
		logger.info("Saving xml file...");

		// Random name created for save on server
		String filename = UUID.randomUUID().toString() + ".xml";

		// Save xml file content into "files" repository
		logger.info("Metadata xml file creation...");

		FileOutputStream out;

		try {
			out = new FileOutputStream(new File(FILE_REPOSITORY, filename));

			out.write(fileContent.getBytes());
			out.close();
		} catch (IOException e) {
			logger.warning("Error when writing metadata file on server.\n"+e.getMessage());
			e.printStackTrace();
		}
		logger.fine("... temporary file created: " + FILE_REPOSITORY + "/" + filename);

		// return created file's name
		return filename;
	}

    /**
	 * Distant method that save a String into a generated xml file in the
	 * server. It returns the generated file name.
	 *
	 * @param fileContent
     * @param filename
	 * @return String filename
	 */
	public String saveAsXMLFile(String filename, String fileContent) {
		logger.info("Saving xml file...");

		// Random name created for save on server
		String fileName = filename;
        if(!(fileName.matches("(\\.[0-9A-Za-z]+)$")))
            fileName+=".xml";

		// Save xml file content into "files" repository
		logger.info("Metadata xml file creation...");

		FileOutputStream out;

		try {
			out = new FileOutputStream(new File(FILE_REPOSITORY, fileName));

			out.write(fileContent.getBytes());
			out.close();
		} catch (IOException e) {
			logger.warning("Error when writing metadata file on server.\n"+e.getMessage());
			e.printStackTrace();
		}
		logger.fine("... temporary file created: " + FILE_REPOSITORY + "/" + fileName);

		// return created file's name
		return fileName;
	}

}
