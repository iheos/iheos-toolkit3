package gov.nist.hit.ds.dsSims.eb.metadataValidator.model

public class ClassAndIdDescription {
	public List<String> definedSchemes;
    public List<String> requiredSchemes;
    public List<String> multipleSchemes;
	// id => name mapping
    public Map<String, String> names;
}
