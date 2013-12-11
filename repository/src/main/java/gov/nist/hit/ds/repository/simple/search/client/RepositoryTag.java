package gov.nist.hit.ds.repository.simple.search.client;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * RepositoryTag is a value object that contains key repository attributes.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RepositoryTag implements Comparable<RepositoryTag>, IsSerializable, Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1367578960873454316L;
	// instance variables 
    private String id;
    private String displayName;
    private String source;
    private String properties;
   
    /**
     * Constructor for objects of class RepositoryTag
     */
    public RepositoryTag()
    {
        
    }
    public RepositoryTag(String id, String source, String displayName, String properties)
    {
        setId(id);
        setSource(source);
        setDisplayName(displayName);
        setProperties(properties);
        
    }
    
    // @Override
    public int compareTo(RepositoryTag rtag) {
        return getDisplayName().compareToIgnoreCase(rtag.getDisplayName());
    }
    
    public String getDisplayName() {
        if (displayName!=null)
            return displayName;
        else if (id!=null)
            return id;
        else
            return "Unknown";
    }
    
    public String getCompositeId() {
        return "" + getId() + "^" + getSource();
    }
    
    public String getId() {
        return id;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setSource(String source) {
         this.source = source;
        }
        
    public String getSource() {
        return source;
    }
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}

   

        
    
        

}
