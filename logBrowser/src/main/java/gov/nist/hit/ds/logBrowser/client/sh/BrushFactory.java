package gov.nist.hit.ds.logBrowser.client.sh;

import com.google.gwt.core.client.JavaScriptObject;

public class BrushFactory extends JavaScriptObject {

	protected BrushFactory() {
	}

	public static native BrushFactory get()/*-{
		return $wnd.brushfactory;
	}-*/;

	public static native JavaScriptObject newXmlBrush() /*-{
		return new $wnd.SyntaxHighlighter.brushes.Xml();
	}-*/;
	
	public static native JavaScriptObject newPlainBrush() /*-{
		return new $wnd.SyntaxHighlighter.brushes.Plain();
}-*/;
}