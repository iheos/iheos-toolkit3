package gov.nist.hit.ds.registryMetadataValidator.object;

import java.util.List;
import java.util.Map;

public class ClassAndIdDescription {
	List<String> definedSchemes;
	List<String> requiredSchemes;
	List<String> multipleSchemes;
	// id => name mapping
	Map<String, String> names;
}
