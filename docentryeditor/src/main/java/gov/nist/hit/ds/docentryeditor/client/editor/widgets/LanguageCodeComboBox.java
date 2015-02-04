package gov.nist.hit.ds.docentryeditor.client.editor.widgets;

import java.util.*;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import gov.nist.hit.ds.docentryeditor.shared.model.LanguageCode;

/**
 * <p>
 * <b>This class represents the widget which matches LanguageCode model type</b>
 * <br/>
 * </p>
 */
public class LanguageCodeComboBox extends ComboBox<LanguageCode> {

	static ListStore<LanguageCode> languages = new ListStore<LanguageCode>(new ModelKeyProvider<LanguageCode>() {

		@Override
		public String getKey(LanguageCode item) {
			if (item == null) {
				return "NULL";
			}
			return item.toString();
		}
	});

	public LanguageCodeComboBox() {
		super(languages, new LabelProvider<LanguageCode>() {

			@Override
			public String getLabel(LanguageCode item) {
				return item.toString() + " (" + item.name() + ")";
			}
		});

		getStore().clear();

		List<LanguageCode> l = new ArrayList<LanguageCode>(Arrays.asList(LanguageCode.values()));

		Collections.sort(l, new Comparator<LanguageCode>() {

			@Override
			public int compare(LanguageCode o1, LanguageCode o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		getStore().addAll(l);

		setTriggerAction(TriggerAction.ALL);
		setForceSelection(true);
		setTypeAhead(true);
	}

}
