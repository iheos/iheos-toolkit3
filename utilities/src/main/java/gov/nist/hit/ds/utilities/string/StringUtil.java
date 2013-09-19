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
		if (!string.startsWith(prefix))
			return string;
		return string.substring(prefix.length());
	}
	
	static public String mustEndWith(String string, String suffix) {
		if (string.endsWith(suffix))
			return string;
		return string + suffix;
	}
	
	static public String removePrefixEndingWith(String string, String ending) {
		int index = string.indexOf(ending);
		if (index == -1)
			return string;
		int prefixLength = index + ending.length();
		return string.substring(prefixLength);
	}
	
	static public String lastPiece(String string, String separator) {
		if (string == null || string.equals(""))
			return string;
		String[] parts = string.split(separator);
		if (parts == null || parts.length == 0)
			return string;
		return parts[parts.length - 1];
	}
	
	static public String firstPiece(String string, String separator) {
		int index = string.indexOf(separator);
		if (index == -1)
			return string;
		return string.substring(0, index);
	}
	
	static public boolean asBoolean(String value) {
		if ("false".equalsIgnoreCase(value)) return false;
		if ("no".equalsIgnoreCase(value)) return false;
		if ("0".equals(value)) return false;
		return true;
	}
	
	static public String firstNChars(String s, int n) {
		if (s == null) return null;
		if (s.length() <= n) return s;
		return s.substring(0, n);
	}
	
}
