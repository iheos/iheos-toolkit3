package gov.nist.hit.ds.utilities.string;

public class StringUtil {

	static public String capitalize(String in) {
		if (in == null)
			return null;
		if (in.length() == 0)
			return in;
		String firstLetter = in.substring(0, 1);
		if (in.length() == 1) 
			return firstLetter;
		String rest = in.substring(1);
		return firstLetter.toUpperCase() + rest;
	}
	
	static public String removePrefix(String string, String prefix) {
		if (string == null || prefix == null)
			return null;
		if (!prefix.endsWith("."))
			prefix = prefix + ".";
		if (!string.startsWith(prefix))
			return string;
		return string.substring(prefix.length());
	}
	
}
