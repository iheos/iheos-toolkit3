package gov.nist.hit.ds.docentryeditor.shared.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by onh2 on 2/19/2015.
 */
public class XdsMetadata implements Serializable{
    private static final long serialVersionUID = 1L;

    private List<XdsDocumentEntry> documentEntries;

    public XdsMetadata(){}

    public List<XdsDocumentEntry> getDocumentEntries(){return documentEntries;}

    public void setDocumentEntries(List<XdsDocumentEntry> documentEntries){this.documentEntries=documentEntries;}
}
