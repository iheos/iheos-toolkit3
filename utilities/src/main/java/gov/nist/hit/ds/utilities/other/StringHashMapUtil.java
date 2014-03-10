package gov.nist.hit.ds.utilities.other;

import java.util.HashMap;
import java.util.Map;

public class StringHashMapUtil {

	static public Map<String, String> reverse(Map<String, String> in)  {
		Map<String, String> out = new HashMap<String, String>();

		for (String key : in.keySet() ) {
			String val = in.get(key);
			out.put(val, key);
		}

		return out;
	}

}
