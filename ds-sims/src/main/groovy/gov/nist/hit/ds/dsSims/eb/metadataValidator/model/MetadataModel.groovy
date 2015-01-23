package gov.nist.hit.ds.dsSims.eb.metadataValidator.model
/**
 * Created by bmajur on 12/24/14.
 */
class MetadataModel {
    List<SubmissionSetModel> submissionSets
    List<DocumentEntryModel> docEntries
    List<FolderModel> folders
    List<AssociationModel> assocs

    Set<String> knownIds = new HashSet<String>();

}
