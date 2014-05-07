package gov.nist.hit.ds.repository.simple.search.client;


import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetNode implements IsSerializable, Serializable {

	/**
	 * AssetNode is essentially a light-weight DTO that carries out key asset information from the Asset type object
	 * 
	 * @author Sunil.Bhaskarla
	 */
	private static final long serialVersionUID = -46123676112710466L;
	
	private String repId;
	private String assetId;
	private String parentId;
	private String type;
	private String displayName;
	private String description;
	private String mimeType;
	private String reposSrc;
	private String location;
	private String createdDate;
	
	private boolean hasContent;
	private String txtContent;
	private String props;
    private Map<String,String> extendedProps = new HashMap<String, String>();
	private String[][] csv;
	
	
	private List<AssetNode> children = new ArrayList<AssetNode>();
	
	public AssetNode() {
		
	}
	
	/**
	 * @param repId
	 * @param assetId
	 * @param type
	 * @param displayName
	 * @param description
	 */
	public AssetNode(String repId, String assetId, String type,
			String displayName, String description, String mimeType, String src) {
		super();
		this.repId = repId;
		this.assetId = assetId;
		this.type = type;
		this.displayName = displayName;
		this.description = description;
		this.mimeType = mimeType;
		this.reposSrc = src;
	}

	public List<AssetNode> getChildren() {
		return children;
	}
	
	public void addChild(AssetNode a) {
		children.add(a);
	}
	
	public void addChildren(List<AssetNode> children) {
		this.children.addAll(children);
	}
	
	public String getRepId() {
		return repId;
	}
	public void setRepId(String repId) {
		this.repId = repId;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getReposSrc() {
		return reposSrc;
	}

	public void setReposSrc(String reposSrc) {
		this.reposSrc = reposSrc;
	}

	public String getTxtContent() {
		return txtContent;
	}

	public void setTxtContent(String txtContent) {
		this.txtContent = txtContent;
	}

	public String getProps() {
		return props;
	}

	public void setProps(String props) {
		this.props = props;
	}

	public String[][] getCsv() {
		return csv;
	}

	public void setCsv(String[][] csv) {
		this.csv = csv;
	}

	public boolean isContentAvailable() {
		return hasContent;
	}

	public void setContentAvailable(boolean hasContent) {
		this.hasContent = hasContent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

    public Map<String, String> getExtendedProps() {
        return extendedProps;
    }

    public void setExtendedProps(Map<String, String> extendedProps) {
        this.extendedProps = extendedProps;
    }

}
