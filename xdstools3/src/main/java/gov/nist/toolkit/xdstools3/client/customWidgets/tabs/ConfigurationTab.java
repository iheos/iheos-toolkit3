package gov.nist.toolkit.xdstools3.client.customWidgets.tabs;

import gov.nist.toolkit.xdstools3.client.customWidgets.CheckboxItemWithTooltipWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TextItemWithTooltipWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.UploadItemWithTooltipWidget;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class ConfigurationTab extends CloseableTabWidget {
	String header = "Configure XDS Toolkit";
	String propertiesTitle = "Properties";


	public ConfigurationTab() { 

		super("Configuration");

		// Set header and subtitles
		setHeader(header);
		setSubtitle(propertiesTitle);

		// Contents
		DynamicForm form = new DynamicForm();

		TextItemWithTooltipWidget host = createField("host", "Toolkit_Host", "Domain name of the toolkit", 400);

		TextItemWithTooltipWidget cache = createField("cache", "External_Cache", "Path of external_cache", 400);

		TextItemWithTooltipWidget password = createField("password", "Admin_password", "Password for administrator", 400);

		TextItemWithTooltipWidget tls_port = createField("tls_port", "Toolkit_TLS_Port", "Port for TLS transactions", 400);

		CheckboxItemWithTooltipWidget ciphers = createCheckBox("ciphers", "Enable_all_ciphers", "Enable or disable all ciphers", 400);

		TextItemWithTooltipWidget environment = createField("environment", "Default_Environment	", "Default toolkit environment<br />Example: <br />MU2014<br />NA2014", 400);

		CheckboxItemWithTooltipWidget actors = createCheckBox("actors", "Use_Actors_File", "Use actors file", 400);

		TextItemWithTooltipWidget gazelle = createField("gazelle", "Gazelle_Config_URL", "URL of Gazelle to configure toolkit", 400);

		TextItemWithTooltipWidget port = createField("port", "Toolkit_Port", "Port for toolkit", 400);

		form.setFields(new FormItem[] { 
				host, cache, password, tls_port, ciphers, environment, actors, gazelle, port
		}); 


		getPanel().addMember(form);
		//getPanel().draw();
	}  

	public TextItemWithTooltipWidget createField(String name, String title, String tooltip, int width) {
		TextItemWithTooltipWidget item = new TextItemWithTooltipWidget();
		item.setName(name);
		item.setTitle(title);
		item.setTooltip(tooltip);
		item.setWidth(width);
		return item;
	}

	public CheckboxItemWithTooltipWidget createCheckBox(String name, String title, String tooltip, int width) {
		CheckboxItemWithTooltipWidget item = new CheckboxItemWithTooltipWidget();
		item.setName(name);
		item.setTitle(title);
		item.setTooltip(tooltip);
		item.setWidth(width);
		return item;
	}

	public UploadItemWithTooltipWidget createUploadItem(String name, String title, String tooltip, int width) {
		UploadItemWithTooltipWidget item = new UploadItemWithTooltipWidget();
		item.setName(name);
		item.setTitle(title);
		item.setTooltip(tooltip);
		item.setWidth(width);
		return item;
	}


}  