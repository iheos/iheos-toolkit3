package gov.nist.hit.ds.dsSims.eb.metadataValidator.object;

import java.util.List;
import java.util.Map;

public class ClassAndIdDescription {
	List<String> definedSchemes;
	List<String> requiredSchemes;
	List<String> multipleSchemes;
	// id => displayName mapping
	Map<String, String> names;
}
