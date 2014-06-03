package gov.nist.hit.ds.repository.simple.search.client;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SimpleData implements IsSerializable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -20395611696338839L;
	
	private int row;
	private int col;
	ArrayList<String> myList = new ArrayList<String>();
	
	
	public SimpleData() {}
	
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public ArrayList<String> getMyList() {
		return myList;
	}
	public void setMyList(ArrayList<String> myList) {
		this.myList = myList;
	}
	
	public void addItem(String s) {
		myList.add(s);
	}
	
	

}
