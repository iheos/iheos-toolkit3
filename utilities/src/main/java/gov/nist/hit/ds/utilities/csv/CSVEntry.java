package gov.nist.hit.ds.utilities.csv;


import java.util.ArrayList;
import java.util.List;

public class CSVEntry implements IEntryFactory {
	List<String> items = new ArrayList<String>();
	public String line;

	public List<String> getItems() {
		return items;
	}
	
	public CSVEntry(int size) {
		for (int i=0; i<size; i++) 
			items.add(null);
	}
	
	public CSVEntry set(int index, String value) {
		items.set(index, value);
		return this;
	}
		
	public CSVEntry(String line) {
		this.line = line;
		int stringStart = -1;
		boolean committed;
        boolean quotedText = false; // This will distinguish plain quoted text from quotes in non-quoted text. Example: ...,mustUnderstand="true",...

		committed = false;
		for (int cursor=0; cursor < line.length(); cursor++) {
			char c = line.charAt(cursor);

			if (isWhite(c)) {
				if (stringStart==-1 && committed) { // Previously, empty fields were lost because they were not handled
                    committed = false;
                }
			} else if (c == ',') {
				if (stringStart == -1) {
					if (committed)
						committed = false;
					else
						add("");
				} else {
					add(line.substring(stringStart, cursor).trim());
					stringStart = -1;
					committed = true;
				}
			} else if (c == '"') {
				if (stringStart == -1) {
                    quotedText = true;
					stringStart = cursor + 1;
                } else if (quotedText) {
					String contents = line.substring(stringStart, cursor).trim(); 
					add(contents);
					stringStart = -1;
					committed = true;
                    quotedText = false;
				}
			} else {
				if (stringStart == -1)
					stringStart = cursor;
			}
		}
		
		if (stringStart != -1) {
			add(line.substring(stringStart, line.length()).trim());
		}
		
	}
	
	boolean isWhite(char c) { return c == ' ' || c == '\t'; }
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		for (String item : items)
			buf.append(item).append(", ");
		
		return buf.toString();
	}
	
	public String get(int i) {
		if (i >= items.size())
			return "";
		return items.get(i);
	}

	public CSVEntry add(String item) {
//        logger.info("adding item : "+ item);
		items.add(item);
		return this;
	}
	
	public CSVEntry() {}

	@Override
	public CSVEntry mkEntry(String line) {
		return new CSVEntry(line);
	}
	
}
