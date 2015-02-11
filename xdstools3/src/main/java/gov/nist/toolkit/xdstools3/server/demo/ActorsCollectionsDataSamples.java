package gov.nist.toolkit.xdstools3.server.demo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onh2 on 8/26/2014.
 *
 * Provides test data / GUI demo data for Actors and Collections widgets
 */
public enum ActorsCollectionsDataSamples {
    instance;

    Map<String,List<String>> testsMap;
    Map<String,String> collectionsReadme;

    private ActorsCollectionsDataSamples(){
        testsMap = new HashMap<>();
        collectionsReadme = new HashMap<>();

        List<String> sq = new ArrayList<>();
        sq.add("12346");
        testsMap.put("Initialize_for_Stored_Query", sq);

        List<String> rg=new ArrayList<>();
        rg.add("12309");
        rg.add("12310");
        rg.add("12311");
        rg.add("12312");
        rg.add("12313");
        rg.add("12314");
        rg.add("15500");
        rg.add("12300");
        rg.add("12301");
        rg.add("12306");
        rg.add("12307");
        rg.add("12308");
        testsMap.put("rg", rg);

        List<String> rep=new ArrayList<>();
        rep.add("12318");
        rep.add("12345");
        rep.add("11966");
        rep.add("11979");
        rep.add("11983");
        rep.add("11986");
        rep.add("12021");
        rep.add("12028");
        rep.add("12029");
        rep.add("12369");
        testsMap.put("rep", rg);

        List<String> reg=new ArrayList<>();
        reg.add("12361");
        reg.add("12374");
        reg.add("11897");
        reg.add("11898");
        reg.add("11899");
        reg.add("11901");
        reg.add("11902");
        reg.add("11903");
        reg.add("11904");
        reg.add("11905");
        reg.add("11906");
        reg.add("11907");
        reg.add("11908");
        reg.add("11909");
        reg.add("11990");
        reg.add("11991");
        reg.add("11992");
        reg.add("11993");
        reg.add("11994");
        reg.add("11995");
        reg.add("11996");
        reg.add("11997");
        reg.add("11998");
        reg.add("11999");
        reg.add("12000");
        reg.add("12001");
        reg.add("12002");
        reg.add("12004");
        reg.add("12084");
        reg.add("12323");
        reg.add("12326");
        reg.add("12327");
        reg.add("12364");
        reg.add("12368");
        reg.add("12370");
        reg.add("12379");
        reg.add("15800");
        reg.add("15802");
        testsMap.put("reg", reg);

        List<String> rec = new ArrayList<>();
        rec.add("12371");
        rec.add("12372");
        rec.add("12373");
        rec.add("12375");
        rec.add("12376");
        testsMap.put("rec", rec);

        collectionsReadme.put("11897", "SQ.b FindDocuments Stored Query\n" +
                "\n" +
                "Stored Query must be run over SOAP 1.2.  \n" +
                "\n" +
                "This testplan contains many many test steps each validating \n" +
                "a feature of the FindDocuments stored query.  This test relies\n" +
                "on test 12346 to pre-load the Registry with known test data.\n" +
                "\n" +
                "The test steps are:\n" +
                "\n" +
                "and:\n" +
                "   tests AND logic in SQ\n" +
                "   eventCodeList having both T-D4909  AND  T-62002\n" +
                "   must return single document\n" +
                "\n" +
                "classcode_one: \n" +
                "   queries for: \n" +
                "      classCode = 'Consult'\n" +
                "      status = 'Approved'\n" +
                "   must return: DocD\n" +
                "\n" +
                "classcode_practicesetting:\n" +
                "  queries for:\n" +
                "    classCode = 'Communication'\n" +
                "    practiceSettingCode = 'Cardiology'\n" +
                "    status = 'Approved'\n" +
                "  must return: DocB\n" +
                "\n" +
                "classcode_scheme_2: \n" +
                "   queries for:\n" +
                "     classCode = ('Communication', 'Communication')\n" +
                "     classCodeScheme = ('Connect-a-thon classCodes','Connect-a-thon classCodes 2')\n" +
                "     status = 'Approved'\n" +
                "   must return: DocB, DocC\n" +
                "\n" +
                "classcode_scheme_mismatch:\n" +
                "  queries for:\n" +
                "      classCode = 'Communication'\n" +
                "      classCodeScheme = ('Connect-a-thon classCodes', 'a') \n" +
                "      status = 'Approved'\n" +
                "  must return: none (mismatch on codes and scheme)\n" +
                "\n" +
                "classcode_two: \n" +
                "   queries for: \n" +
                "      classCode = 'Consult' or 'History and Physical'\n" +
                "      status = 'Approved'\n" +
                "   must return: DocD, DocE, DocA\n" +
                "\n" +
                "confcode:\n" +
                "  queries for:\n" +
                "    confidentialityCode = ('1.3.6.1.4.1.21367.2006.7.101',\n" +
                "                                    '1.3.6.1.4.1.21367.2006.7.103')\n" +
                "    status = 'Approved'\n" +
                "  must return: DocB, DocC\n" +
                "\n" +
                "creationtime_between:\n" +
                "  queries for:\n" +
                "    creationTimeFrom: 20040101\n" +
                "    creationTimeTo:  20050101\n" +
                "    status = 'Approved'\n" +
                "  must return: DocB\n" +
                "\n" +
                "creationtime_left_edge:\n" +
                "  queries for:\n" +
                "    creationTimeFrom: 20041224\n" +
                "    creationTimeTo:  20050101\n" +
                "    status = 'Approved'\n" +
                "  must return: DocB\n" +
                "\n" +
                "creationtime_right_edge:\n" +
                "  queries for:\n" +
                "    creationTimeFrom: 20041124\n" +
                "    creationTimeTo:  20041224\n" +
                "    status = 'Approved'\n" +
                "  must return: none\n" +
                "\n" +
                "creationtime_practicesetting:\n" +
                "  queries for: \n" +
                "    creation time:  20020101 thru 20060101\n" +
                "    practiceSettingCode: 'Dialysis'\n" +
                "    status = 'Approved'\n" +
                "  must return: DocD\n" +
                "\n" +
                "deprecated:\n" +
                "  queries for:\n" +
                "    status = 'Deprecated'\n" +
                "  must return: DocE\n" +
                "\n" +
                "eventcode:\n" +
                "  queries for:\n" +
                "    eventCodeList = 'Colonoscopy'\n" +
                "    status = 'Approved'\n" +
                "  must return: DocB\n" +
                "\n" +
                "eventcode_scheme:\n" +
                "  queries for:\n" +
                "    eventCodeList = 'Colonoscopy'\n" +
                "    eventCodeList = 'Connect-a-thon eventCodeList'\n" +
                "    status = 'Approved'\n" +
                "  must return: DocB\n" +
                "\n" +
                "formatcode:\n" +
                "  queries for:\n" +
                "    formatCode = 'CDAR2/IHE 1.0'\n" +
                "    status = 'Approved'\n" +
                "  must return: DocA, DocB, DocF, DocC\n" +
                "\n" +
                "hcftc:\n" +
                "  queries for:\n" +
                "    healthcareFacilty: Outpatient\n" +
                "    status: 'Approved'\n" +
                "  must return: DocA, DocF, DocD\n" +
                "\n" +
                "hcftc_scheme:\n" +
                "  queries for:\n" +
                "    healthcareFacilty: Outpatient\n" +
                "    healthcareFaciltyScheme: 'Connect-a-thon healthcareFacilityTypeCodes'\n" +
                "    status: 'Approved'\n" +
                "  must return: DocF, DocD\n" +
                "\n" +
                "leafclass: \n" +
                "   queries for:\n" +
                "      all approved documents, DocE is deprecated so not returned\n" +
                "   returns LeafClass   \n" +
                "   must return: DocA, DocB, DocC, DocD, DocF\n" +
                "\n" +
                "no_matching_classcode:\n" +
                "   queries for class code not contained in test data\n" +
                "   must return: none\n" +
                "\n" +
                "object_refs: \n" +
                "   queries for:\n" +
                "      all approved documents, DocE is deprecated so not returned\n" +
                "   returns ObjectRefs   \n" +
                "   must return: DocA, DocB, DocC, DocD, DocF\n" +
                "\n" +
                "old_scheme:\n" +
                "   query using old format.  must return error since code not in CE format\n" +
                "\n" +
                "practicesetting:\n" +
                "  queries for:\n" +
                "    practiceSettingCode = 'Dialysis'\n" +
                "    status = 'Approved'\n" +
                "  must return: DocA, DocF, DocD\n" +
                "\n" +
                "practicesetting_scheme:\n" +
                "  queries for:\n" +
                "    practiceSettingCode = 'Dialysis'\n" +
                "    practiceSettingCodeScheme = 'Connect-a-thon practiceSettingCodes'\n" +
                "    status = 'Approved'\n" +
                "  must return: DocA, DocD\n" +
                "\n" +
                "  NOTE: there is a parameter $XDSDocumentEntryPracticeSettingCodeScheme\n" +
                "  in this stored query.  This is not a mistake.   Extra parameters\n" +
                "  must be accepted and ignored by the stored query request parser.\n" +
                "  This parameter is no longer valid given the Code Type parameter\n" +
                "  format change but the parameter must still be accepted (and ignored)\n" +
                "  because of this ebRS rule.\n" +
                "\n" +
                "practicesetting_two:\n" +
                "  contains duplicate $XDSDocumentEntryPracticeSettingCode\n" +
                "  must return: error\n" +
                "\n" +
                "servicestarttime:\n" +
                "  queries for:\n" +
                "    serviceStartTimeFrom: 2005\n" +
                "    serviceStartTimeTo: 2006\n" +
                "    status = 'Approved' \n" +
                "  must return: DocC, DocD\n" +
                "\n" +
                "servicestoptime:\n" +
                "  queries for:\n" +
                "    serviceStopTimeFrom: 2005\n" +
                "    serviceStopTimeTo: 2006\n" +
                "    status = 'Approved' \n" +
                "  must return: DocC, DocD\n");

        collectionsReadme.put("11898", "SQ.b FindSubmissionSets Stored Query\n" +
                "\n" +
                "Stored Query must use SOAP version 1.2 \n" +
                "\n" +
                "This test contains many many test steps each validating one \n" +
                "aspect of the FindSubmissionSets stored query.  \n" +
                "Relies on test data loaded by test 12346\n" +
                "\n" +
                "\n" +
                "The test steps are:\n" +
                "\n" +
                "simple\n" +
                "\tBasic query using patient ID and status\n" +
                "\n" +
                "other_sourceid\n" +
                "\tAdds selection on non-existant sourceId\n" +
                "\n" +
                "submissiontime_in\n" +
                "\tIncludes all 5 in submission time range\n" +
                "\n" +
                "submissiontime_out\n" +
                "\tIncludes none in submission time range\n" +
                "\n" +
                "submissiontime_no_start\n" +
                "\tNo start time specified - includes all 5\n" +
                "\n" +
                "submissiontime_no_end\n" +
                "\tNo end time - includes all 5\n" +
                "\n" +
                "author_all\n" +
                "\tSelect all 5 based on same author\n" +
                "\n" +
                "author_none\n" +
                "\tSelect none based on different author\n" +
                "\t\n" +
                "contenttype_all\n" +
                "\tSelect all 5 based on same content type\n" +
                "\n" +
                "contenttype_none\n" +
                "\tSelect none based on different content type\n");

        collectionsReadme.put("11899", "SQ.b FindFolders Stored Query\n" +
                "\n" +
                "This test relies on test 12346 to pre-load test data.\n" +
                "\n" +
                "basic\n" +
                "\tBasic operation based on Patient ID parameter\n" +
                "\n" +
                "in_updatetime\n" +
                "\tFilter with update time\n" +
                "\n" +
                "out_updatetime\n" +
                "\tFilter with update time\n");

        collectionsReadme.put("11901", "SQ.b GetDocuments Stored Query\n" +
                "\n" +
                "This test relies on test 12346 to pre-load test data.\n" +
                "\n" +
                "Stored Query must use SOAP version 1.2 \n" +
                "\n" +
                "Test Steps\n" +
                "\n" +
                "uniqueid\n" +
                "\tOperation with UniqueId\n" +
                "\n" +
                "uniqueid2\n" +
                "\tOperation with multiple UniqueIds\n" +
                "\n" +
                "uuid\n" +
                "\tOperation with UUID\n" +
                "\n" +
                "uuid2\n" +
                "\tOperation with multiple UUIDs\n" +
                "\n" +
                "uuid_multiple_slot_values\n" +
                "\tSQ parameter values spread across multiple Value transactions\n" +
                "of a Slot.  (CP 295)\n" +
                "\n" +
                "homeCommunityId\n" +
                "\tSection 3.18.4.1.3, part 4, For Document Registry Actors says\n" +
                "\tthat the Document Registry must accept the homeCommunityId\n" +
                "\ton a Stored Query. This test repeats step uniqueid above\n" +
                "\tbut with the homeCommunityId present.\n" );

        collectionsReadme.put("11902", "SQ.b GetFolders Stored Query\n" +
                "\n" +
                "This test validates the operation of the GetFolders stored query.  It\n" +
                "relies on test 12346 to pre-load test data into the Registry.\n" );

        collectionsReadme.put("11903", "SQ.b GetAssociations Stored Query\n" +
                "\n" +
                "Relies on test data loaded by test 12346\n" +
                "\n" +
                "Test Steps\n" +
                "\n" +
                "single_from_doc\n" +
                "\tSingle association referencing document\n" +
                "\n" +
                "multiple_assoc_from_doc\n" +
                "\tmultiple associations referencing document\n" +
                "\n" +
                "multiple_assoc_from_ss\n" +
                "\tmultiple associations referencing ss\n" +
                "\n" +
                "multiple_doc\n" +
                "\tInput references multiple documents\n" );

        collectionsReadme.put("11904", "SQ.b GetDocumentsAndAssociations Stored Query\n" +
                "\n" +
                "Test 12346 must be run first to load test data into the Registry.\n" +
                "\n" +
                "Test Steps\n" +
                "\n" +
                "uniqueid\n" +
                "\tQuery with uniqueId.  DocumentEntry targeted was submitted alone in submissionset so\n" +
                "\tonly the DocumentEntry and a single Association should be returned.\n" +
                "\n" +
                "uniqueids\n" +
                "\tQuery with two UniqueIds.  One of the targeted DocumentEntries is the same as\n" +
                "\tabove.  The second is a member of a Folder. Two DocumentEntries are expected\n" +
                "\tback and 3 Associations: SubmissionSet to DocumentEntry association for each\n" +
                "\tDocumentEntry AND a single Folder to DocumentEntry association for the second\n" +
                "\tDocumentEntry.\n" +
                "\n" +
                "uuid\n" +
                "\tOperation with UUID. This is a repeat of step uniqueid above but starting with the\n" +
                "\tUUID of the DocumentEntry instead of the uniqueId.\n" +
                "\n" +
                "uuids\n" +
                "\tOperation with two UUIDs.  This is a repeat of step uniqueids above but starting\n" +
                "\twith the UUIDs of the DocumentEntries instead of the uniqueIds.\n" );

        collectionsReadme.put("11905", "SQ.b GetSubmissionSets Stored Query\n" +
                "\n" +
                "Relies on test data loaded by test 12346\n" +
                "\n" +
                "Test Steps\n" +
                "\n" +
                "doc_uuid\n" +
                "\tDocument UUID as parameter\n" +
                "\n" +
                "two_doc_uuid_two_ss\n" +
                "\tTwo Document UUIDs as parameters, each in different submission set\n" +
                "\n" +
                "two_doc_uuid_one_ss\n" +
                "\tTwo Document UUIDs as parameters, both from same submission set\n" +
                "\n" +
                "fol_uuid\n" +
                "\tFolder UUID as parameter\n" +
                "\n" +
                "doc_and_fol\n" +
                "\tTwo UUIDS, document and folder from different submission sets\n" );
        collectionsReadme.put("11906", "SQ.b GetSubmissionSetAndContents Stored Query\n" +
                "\n" +
                "Stored Query must use SOAP version 1.2 \n" +
                "\n" +
                "Test 12346 must be run first to load test data needed\n" +
                "for this test.\n" +
                "\n" +
                "uniqueid\n" +
                "\tQuery based on uniqueId\n" +
                "\n" +
                "folder_and_docs\n" +
                "\tQuery based on uniqueId. Submission Set includes multiple documents and a folder.\n" +
                "\n" +
                "uuid\n" +
                "\tQuery based on uuid\n" +
                "\n" +
                "format_code\n" +
                "\tFilter the documents via Format code\n" +
                "\n" +
                "conf_code\n" +
                "\tFilter the documents via Confidentialitiy code\n" +
                "\t\n" +
                "objectrefs\n" +
                "\tRequest ObjectRefs instead of LeafClass. Should return 3 ObjectRefs.\n" +
                "\n" +
                "In the folder_and_docs, format_code,  and conf_code sections, one \n" +
                "of two DocumentEntries are returned. So, the SQ results include the SS, the folder, one DocumentEntry, and Associations between:\n" +
                "\tSS and Folder\n" +
                "\tSS and DocumentEntry\n" +
                "\tFolder and DocumentEntry\n" );

        collectionsReadme.put("11907", "SQ.b GetFolderAndContents Stored Query\n" +
                "\n" +
                "Relies on test data loaded by test 12346\n" +
                "\n" +
                "Test Steps\n" +
                "\n" +
                "uniqueid\n" +
                "\tOperation with UniqueId\n" +
                "\n" +
                "uuid\n" +
                "\tOperation with UUID\n" +
                "\n" +
                "conf_code\n" +
                "\tOperation with UUID and confidentialityCode\n" +
                "\n" +
                "both_conf_code\n" +
                "\tOperation with UUID and confidentialityCode\n" +
                "\n" +
                "format_code\n" +
                "\tOperation with UUID and formatCode\n" );

        collectionsReadme.put("11908", "SQ.b GetFoldersForDocument Stored Query\n" );

        collectionsReadme.put("11909", "SQ.b GetRelatedDocuments Stored Query\n" +
                "\n" +
                "Stored Query must use SOAP version 1.2\n" +
                "\n" +
                "Relies on test data loaded by test 12346\n" +
                "\n" +
                "\n" +
                "Test Steps\n" +
                "\n" +
                "no_initial_doc\n" +
                "\tOriginal document does not exist. Nothing returned.\n" +
                "\n" +
                "uniqueid_no_related\n" +
                "\tOperation with single UniqueId as input but no related documents\n" +
                "\n" +
                "uuid_no_related\n" +
                "\tOperation with single UUID as input but no related documents\n" +
                "\n" +
                "uniqueid\n" +
                "\tOperation with single UniqueId as input.\n" +
                "\n" +
                "near_folder\n" +
                "\tOperation near a folder.  Folders and Submission Sets are not returnd.\n" +
                "\n" +
                "uuid\n" +
                "\tOperation with UUID input\n" +
                "\n" );

        collectionsReadme.put("11966", "PnR.b Accept document\n" +
                "\n" +
                "submit - Generate a Provide and Register.b transaction to the Repository\n" +
                "under test which is already configured to forward Register.b\n" +
                "transactions to the Public Registry.\n" +
                "\n" +
                "eval - The Public Registry is queried to validate the metadata forwarded.\n" +
                "When using xdstest to run this step, make sure it sends to the Public\n" +
                "Registry.  This can be done using the -s pub command line option to\n" +
                "xdstest." );

        collectionsReadme.put("11979", "PnR.b Accept two documents\n" +
                "\n" +
                "submit - Generate a Provide and Register.b transaction to the Repository\n" +
                "under test which is already configured to forward Register.b\n" +
                "transactions to the Public Registry.\n" +
                "\n" +
                "eval - The Public Registry is queried to validate the metadata forwarded.\n" +
                "When using xdstest to run this step, make sure it sends to the Public\n" +
                "Registry.  This can be done using the -s pub command line option to\n" +
                "xdstest.\n" );

        collectionsReadme.put("11983", "PnR.b Reject submissions where metadata and documents do not match\n" +
                "\n" +
                "The submit and eval directories hold test steps with the same names, \n" +
                "shown below.  The submit tests test that the proper error is returned.  \n" +
                "The eval tests test that no contents are present in the Registry.\n" +
                "\n" +
                "*_no_metadata\n" +
                "\tA document is present as attachment but no XDSDocuementEntry is \n" +
                "present in metadata. The error XDSMissingDocumentMetadata must be returned.\n" +
                "\n" +
                "*_no_doc\n" +
                "\tA XDSDocumentEntry is present in metadata but no corresponding \n" +
                "document is attached. The error XDSMissingDocument must be returned.\n" );

        collectionsReadme.put("11986", "PnR.b Return Errors from Registry\n" +
                "\n" +
                "Send a Provide and Register.b transaction to Repository which then \n" +
                "forwards the metadata to the Public Registry.  The metadata contains\n" +
                "a known flaw for which the Public Registry will return a known\n" +
                "error code.  The test validates that the error code is returned\n" +
                "through the Repository back to the Document Source." );

        collectionsReadme.put("11990", "R.b Accept register one document\n" +
                "\n" +
                "submit - Generate Register.b transaction to your Registry\n" +
                "\n" +
                "eval - use SQ (GetSubmissionSetAndContents) to verify registry contents" );
        collectionsReadme.put("11991", "R.b Accept register two documents\n" +
                "\n" +
                "submit - Generate Register.b transaction to your Registry\n" +
                "\n" +
                "eval - use SQ to verify registry contents" );
        collectionsReadme.put("11992", "R.b Accept Document Replace\n" +
                "\n" +
                "submit - submit single document\n" +
                "\n" +
                "rplc - issue a document replace\n" +
                "\n" +
                "eval - Verify the new document exists, the old is deprecated and the\n" +
                "RPLC Association is in place." );
        collectionsReadme.put("11993", "R.b Accept Document Addendum\n" +
                "\n" +
                "submit\n" +
                "\tsubmit  - submit a document\n" +
                "\tsubmit_copy - submit a second copy of the document\n" +
                "\n" +
                "rplc\n" +
                "\trplc - replace document from step submit_copy\n" +
                "\n" +
                "apnd\n" +
                "\tapnd - add ammendment to document from step submit\n" +
                "\tapnd_rplc - add ammendment to document from step submit_copy - must fail\n" +
                "\n" +
                "eval\n" +
                "\tvalidate_original - verify document from step submit has status Approved (apnd didn't change it)\n" +
                "\tvalidate_apnd - verify document from step apnd is present and has status Approved\n" +
                "\tno_validate_rplc_apnd - verify metadata from step apnd_rplc was not stored\n"  );

        collectionsReadme.put("11994", "R.b Accept Document Transformation\n" +
                "\n" +
                "submit\n" +
                "\tsubmit  - submit a document\n" +
                "\tsubmit_copy - submit a second copy of the document\n" +
                "\n" +
                "rplc\n" +
                "\trplc - replace document from step submit_copy\n" +
                "\n" +
                "xfrm\n" +
                "\txfrm - add transformation to document from step submit\n" +
                "\txfrm_rplc - add transformation to document from step submit_copy - must fail\n" +
                "\n" +
                "eval\n" +
                "\tvalidate_original - verify document from step submit has status Approved (apnd didn't change it)\n" +
                "\tvalidate_xfrm - verify document from step apnd is present and has status Approved\n" +
                "\tno_validate_rplc_xfrm - verify metadata from step xfrm_rplc was not stored\n"  );
        collectionsReadme.put("11995", "R.b Accept document replace with transformation\n" +
                "\n" +
                "submit - Submit a document\n" +
                "\n" +
                "rplc - Submit a replacement document\n" +
                "\n" +
                "eval - Verify the original document is deprecated, the replacement \n" +
                "document exists, and the RPLC association is in place.\n"  );
        collectionsReadme.put("11996", "R.b Reject Submission of Invalid Patient ID\n" +
                "\n" +
                "Verify a Register.b transaction is rejected by your Registry given\n" +
                "that it carried an unknown Patient ID.  The correct error code\n" +
                "(XDSUnknownPatientId) must be returned." );
        collectionsReadme.put("11997", "R.b Reject Submission Set, Patient ID does not match Document\n" +
                "\n" +
                "submit - Send a Register.b transaction to your Registry where the \n" +
                "Patient ID on the Submission Set and Document do not match.  Verify the\n" +
                "correct error code (XDSPatientIdDoesNotMatch) is returned.\n" +
                "\n" +
                "eval - Verify submission did not update registry." );
        collectionsReadme.put("11998", "R.b Reject Submission, Patient ID on Replacement Document does not match Original\n" +
                "\n" +
                "submit - submit single document\n" +
                "\n" +
                "rplc - allocate new Patient ID and then issue a document replace.  Transaction must fail.\n" +
                "\n" +
                "eval - Verify the new DocumentEntry does not exist, the old still has status Approved" );
        collectionsReadme.put("11999", "R.b Accept Create Folder\n" +
                "\n" +
                "submit - Submit an empty folder\n" +
                "\n" +
                "eval - Use SQ (GetSubmissionSetAndContents) to verify registry has new folder" );

        collectionsReadme.put("12000", "R.b Accept Create Folder with Initial Document\n" +
                "\n" +
                "submit - Submit a folder containing a document\n" +
                "\n" +
                "eval - Use SQ to verify registry has new folder, new document, and that\n" +
                "the document is in the folder. Step eval_folder uses \n" +
                "GetSubmissionSetAndContents and step eval_assoc uses GetFolderAndContents \n" +
                "stored queries");
        collectionsReadme.put("12001", "R.b Add new document to existing folder\n" +
                "\n" +
                "create_folder - creates a folder to work in\n" +
                "\n" +
                "add_to_folder - adds document to folder\n" +
                "\n" +
                "eval - verify registry contents\n" );
        collectionsReadme.put("12002", "R.b Reject Add Document to Folder - Patient ID does not match\n" +
                "\n" +
                "create_folder - creates a folder to work in\n" +
                "\n" +
                "add_to_folder - attempt add document to folder with different Patient ID\n" +
                "\n" +
                "eval - verify registry contents\n" );
        collectionsReadme.put("12004", "R.b Document Resubmission\n" +
                "\n" +
                "Test Purpose\n" +
                "\n" +
                "The creation of a document and the submission of a document are different \n" +
                "events.  A document created within an EHR could be forwarded as reference \n" +
                "to multiple parties.  A lab report is an example.  Each party could then \n" +
                "submit this document as evidence to support an action taken.  Now, as a \n" +
                "second issue, this report is based on the CDA format and the Content \n" +
                "Profile requires that the XDSDocumentEntry.uniqueId be the value of \n" +
                "attribute x in the CDA.  Now, if both parties are truly submitting the same \n" +
                "document (hash matches) then this should not cause a conflict. They cannot \n" +
                "allocate a new uniqueId for one of the copies because the Content Profile \n" +
                "stipulates taking the value from the document header.  This test validates:\n" +
                "\n" +
                "1) That resubmission does not generate an error\n" +
                "2) That an apparent resubmission (same uniqueId) but different hash (different content) is rejected\n" +
                "3) Both copies are available in query\n" +
                "\n" +
                "submit\n" +
                "\tSubmit a Submission Set containing a single document\n" +
                "\n" +
                "resubmit_doc\n" +
                "\tResubmit same with document having same XDSDocumentEntry.uniqueId and\n" +
                "XDSDocumentEntry.hash.  The Register must be successful.\n" +
                "\n" +
                "resubmit_doc_diff_hash\n" +
                "\tResubmit again with document having a different value in \n" +
                "XDSDocumentEntry.hash but same value in XDSDocumentEntry.uniqueId.  This must \n" +
                "return errorCode XDSDuplicateUniqueIdInRegistry.\n");
        collectionsReadme.put("12021", "Ret.b Accept retrieve document set - two documents\n" +
                "\n" +
                "submit - Send a Provide and Register.b transaction to your Repository which\n" +
                "forwards the metadata to the Public Registry. Your Repository must\n" +
                "be properly configured (see wiki at\n" +
                "http://ihewiki.wustl.edu/wiki/index.php/XDS_Test_Kit_2007-2008_Test_Descriptions#12021).\n" +
                "\n" +
                "query - Use Stored Query to fetch the repositoryUniqueId and document\n" +
                "uniqueId for each document. Make sure to have xdstest configured to \n" +
                "query from the Public Registry.\n" +
                "\n" +
                "retrieve - Use the above parameters to generate a Retrieve Document\n" +
                "Set transaction for both documents.\n" );
        collectionsReadme.put("12028", "Ret.b Accept retrieve document set - single document with TLS\n" +
                "\n" +
                "submit - Issue a Provide and Registry.b transaction to load your\n" +
                "repository with a document.  Your repository must forward the metadata\n" +
                "to the Public Registry.\n" +
                "\n" +
                "query - Query the Public Registry to get the metadata as submitted\n" +
                "by the Repository.\n" +
                "\n" +
                "retrieve - Issue a Retrieve Document Set transaction to your repository\n" +
                "to get the document.  This must be done over TLS.\n");
        collectionsReadme.put("12029", "Ret.b Accept retrieve document set - single document\n" +
                "\n" +
                "Configure Repository to forward Register.b transactions to the Public\n" +
                "Registry\n" +
                "\n" +
                "submit - submit a single document to the Repository\n" +
                "\n" +
                "query - query the Public Registry for metadata\n" +
                "\n" +
                "retrieve - retrieve document\n");
        collectionsReadme.put("12084", "R.b Submission Stored - All or Nothing\n" +
                "\n" +
                "submit - Send a Register.b transaction including two documents.  The\n" +
                "metadata for one document is flawed (no repositoryUniqueId attribute).  \n" +
                "The transaction must fail.\n" +
                "\n" +
                "eval - use SQ to prove that nothing was stored.");
        collectionsReadme.put("12309", "XCQ FindDocuments for ObjectRef\n" +
                "\n" +
                "Depends on test 12318 (which targets a Document Repository) to initialize the test data.\n" +
                "\n" +
                "Assertions test the following:\n" +
                "\n" +
                "homeMatch1 - first ObjectRef has correct home attribute (value from xdstest2tool/mgmt/default.xml\n" +
                "\n" +
                "homeMatch2 - second ObjectRef has correct home attribute (value from xdstest2tool/mgmt/default.xml\n" +
                "\n" +
                "ObjectRefCount - Query returns 2 ObjectRefs\n" );
        collectionsReadme.put("12310", "XCQ FindDocuments for LeafClass\n" +
                "\n" +
                "This test depends on test 12318 to initialize test data.\n" +
                "\n" +
                "There is a single test step.\n" +
                "\n" +
                "LeafClass indicates that the entire XML description of the document of interest will be returned.\n" +
                "\n" +
                "Assertions test the following conditions:\n" +
                "\n" +
                "ExtrinsicObjectCount - 2 ExtrinsicObject returned\n" +
                "\n" +
                "ExtrinsicObjectsApproved - both ExtrinsicObject have status of Approved\n" +
                "\n" +
                "homeMatch1 - home attribute on first ExtrinsicObject matches configuration\n" +
                " in xdstest2tool/mgmt/default.xml\n" +
                "\n" +
                "homeMatch2 - home attribute on second ExtrinsicObject matches \n" +
                "configuration in xdstest2tool/mgmt/default.xml\n" +
                "\n" +
                "Implicit Assertions:\n" +
                "\n" +
                "Metadata validates against Schema\n" +
                "\n" +
                "Metadata validates against XDS Metadata Validator\n" );
        collectionsReadme.put("12311", "XCQ GetDocuments Cross Community Query\n" +
                "\n" +
                "This test is dependent on test 12309 which does a FindDocuments query returning ObjectRefs.  \n" +
                "This test takes those ObjectRefs and retrieves (really query) the full metadata from the \n" +
                "Receiving Gateway. This approach to the use of metadata is important in case the original \n" +
                "FindDocuments query returned a large quantity of ObjectRefs then the GetDocuments query \n" +
                "would be used to retreive the full metadata a bunch at a time. \n" +
                "\n" +
                "BTW, test 12309 is dependent on test 12318 to load test data.\n" +
                "\n" +
                "\n" +
                "There are several test steps:\n" +
                "\n" +
                "uuid1 - issues a GetDocuments XGQ for a single document based on its UUID\n" +
                "\n" +
                "uuid2 - same query but asking for 2 documents\n" +
                "\n" +
                "Each test step references the log.xml file of test 12309 \n" +
                "(FindDocuments XGQ for ObjectRefs) to get the starting ObjectRefs.\n" +
                "\n" +
                "Each test uses the following assertions:\n" +
                "\n" +
                "ExtrinsicObjectCount - Correct # of ExtrinsicObjects returned\n" +
                "\n" +
                "ExtrinsicObjectsApproved - both ExtrinsicObject have status of Approved\n" +
                "\n" +
                "homeMatch* - home attribute on ExtrinsicObject matches configuration\n" +
                " in xdstest2tool/mgmt/default.xml\n" +
                "\n" +
                "\n" +
                "Implicit Assertions:\n" +
                "\n" +
                "Metadata validates against Schema\n" +
                "\n" +
                "Metadata validates against XDS Metadata Validator\n" );
        collectionsReadme.put("12312", "XCR Retrieve single document\n" +
                "\n" +
                "This test has two parts:\n" +
                "\n" +
                "==============================================\n" +
                "correct - show correct operation\n" +
                "\n" +
                "Generate request for the retrieval of a single document based \n" +
                "on metadata returned in test 12311.\n" +
                "\n" +
                "Based on the XDSDocument.uniqueId, repositoryUniqueId, and homeCommunityId returned \n" +
                "in test 12311, issue a RetrieveDocumentSet transaction to retrieve a document.  \n" +
                "\n" +
                "When successful, perform the following validations:\n" +
                "\n" +
                "* Single document returned\n" +
                "\n" +
                "* RepositoryUniqueId matches request\n" +
                "\n" +
                "* DocumentUniqueId matches request\n" +
                "\n" +
                "* MimeType matches metadata from test 12311\n" +
                "\n" +
                "* Document hash, as calculated after the retrieve, matches value in metadata from test 12311\n" +
                "\n" +
                "* HomeCommunityId matches configuration\n" +
                "\n" +
                "\n" +
                "==============================================\n" +
                "missing_home - show proper error (XDSMissingHomeCommunityId) returned when \n" +
                "homeCommunityId not in request\n" );
        collectionsReadme.put("12313", "XCR Retrieve multiple document\n" +
                "\n" +
                "Generate request for the retrieval of two documents based on metadata returned in test 12311.\n" +
                "\n" +
                "Based on the XDSDocument.uniqueId, repositoryUniqueId, and homeCommunityId returned in test 12311, \n" +
                "issue a RetrieveDocumentSet transaction to retrieve two documents.  \n" );
        collectionsReadme.put("12314", "XCQ FindDocuments for ObjectRef over TLS\n" +
                "\n" +
                "The following is done over a TLS based channel.\n" +
                "\n" +
                "Depends on test 12318 to initialize the test data.\n" +
                "\n" +
                "Assertions test the following:\n" +
                "\n" +
                "homeMatch1 - first ObjectRef has correct home attribute (value from xdstest2tool/mgmt/default.xml\n" +
                "\n" +
                "homeMatch2 - second ObjectRef has correct home attribute (value from xdstest2tool/mgmt/default.xml\n" +
                "\n" +
                "ObjectRefCount - Query returns 2 ObjectRefs\n" );
        collectionsReadme.put("12323", "R.b Folder lastUpdateTime\n" +
                "\n" +
                "This test verifies that the registry properly sets the XDSFolder.lastUpdateTime\n" +
                "attribute.\n" +
                "\n" +
                "Steps\n" +
                "\n" +
                "no_time - submit folder with no lastUpdateTime attribute\n" +
                "\n" +
                "has_time - submit folder with lastUpdateTime attribute (old date)\n" +
                "\n" +
                "verify_no_submission_time - use GetFolder stored query to retrieve\n" +
                "folder from no_time step.  Verify the XDSFolder.lastUpdateTime\n" +
                "attribute shows today's date.\n" +
                "\n" +
                "verify_has_submission_time - use GetFolder stored query to retrieve\n" +
                "folder from has_time step.  Verify the XDSFolder.lastUpdateTime\n" +
                "attribute shows today's date.\n" +
                "\n" +
                "add_to_folder - submit a new DocumentEntry adding it to the folder\n" +
                "submitted in step has_time\n" +
                "\n" +
                "verify_time_updated - use GetFolder stored query to verify that \n" +
                "XDSFolder.lastUpdateTime was updated by the add_to_folder step.  Your\n" +
                "ability to pass this step depends on how you test and the precision \n" +
                "of XDSFolder.lastUpdateTime in your implementation.  The XD* profiles do not specify the time precision. If you only support time precision \n" +
                "to the minute and you run step add_to_folder a few seconds after step \n" +
                "has_time then XDSFolder.lastUpdateTime will not show any change.  You will \n" +
                "have to wait a minute or two before running this step to show that your \n" +
                "Registry manages this attribute correctly.  If you support a 1 second \n" +
                "granularity or better then the automatic running of this test will probably \n" +
                "work fine for you.\n" +
                "\n" +
                "rplc - replace document submitted in step add_to_folder.  This must update\n" +
                "the lastUpdateTime on the folder\n" +
                "\n" +
                "verify_time_updated_by_rplc - verify the rplc step did update\n" +
                "lastUpdateTime on the folder\n" );
        collectionsReadme.put("12326", "R.b Add Existing document to existing folder\n" +
                "\n" +
                "This test validates a Registry's ability to perform a basic Folder\n" +
                "operation, adding a Document already in Registry to a Folder also \n" +
                "already in Registry.\n" +
                "\n" +
                "create_folder - creates a folder and a document to work with\n" +
                "\n" +
                "add_to_folder - adds document to folder\n" +
                "\n" +
                "eval - verify registry contents\n" +
                "\n" +
                "\n" +
                "FAQ regarding the add_to_folder section:\n" +
                "\n" +
                "Q: I have a little problem with the XDS Registry test 12326. \n" +
                "An existing document shall be added to an existing folder via a new submission set. \n" +
                "In the technical framework it is described that when an existing document is referenced \n" +
                "via a new submission set there must be an HasMember Association with the Association Label \n" +
                "\"ByReference\" between the submission set and the document entry. But in this test only the \n" +
                "association between folder and document and the association between submission set and \n" +
                "the folder/document association is sent. \n" +
                "\n" +
                "Is this correct or is the test wrong?\n" +
                "\n" +
                "A: In test 12326, the create_folder section submits:\n" +
                "\n" +
                "   A SubmissionSet\n" +
                "   A DocumentEntry linked to the SubmissionSet via a HasMember Association with \n" +
                "   SubmissionSetStatus of Original (new submission)\n" +
                "   A Folder linked to the SubmissionSet via a HasMember Association\n" +
                "\n" +
                "So, at the end of this submission the Registry contains a new DocumentEntry and \n" +
                "a new Folder.  There is no linkage between them (other than that they were submitted \n" +
                "in the same SubmissionSet). The DocumentEntry and the Folder could have been \n" +
                "submitted in separate submissions and this test would be the same.\n" +
                "\n" +
                "In the second submission, section add_to_folder, the DocumentEntry is added \n" +
                "to the Folder. So, the submission contains:\n" +
                "\n" +
                "   The Folder to DocumentEntry HasMember Association (declaring the \n" +
                "         DocumentEntry to be a member of the Folder)\n" +
                "   The HasMember Association linking the SubmissionSet to the above \n" +
                "         Association.  This documents which submission added the \n" +
                "         DocumentEntry to the Folder (who is responsible for this \n" +
                "         DocumentEntry being in the is folder).\n" +
                "\n" +
                "The SubmissionSetStatus attribute is only used when linking a \n" +
                "DocumentEntry to a SubmissionSet.  No DocumentEntries were submitted \n" +
                "or referenced by this submission.\n" +
                "\n" +
                "So, going back to your original question, the ByReference label \n" +
                "is used when adding a DocumentEntry to an existing SubmissionSet. \n" +
                "This is occasionally done because documents of different Patient \n" +
                "IDs need to be linked.  Mother and child during child birth is the \n" +
                "typical example here.  The only way to link documents of different \n" +
                "Patient IDs is through the ByReference label to a SubmissionSet. This \n" +
                "is not the focus of this test.\n");
        collectionsReadme.put("12327", "R.b Accept Document Replace, Document in Folder\n" +
                "\n" +
                "A Document Registry, when accepting a Document Replace, must findSimple all\n" +
                "Folders the original document is a member of and add the replacement\n" +
                "document to those folders.\n" +
                "\n" +
                "Steps are:\n" +
                "\n" +
                "submit_doc_w_fol - submit a document in a folder\n" +
                "\n" +
                "verify_submission -  Use GetSubmissionSetAndContents SQ to verify the\n" +
                "submission\n" +
                "\n" +
                "rplc - replace the document from step submit_doc_w_fol\n" +
                "\n" +
                "verify_folder_contents - verify both the original and the replacement\n" +
                "documents are members of the folder\n");
        collectionsReadme.put("12364", "MPQ - FindDocumentsForMultiplePatients Strored Query\n" +
                "\n" +
                "This test relies on test 12361 to load the necessary test data.\n" +
                "\n" +
                "Each sub-directory holds a testplan which tests a different\n" +
                "aspect of FindDocumentsForMultiplePatients Stored Query.\n" +
                "\n" +
                "With multiple patient ids the following patterns\n" +
                "\n" +
                "nothing - no codes or patient id is specified - must fail\n" +
                "classcode - classcode is specified but no Patient ID\n" +
                "eventcode - eventcode is specified but no Patient ID\n" +
                "hcftc - healthcareFacilityTypeCode is specified but no Patient ID\n" +
                "classcode_eventcode - select on both \n" +
                "one_pid - include single Patient ID\n" +
                "multi_pid - include multiple Patient IDs\n" );
        collectionsReadme.put("12368", "SQ.b XDSResultNotSinglePatient Error\n" +
                "\n" +
                "The Stored Query transaction is restricted from returning full metadata (LeafClass) for more than one Patient ID.  When ObjectRefs are \n" +
                "requested, there is no restriction since PII is not being disclosed.\n" +
                "\n" +
                "Each subtest deals with a combination of an object type \n" +
                "(DocumentEntry, SubmissionSet, Folder) matched up with a return \n" +
                "type (LeafClass, ObjectRef) to ensure the proper response is \n" +
                "generated.\n" +
                "\n" +
                "This test is dependent on both 12346 and 12374 since they load \n" +
                "metadata into different Patient IDs.\n");
        collectionsReadme.put("12369", "12369 PnR.b Wrong size or hash received in metadata\n" +
                "\n" +
                "Sub tests\n" +
                "\n" +
                "size\n" +
                "\tMetadata contains size attribute that does not match attached document\n" +
                "\n" +
                "hash\n" +
                "\tMetadata contains hash attribute that does not match attached document\n" +
                "\n" +
                "In both cases, XDSRepositoryMetadataError must be returned.\n" +
                "\n" +
                " \n");
        collectionsReadme.put("12370", "12370 R.b Accept association documenation classification\n" +
                "\n" +
                "Lifecycle management type associations may contain an optional classification labeling the\n" +
                "reason for the update.  This association may be present on RPLC, APND, XFRM etc. assocations.\n" +
                "\n" +
                "Sub Tests\n" +
                "\n" +
                "not_configured - attempt to add this association on a HasMember assocation - fails\n" +
                "\n" +
                "submit - submit a document\n" +
                "\n" +
                "rplc - replace the document and the RPLC association has the documentation classification\n" +
                "\n" +
                "query - validate the documentation classification is returned in a stored query \n" );
        collectionsReadme.put("12371", "XDR Provide and Register.b \n" +
                "\n" +
                "This Provide and Register contains a single DocumentEntry with the\n" +
                "necessary Submission Set and HasMember Association.  The Submission\n" +
                "Set includes the optional intendedRecipient attribute that is \n" +
                "used with XDR.\n");
        collectionsReadme.put("12372", "XDR Provide and Register.b \n" +
                "\n" +
                "This Provide and Register contains a single DocumentEntry with the\n" +
                "necessary Submission Set and HasMember Association.  The Submission\n" +
                "Set includes the optional intendedRecipient attribute that is \n" +
                "used with XDR.\n" +
                "\n" +
                "This test is identical to 12371 but must use TLS.\n"  );
        collectionsReadme.put("12373", "XDR Provide and Register.b with multiple documents\n" +
                "\n" +
                "This Provide and Register contains two DocumentEntry objects with the\n" +
                "necessary Submission Set and HasMember Association.  The Submission\n" +
                "Set includes the optional intendedRecipient attribute that is \n" +
                "used with XDR.\n"  );
        collectionsReadme.put("12375", "XDR Provide and Register.b with Limited Metadata\n" +
                "\n" +
                "This Provide and Register contains a single DocumentEntry with the\n" +
                "necessary Submission Set and HasMember Association. The metadata \n" +
                "provided conforms to the requirements\n" +
                "of the XDR Metadata Limited Document Source.  Only the \n" +
                "absolutely required attributes are included.\n" +
                "\n" +
                "In this metadata, both the SubmissionSet and DocumentEntry\n" +
                "conform to the Limited Metadata specification.  The\n" +
                "profile does not require this of all objects in a submission.\n"  );
        collectionsReadme.put("12376", "XDR Provide and Register.b with Limited Metadata but without Limited Metadata label\n" +
                "\n" +
                "This Provide and Register contains a single DocumentEntry with the\n" +
                "necessary Submission Set and HasMember Association. The metadata \n" +
                "provided conforms to the requirements\n" +
                "of the XDR Metadata Limited Document Source.  Only the \n" +
                "absolutely required attributes are included.  \n" +
                "\n" +
                "But, the SubmissionSet and DocumentEntry are missing the Classification\n" +
                "(label) that indicates Limited Metadata is provided.  As a result\n" +
                "the Document Recipient expects to findSimple full metadata.\n" +
                "\n" +
                "In this metadata, both the SubmissionSet and DocumentEntry\n" +
                "conform to the Limited Metadata specification.  The\n" +
                "profile does not require this of all objects in a submission.\n"  );
        collectionsReadme.put("12379", "Extra Metadata\n" +
                "\n" +
                "Section ITI-TF 4.1.14 defines the composition and handling of \n" +
                "Extra Metadata, ebRIM Slots not defined in XD* metadata.  This \n" +
                "test tests the required handling by the XDS Document Registry actor.\n" +
                "\n" +
                "There are two test sections defined: support and no_support. The\n" +
                "XDS Document Registry actor is required to implement Extra\n" +
                "Metadata by either \n" +
                "\n" +
                "\tAccepting, not storing and returning the XdsExtraMetadataNotSaved\n" +
                "\twarning code\n" +
                "\t\n" +
                "\tAccepting, storing and returning the extra metadata in query\n" +
                "\tresponses\n" +
                "\t\n" +
                "The two sections align with these two approaches the Registy actor\n" +
                "can support.  Only one of the will succeed (they are mutually\n" +
                "exclusive). When logging results in Gazelle, upload the results\n" +
                "for both sections. " );
        collectionsReadme.put("15500", "XCPD Find Patient Messages\n" +
                "\n" +
                "Tests the ability of a responding gateway to respond to a findSimple patient request (305). This tests the response message (306).\n" +
                "\n" +
                "The test is composed of three steps stored in three different XML files:\n" +
                "- 1. test1_findpatient_dob_gender_name\n" +
                "\tAttempts to locate a patient using DOB, gender and name. Expected status: success.\n" +
                "- 2. test2_findpatient_gender_name_failure\n" +
                "\tAttempts to locate a patient by name only, which means DOB is voluntarily missing. Expected status: failure.\n" +
                "- 3. test3_findpatient_dob_gender_name_failure\n" +
                "\tAttempts to locate a patient using (wrong) gender and name. Expected status: failure.\n" +
                "\t\n" +
                "\n" +
                "I will put in more tests later in order to test more parameters: PatientID and address.\n" +
                "\n" +
                "\n" +
                "\n" +
                "--- Data ---\n" +
                "Here is the data we use for this series of tests. You will probably need to enter it manually in your system before we can arrange for something more permanent.\n" +
                "\n" +
                "PatientID\n" +
                "                assigningAuthorityName: domain1\n" +
                "                ext: 100\n" +
                "                root: 1.2.3.4.5.1000\n" +
                "Name\n" +
                "                Given: Chip\n" +
                "                Family: Moore\n" +
                "\n" +
                "Gender: M\n" +
                "DOB: 19849711\n" +
                "Address:\n" +
                "                City: Montreal\n" +
                "\n" +
                "\n" +
                "\n" +
                "--- Addressed issues ---\n" +
                "- The issue dated of ~December 15th, 2011 regarding missing DOBs in some of the tests has been fixed.\n" +
                "\n" +
                "\n" +
                "\n" +
                "--- Contact ---\n" +
                "diane.azais@nist.gov\n" );
        collectionsReadme.put("15800", "MU - Simple DocumentEntry Update\n" +
                "\n" +
                "Tests ability of Document Registry to accept a metadata update which changes a few simple\n" +
                "attributes of a DocumentEntry. This exercises the basic operation\n" +
                "of the Update DocumentEntry Metadata operaton as defined in section\n" +
                "3.57.4.1.3.3.1 of the Metadata Update Supplement\n" +
                "\n" +
                "Sections:\n" +
                "\n" +
                "Section: original - submit an original DocumentEntry, this is a first version.\n" +
                "\n" +
                "Section: update - submit an update to the DocumentEntry, changing the creationTime\n" +
                "\n" +
                "There are two documented \"triggers\" for this operation\n" +
                "1) The id attribute of the original is installed as the lid\n" +
                "attribute on the updated DocumentEntry.  This must be UUID format.\n" +
                "2) The SubmissionSet to DocumentEntry Association has a PreviousVersion\n" +
                "slot with a single value, 1, the version number of the DocumentEntry\n" +
                "being replaced.\n" +
                "\n" +
                "Normally this would be discovered through query. But for testing\n" +
                "we simplify.\n" +
                "\n" +
                "Formatting the updated DocumentEntry goes as follows:\n" +
                "- new id\n" +
                "- logicalId is id of original\n" +
                "- objectType must match original\n" +
                "- uniqueId must match original\n" +
                "- some attributes different (this is the point of the update)\n" +
                "\n" +
                "Section: query_by_uniqueid - has two steps, uniqueid_query which\n" +
                "executes the GetDocuments query to fetch\n" +
                "both versions of the DocumentEntry.  This is done with $MetadataLevel not\n" +
                "present in the query, a non-Metadata Update Document Consumer.\n" +
                "A non-MetadataUpdate Document Consumer may not understand the relationship\n" +
                "between the two entries returned, but it will get two entries.  \n" +
                "\n" +
                "This must be run after sections original and update\n" +
                "\n" +
                "Section: query_by_uniqueid (step validate) - this runs a series of \n" +
                "validations against the two DocumentEntries returned:\n" +
                "\n" +
                "same_logicalId - both have same logicalId\n" +
                "\n" +
                "different_id - entryUUID attribute on each is different\n" +
                "\n" +
                "orig_is_version_1 - the original version has version = 1\n" +
                "\n" +
                "update_is_version_2 - the updated version has version = 2\n" +
                "\n" +
                "orig_id_and_lid_same - on original DocumentEntry, the id and lid attributes \n" +
                "have the same value\n" +
                "\n" +
                "update_id_and_lid_different - on the update DocumentEntry, the id and lid \n" +
                "attributes have different values\n" +
                "\n" +
                "uniqueid_same - the uniqueId attribute on both DocumentEntries is the same\n" +
                "\n" +
                "original_is_deprecated - the initial version of the DocumentEntry must be\n" +
                "deprecated by the update\n" +
                "\n" +
                "update_is_approved - the update of the DocumentEntry must have availabilityStatus\n" +
                "of Approved\n" +
                "\n" +
                "Section: no_orig_docentry - an update is submitted but no original DocumentEntry\n" +
                "exists. The logicalId is hard coded to a unique value. This must fail.\n" +
                "\n" +
                "\n" +
                "Section: initial_version - submit a first version of a DocumentEntry via\n" +
                "update.  This is illegal, you cannot use metadata update to submit the first\n" +
                "version of a document. (ITI TF-2b:3.57.4.1.3.1 Rule #2)" );
        collectionsReadme.put("15802", "MU - Update DocumentEntry Status\n" +
                "\n" +
                "The primary reference for this test is section ITI TF-2b:3.57.4.1.3.3.2\n" +
                "Update DocumentEntry AvailabilityStatus.\n" +
                "\n" +
                "Sections:\n" +
                "\n" +
                "Section: load_docentry_1 - Submit a single DocumentEntry.  Of course it \n" +
                "will be assigned availabilityStatus of Approved.\n" +
                "\n" +
                "Section: deprecate_docentry_1 - Submit an update to deprecate docentry_1. \n" +
                "This will consist of a SubmissionSet and a UpdateAvailabilityStatus\n" +
                "Association referencing docentry_1 \n" +
                "\n" +
                "Section: confirm_docentry_1_deprecated - use GetDocuments Stored Query to \n" +
                "retrieve metadata for docentry_1 and verify it has availabilityStatus\n" +
                "of Deprecated.\n" +
                "\n" +
                "Section: undeprecate_docentry_1 - Submit an update to set the \n" +
                "availabilityStatus on docentry_1 back to Approved.\n" +
                "\n" +
                "Section: confirm_docentry_1_undeprecated - use GetDocuments Stored Query to \n" +
                "retrieve metadata for docentry_1 and verify it has availabilityStatus\n" +
                "of Approved.\n" +
                "\n" +
                "Section: load_docentry_2 - Submit a single DocumentEntry\n" +
                "\n" +
                "Section: update_docentry_2 - Submit an update to docentry_2\n" +
                "\n" +
                "Section: approve_orig_docentry_2 - Attempt to set the availabilityStatus\n" +
                "of the original version of docentry_2 back to Approved.  Since it is \n" +
                "not the most recent version, this must fail.\n" +
                "\n" +
                "Section: verify_orig_docentry_2_deprecated - Verify the previous step\n" +
                "did not change the availabilityStatus of the original version of\n" +
                "docentry_2 back to Approved." );
    }

    public Map<String, String> getCollectionNames(String collectionSetName) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("Initialize_for_Stored_Query","Initialize for Stored Query");
        map.put("rec","Document Recipient");
        map.put("reg","Document Registry");
        map.put("rep","Document Repository");
        map.put("rg", "Responding Gateway");
        return map;
    }


    public Map<String, String> getCollection(String collectionSetName, String collectionName) {
        List<String> nameList = getRequiredElements(collectionSetName,collectionName);
        Map<String, String> tests = new HashMap<String, String>();
        for (String name : nameList) {
            String description= getTestDescription(name);
            if (description!=null && !description.isEmpty()) {
                tests.put(name, description);
            }
        }
        return tests;
    }

    private List<String> getRequiredElements(String collectionSetName, String collectionName) {
        return testsMap.get(collectionName);
    }

    private String getTestDescription(String key){
        String readme = collectionsReadme.get(key);
        if (readme==null)
            return null;
        String[] parts = readme.split("\n");
        if (parts.length == 0)
            return "";
        return parts[0].trim();
    }

    public String getTestReadme(String test) {
        return  collectionsReadme.get(test);
    }
}
