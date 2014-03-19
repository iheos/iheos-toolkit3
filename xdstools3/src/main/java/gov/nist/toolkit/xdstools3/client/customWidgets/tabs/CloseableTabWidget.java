package gov.nist.toolkit.xdstools3.client.customWidgets.tabs;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class CloseableTabWidget extends Tab implements TabInterface {
	private VLayout panel = new VLayout(10);
	
	public CloseableTabWidget(String s){
		 setCanClose(true); 
		 setTitle(s);
	}

	public VLayout getPanel() {
		return panel;
	}
	
	// main header - h1
	public void setHeader(String s){
		Label l = new Label();
		s = "<b>" + s + "</b>";
		l.setContents(s);
		l.setHeight(20);
		panel.addMember(l);
		setPane(panel);
	}
	
	// subtitles - h2
	public void setSubtitle(String s){
		Label l = new Label();
		l.setContents(s);
		l.setHeight(20);
		panel.addMember(l);
		setPane(panel);
		
	}
	
	
}
