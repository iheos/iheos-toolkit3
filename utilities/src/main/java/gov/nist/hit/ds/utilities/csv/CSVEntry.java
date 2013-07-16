package gov.nist.hit.ds.utilities.csv;


import java.util.ArrayList;
import java.util.List;

public class CSVEntry implements IEntryFactory {
	List<String> items = new ArrayList<String>();
	public String line;

	public List<String> getItems() {
		return items;
	}
		
	public CSVEntry(String line) {
		this.line = line;
		int stringStart = -1;
		boolean committed;

		committed = false;
		for (int cursor=0; cursor < line.length(); cursor++) {
			char c = line.charAt(cursor);

			if (isWhite(c)) {
				
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
				if (stringStart == -1)
					stringStart = cursor + 1;
				else {
					String contents = line.substring(stringStart, cursor).trim(); 
					add(contents);
					stringStart = -1;
					committed = true;
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

	void add(String item) {
		items.add(item);
	}
	
	public CSVEntry() {}

	@Override
	public CSVEntry mkEntry(String line) {
		return new CSVEntry(line);
	}
	
}
