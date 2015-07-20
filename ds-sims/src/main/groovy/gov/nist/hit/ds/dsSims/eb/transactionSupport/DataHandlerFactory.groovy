package gov.nist.hit.ds.dsSims.eb.transactionSupport

import javax.activation.DataHandler
import javax.activation.FileDataSource

/**
 * Created by bmajur on 2/26/15.
 */
class DataHandlerFactory {

    static DataHandler get(FileDocumentHandler documentHandler) { new DataHandler(new FileDataSource(documentHandler.file)) }

    static DataHandler get(StringDocumentHandler documentHandler) { new DataHandler(documentHandler.content, documentHandler.mimeType) }
}
