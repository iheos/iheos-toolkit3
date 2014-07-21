package gov.nist.hit.ds.eventLog

/**
 * Created by bill on 4/15/14.
 */
class InOutMessages {
    String reqHdr = null
    byte[] reqBody = null
    String respHdr = null
    byte[] respBody = null

    def reqHdrSaved = false
    def reqBodySaved = false
    def respHdrSaved = false
    def respBodySaved = false

    public String getRequestHeader() { reqHdr }
    public byte[] getRequestBody() { rreqBody }
}
