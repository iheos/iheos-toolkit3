package gov.nist.hit.ds.dsSims.eb.metadataValidator.parser

import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.*
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.toolkit.valsupport.client.ValidationContext
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 12/24/14.
 */
class MetadataParser {
    MetadataModel model = new MetadataModel()

    public MetadataModel run(Metadata m, ValidationContext vc)   {

        model.submissionSets = m.getSubmissionSets().collect { OMElement ss -> new SubmissionSetModel(m, ss) }

        model.docEntries = m.getExtrinsicObjects().collect  { OMElement de -> new DocumentEntryModel(m, de)  }

        model.folders = m.getFolders().collect { OMElement f -> new FolderModel(m, f)  }

        model.assocs = m.getAssociations().collect { OMElement a -> new AssociationModel(m, a, vc)  }

        return model
    }

}
