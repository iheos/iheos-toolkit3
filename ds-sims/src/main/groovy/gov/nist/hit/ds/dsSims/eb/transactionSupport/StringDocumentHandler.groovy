package gov.nist.hit.ds.dsSims.eb.transactionSupport

/**
 * Created by bmajur on 2/26/15.
 */
class StringDocumentHandler implements DocumentHandler {
    String content
    String mimeType

    StringDocumentHandler(String _content, String _mimeType) {
        content = _content
        mimeType = _mimeType
    }
}
