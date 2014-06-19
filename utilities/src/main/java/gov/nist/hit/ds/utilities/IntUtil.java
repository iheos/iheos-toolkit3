package gov.nist.hit.ds.utilities;

public class IntUtil {

	static public int min(int a, int b) {
		if (a < b) return a;
		return b;
	}
	
	static public int max(int a, int b) {
		if (a > b) return a;
		return b;
	}
}
