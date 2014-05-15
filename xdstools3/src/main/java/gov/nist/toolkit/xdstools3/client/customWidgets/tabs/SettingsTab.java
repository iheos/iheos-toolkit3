package gov.nist.toolkit.xdstools3.client.customWidgets.tabs;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.form.fields.SectionItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import gov.nist.toolkit.xdstools3.client.customWidgets.CheckboxItemWithTooltipWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TextItemWithTooltipWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.UploadItemWithTooltipWidget;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import gov.nist.toolkit.xdstools3.client.customWidgets.forms.FormattedDynamicForm;

public class SettingsTab extends CloseableTabWidget {
	String title = "Settings";
	String propertiesTitle = "Properties";


	public SettingsTab() {

        // Teb name
        super("Settings");

        // load default or example config parameters

		// Set title and subtitles
		setHeader(title);


		// Contents
        FormattedDynamicForm form = new FormattedDynamicForm();


        TextItem host = createField("host", "Toolkit host", "transport-testing.nist.gov", 400);
        TextItem port = createField("port", "Toolkit port", "12080", 400);
        TextItem tls_port = createField("tls_port", "Toolkit TLS Port", "12081", 400);
        TextItem cache = createField("cache", "External cache", "Ex.: C:\\Workspace\\external_cache", 400);
        TextItem environment = createField("environment", "Default environment	", "Ex.: MU2014, NA2014...", 400);
        TextItem gazelle = createField("gazelle", "Gazelle config URL", "", 400);
        TextItem password = createField("password", "Admin password", "", 400);


        // Build form sections
        SectionItem section1 = new SectionItem();
        section1.setDefaultValue("General");
        section1.setSectionExpanded(true);
        section1.setItemIds("host", "port", "tls_port", "cache", "environment");

        SectionItem section2 = new SectionItem();
        section2.setDefaultValue("Connectathon");
        section2.setSectionExpanded(false);
        section2.setItemIds("gazelle");

        form.setFields(new FormItem[] {
                section1, host, port, tls_port, cache, environment, section2, gazelle
        });

        // formatting
        form.setWidth(600);
        form.setLayoutAlign(Alignment.CENTER);
        form.setMinColWidth(400);

		getPanel().addMember(form);

	}  

	public TextItem createField(String name, String title, String hint, int width) {
		TextItem item = new TextItem();
		item.setName(name);
		item.setTitle(title);
        item.setHint(hint);
        item.setShowHintInField(true);
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