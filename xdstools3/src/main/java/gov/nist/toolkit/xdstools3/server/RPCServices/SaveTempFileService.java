package gov.nist.toolkit.xdstools3.server.RPCServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Service class to save a temporary file on the server. The file is save into the webapp directory.
 */
public class SaveTempFileService {
    private static final long serialVersionUID = 1L;
    private static File FILE_REPOSITORY;
    private final Logger logger = Logger.getLogger(SaveTempFileService.class.getName());

    public SaveTempFileService(){
        String rootDirPath = System.getProperty("user.dir");
        logger.info("Root Path: " + rootDirPath);
        File fileFolder=new File(new File(rootDirPath),"/files/");
        if(!fileFolder.exists()) {
            logger.info("Create storage folder 'files''");
            fileFolder.mkdir();
            logger.info("... storage folder 'files' created.");
        }
        FILE_REPOSITORY=fileFolder;
        logger.info("New storage folder is: "+FILE_REPOSITORY.getAbsolutePath());
    }

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

        // return created file's name
        return saveAsXMLFile(filename,fileContent);

//        return filename;
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
        if (fileName == null || fileName.equals(""))
            fileName = UUID.randomUUID().toString();
        if (!(fileName.matches(".*(\\.[0-9A-Za-z]+)$") || fileName.matches(".*\\.xml$")))
            fileName += ".xml";

        // Save xml file content into "files" repository
        logger.info("Metadata xml file creation...");

        FileOutputStream out;

        try {
            out = new FileOutputStream(new File(FILE_REPOSITORY, fileName));
            logger.info("... writing file ("+fileName+") in "+FILE_REPOSITORY.getAbsolutePath()+"...");
            out.write(fileContent.getBytes());
            out.close();
        } catch (IOException e) {
            logger.warning("Error when writing metadata file on server.\n" + e.getMessage());
            e.printStackTrace();
        }
        logger.fine("... temporary file created: " + FILE_REPOSITORY + "/" + fileName);

        // return created file's name
        return fileName;
    }
}
