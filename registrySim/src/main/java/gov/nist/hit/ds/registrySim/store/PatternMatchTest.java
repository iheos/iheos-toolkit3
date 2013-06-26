package gov.nist.hit.ds.registrySim.store;


public class PatternMatchTest {

	DocEntryCollection c = new DocEntryCollection();
	String pattern;
	String ret;
	int startAt;

	String value;
	boolean good;
	
	void run2() throws Exception {
		value = "Auth";
		pattern = "%th";
		good = DocEntryCollection.matchAuthor(value, pattern);
		expect(true);
		
		value = "Smi";
		pattern = "S%";
		good = DocEntryCollection.matchAuthor(value, pattern);
		expect(true);
		
		value = "Smi";
		pattern = "S%i";
		good = DocEntryCollection.matchAuthor(value, pattern);
		expect(true);
		
		value = "Smbi";
		pattern = "S%i";
		good = DocEntryCollection.matchAuthor(value, pattern);
		expect(true);
		
		value = "Author Jack Smith";
		pattern = "Author%";
		good = DocEntryCollection.matchAuthor(value, pattern);
		expect(true);
		
		value = "Author Jack Smith";
		pattern = "%Jack%";
		good = DocEntryCollection.matchAuthor(value, pattern);
		expect(true);
		
		value = "Author Jack Smith";
		pattern = "Author Jack%";
		good = DocEntryCollection.matchAuthor(value, pattern);
		expect(true);
		
		value = "Author Jack Smith";
		pattern = "%";
		good = DocEntryCollection.matchAuthor(value, pattern);
		expect(true);
		
		value = " th";
		pattern = "%th%";
		good = DocEntryCollection.matchAuthor(value, pattern);
		expect(true);
		
		value = "Author Jack Smith";
		pattern = "%Jane%";
		good = DocEntryCollection.matchAuthor(value, pattern);
		expect(false);
		
		
	}
	
	void run() throws Exception {

		pattern = "%And%";
		startAt = 0;
		ret = DocEntryCollection.getAfterText(pattern, startAt);
		expect("And");

		pattern = "%And";
		startAt = 0;
		ret = DocEntryCollection.getAfterText(pattern, startAt);
		expect("And");
		
		pattern = "%And%Or";
		startAt = 0;
		ret = DocEntryCollection.getAfterText(pattern, startAt);
		expect("And");
		
		pattern = "%And%Or";
		startAt = 4;
		ret = DocEntryCollection.getAfterText(pattern, startAt);
		expect("Or");
		
	}
	
	void expect(String expect) throws Exception {
		System.out.println("expect = " + expect);
		if (!expect.equals(ret))
			throw new Exception("Expected [" + expect + "] got [" + ret + "]");
	}
	

	void expect(boolean val) throws Exception {
		if (good != val)
			throw new Exception("fail");
	}
	
	static public void main(String[] args) {
		try {
//			new PatternMatchTest().run();
			new PatternMatchTest().run2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done");
	}

}
