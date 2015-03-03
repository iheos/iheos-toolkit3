package gov.nist.hit.ds.docentryeditor.server;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

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
 */
public class SaveFileService implements Serializable {

    private static final long serialVersionUID = 1L;
    private static File FILE_REPOSITORY;
    private final Logger logger = Logger.getLogger(SaveFileService.class.getName());

    public SaveFileService(){
//        String rootDirPath = System.getProperty("user.dir");
        String rootDirPath= RequestFactoryServlet.getThreadLocalServletContext().getRealPath("/");
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
        // TODO remove hard coded xml when submission set will be handler by the editor
        return saveAsXMLFile(filename,
                "<xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb=\"urn:ihe:iti:xds-b:2007\">\n" +
                "    <lcm:SubmitObjectsRequest xmlns:lcm=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\">\n" +
                "        <rim:RegistryObjectList xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\">"+
                fileContent.replace("displayName","name")+
                "<rim:RegistryPackage id=\"SubmissionSet01\"\n" +
                "                                 objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage\">\n" +
                "                <rim:Slot name=\"submissionTime\">\n" +
                "                    <rim:ValueList>\n" +
                "                        <rim:Value>20041225235050</rim:Value>\n" +
                "                    </rim:ValueList>\n" +
                "                </rim:Slot>\n" +
                "                <rim:Name>\n" +
                "                    <rim:LocalizedString value=\"Physical\"/>\n" +
                "                </rim:Name>\n" +
                "                <rim:Description>\n" +
                "                    <rim:LocalizedString value=\"Annual physical\"/>\n" +
                "                </rim:Description>\n" +
                "                <rim:Classification\n" +
                "                        classificationScheme=\"urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d\"\n" +
                "                        classifiedObject=\"SubmissionSet01\" nodeRepresentation=\"\"\n" +
                "                        objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification\"\n" +
                "                        id=\"id_11\">\n" +
                "                    <rim:Slot name=\"authorPerson\">\n" +
                "                        <rim:ValueList>\n" +
                "                            <rim:Value>^Dopplemeyer^Sherry^^^</rim:Value>\n" +
                "                        </rim:ValueList>\n" +
                "                    </rim:Slot>\n" +
                "                    <rim:Slot name=\"authorInstitution\">\n" +
                "                        <rim:ValueList>\n" +
                "                            <rim:Value>Cleveland Clinic</rim:Value>\n" +
                "                            <rim:Value>Berea Community</rim:Value>\n" +
                "                        </rim:ValueList>\n" +
                "                    </rim:Slot>\n" +
                "                    <rim:Slot name=\"authorRole\">\n" +
                "                        <rim:ValueList>\n" +
                "                            <rim:Value>Primary Surgon</rim:Value>\n" +
                "                        </rim:ValueList>\n" +
                "                    </rim:Slot>\n" +
                "                    <rim:Slot name=\"authorSpecialty\">\n" +
                "                        <rim:ValueList>\n" +
                "                            <rim:Value>Orthopedic</rim:Value>\n" +
                "                        </rim:ValueList>\n" +
                "                    </rim:Slot>\n" +
                "                </rim:Classification>\n" +
                "                <rim:Classification\n" +
                "                        classificationScheme=\"urn:uuid:aa543740-bdda-424e-8c96-df4873be8500\"\n" +
                "                        classifiedObject=\"SubmissionSet01\" nodeRepresentation=\"History and Physical\"\n" +
                "                        objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification\"\n" +
                "                        id=\"id_12\">\n" +
                "                    <rim:Slot name=\"codingScheme\">\n" +
                "                        <rim:ValueList>\n" +
                "                            <rim:Value>Connect-a-thon contentTypeCodes</rim:Value>\n" +
                "                        </rim:ValueList>\n" +
                "                    </rim:Slot>\n" +
                "                    <rim:Name>\n" +
                "                        <rim:LocalizedString value=\"History and Physical\"/>\n" +
                "                    </rim:Name>\n" +
                "                </rim:Classification>\n" +
                "                <rim:ExternalIdentifier\n" +
                "                        identificationScheme=\"urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8\"\n" +
                "                        value=\"2009.9.1.2456\"\n" +
                "                        objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier\"\n" +
                "                        id=\"id_13\" registryObject=\"SubmissionSet01\">\n" +
                "                    <rim:Name>\n" +
                "                        <rim:LocalizedString value=\"XDSSubmissionSet.uniqueId\"/>\n" +
                "                    </rim:Name>\n" +
                "                </rim:ExternalIdentifier>\n" +
                "                <rim:ExternalIdentifier\n" +
                "                        identificationScheme=\"urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832\"\n" +
                "                        value=\"1.3.6.1.4.1.21367.2009.1.2.1\"\n" +
                "                        objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier\"\n" +
                "                        id=\"id_14\" registryObject=\"SubmissionSet01\">\n" +
                "                    <rim:Name>\n" +
                "                        <rim:LocalizedString value=\"XDSSubmissionSet.sourceId\"/>\n" +
                "                    </rim:Name>\n" +
                "                </rim:ExternalIdentifier>\n" +
                "                <rim:ExternalIdentifier\n" +
                "                        identificationScheme=\"urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446\"\n" +
                "                        value=\"76cc765a442f410^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO\"\n" +
                "                        objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier\"\n" +
                "                        id=\"id_15\" registryObject=\"SubmissionSet01\">\n" +
                "                    <rim:Name>\n" +
                "                        <rim:LocalizedString value=\"XDSSubmissionSet.patientId\"/>\n" +
                "                    </rim:Name>\n" +
                "                </rim:ExternalIdentifier>\n" +
                "            </rim:RegistryPackage>\n" +
                "            <rim:Classification classifiedObject=\"SubmissionSet01\"\n" +
                "                                classificationNode=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\" id=\"ID_1216346_1\"\n" +
                "                                objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification\"/>\n" +
                "            <rim:Association\n" +
                "                    associationType=\"urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember\"\n" +
                "                    sourceObject=\"SubmissionSet01\" targetObject=\"Document01\" id=\"ID_1216346_2\"\n" +
                "                    objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association\">\n" +
                "                <rim:Slot name=\"SubmissionSetStatus\">\n" +
                "                    <rim:ValueList>\n" +
                "                        <rim:Value>Original</rim:Value>\n" +
                "                    </rim:ValueList>\n" +
                "                </rim:Slot>\n" +
                "            </rim:Association>\n" +
                "        </rim:RegistryObjectList>\n" +
                "    </lcm:SubmitObjectsRequest>\n" +
                "</xdsb:ProvideAndRegisterDocumentSetRequest>"
        );

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

        // TODO Remove hard coded xml when submission set and association are done
        String outS= "<xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb=\"urn:ihe:iti:xds-b:2007\">\n" +
                "    <lcm:SubmitObjectsRequest xmlns:lcm=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\">\n" +
                "        <rim:RegistryObjectList xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\">"+
                fileContent.replace("displayName","name")+
                "<rim:RegistryPackage id=\"SubmissionSet01\"\n" +
                "                                 objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage\">\n" +
                "                <rim:Slot name=\"submissionTime\">\n" +
                "                    <rim:ValueList>\n" +
                "                        <rim:Value>20041225235050</rim:Value>\n" +
                "                    </rim:ValueList>\n" +
                "                </rim:Slot>\n" +
                "                <rim:Name>\n" +
                "                    <rim:LocalizedString value=\"Physical\"/>\n" +
                "                </rim:Name>\n" +
                "                <rim:Description>\n" +
                "                    <rim:LocalizedString value=\"Annual physical\"/>\n" +
                "                </rim:Description>\n" +
                "                <rim:Classification\n" +
                "                        classificationScheme=\"urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d\"\n" +
                "                        classifiedObject=\"SubmissionSet01\" nodeRepresentation=\"\"\n" +
                "                        objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification\"\n" +
                "                        id=\"id_11\">\n" +
                "                    <rim:Slot name=\"authorPerson\">\n" +
                "                        <rim:ValueList>\n" +
                "                            <rim:Value>^Dopplemeyer^Sherry^^^</rim:Value>\n" +
                "                        </rim:ValueList>\n" +
                "                    </rim:Slot>\n" +
                "                    <rim:Slot name=\"authorInstitution\">\n" +
                "                        <rim:ValueList>\n" +
                "                            <rim:Value>Cleveland Clinic</rim:Value>\n" +
                "                            <rim:Value>Berea Community</rim:Value>\n" +
                "                        </rim:ValueList>\n" +
                "                    </rim:Slot>\n" +
                "                    <rim:Slot name=\"authorRole\">\n" +
                "                        <rim:ValueList>\n" +
                "                            <rim:Value>Primary Surgon</rim:Value>\n" +
                "                        </rim:ValueList>\n" +
                "                    </rim:Slot>\n" +
                "                    <rim:Slot name=\"authorSpecialty\">\n" +
                "                        <rim:ValueList>\n" +
                "                            <rim:Value>Orthopedic</rim:Value>\n" +
                "                        </rim:ValueList>\n" +
                "                    </rim:Slot>\n" +
                "                </rim:Classification>\n" +
                "                <rim:Classification\n" +
                "                        classificationScheme=\"urn:uuid:aa543740-bdda-424e-8c96-df4873be8500\"\n" +
                "                        classifiedObject=\"SubmissionSet01\" nodeRepresentation=\"History and Physical\"\n" +
                "                        objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification\"\n" +
                "                        id=\"id_12\">\n" +
                "                    <rim:Slot name=\"codingScheme\">\n" +
                "                        <rim:ValueList>\n" +
                "                            <rim:Value>Connect-a-thon contentTypeCodes</rim:Value>\n" +
                "                        </rim:ValueList>\n" +
                "                    </rim:Slot>\n" +
                "                    <rim:Name>\n" +
                "                        <rim:LocalizedString value=\"History and Physical\"/>\n" +
                "                    </rim:Name>\n" +
                "                </rim:Classification>\n" +
                "                <rim:ExternalIdentifier\n" +
                "                        identificationScheme=\"urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8\"\n" +
                "                        value=\"2009.9.1.2456\"\n" +
                "                        objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier\"\n" +
                "                        id=\"id_13\" registryObject=\"SubmissionSet01\">\n" +
                "                    <rim:Name>\n" +
                "                        <rim:LocalizedString value=\"XDSSubmissionSet.uniqueId\"/>\n" +
                "                    </rim:Name>\n" +
                "                </rim:ExternalIdentifier>\n" +
                "                <rim:ExternalIdentifier\n" +
                "                        identificationScheme=\"urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832\"\n" +
                "                        value=\"1.3.6.1.4.1.21367.2009.1.2.1\"\n" +
                "                        objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier\"\n" +
                "                        id=\"id_14\" registryObject=\"SubmissionSet01\">\n" +
                "                    <rim:Name>\n" +
                "                        <rim:LocalizedString value=\"XDSSubmissionSet.sourceId\"/>\n" +
                "                    </rim:Name>\n" +
                "                </rim:ExternalIdentifier>\n" +
                "                <rim:ExternalIdentifier\n" +
                "                        identificationScheme=\"urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446\"\n" +
                "                        value=\"76cc765a442f410^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO\"\n" +
                "                        objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier\"\n" +
                "                        id=\"id_15\" registryObject=\"SubmissionSet01\">\n" +
                "                    <rim:Name>\n" +
                "                        <rim:LocalizedString value=\"XDSSubmissionSet.patientId\"/>\n" +
                "                    </rim:Name>\n" +
                "                </rim:ExternalIdentifier>\n" +
                "            </rim:RegistryPackage>\n" +
                "            <rim:Classification classifiedObject=\"SubmissionSet01\"\n" +
                "                                classificationNode=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\" id=\"ID_1216346_1\"\n" +
                "                                objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification\"/>\n" +
                "            <rim:Association\n" +
                "                    associationType=\"urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember\"\n" +
                "                    sourceObject=\"SubmissionSet01\" targetObject=\"Document01\" id=\"ID_1216346_2\"\n" +
                "                    objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association\">\n" +
                "                <rim:Slot name=\"SubmissionSetStatus\">\n" +
                "                    <rim:ValueList>\n" +
                "                        <rim:Value>Original</rim:Value>\n" +
                "                    </rim:ValueList>\n" +
                "                </rim:Slot>\n" +
                "            </rim:Association>\n" +
                "        </rim:RegistryObjectList>\n" +
                "    </lcm:SubmitObjectsRequest>\n" +
                "</xdsb:ProvideAndRegisterDocumentSetRequest>";

        try {
            out = new FileOutputStream(new File(FILE_REPOSITORY, fileName));
            logger.info("... writing file ("+fileName+") in "+FILE_REPOSITORY.getAbsolutePath()+"...");
            out.write(outS.getBytes());
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
