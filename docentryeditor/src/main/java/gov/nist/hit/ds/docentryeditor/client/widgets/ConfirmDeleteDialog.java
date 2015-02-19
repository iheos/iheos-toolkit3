package gov.nist.hit.ds.docentryeditor.client.widgets;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;

public class ConfirmDeleteDialog extends ConfirmMessageBox {

	public ConfirmDeleteDialog(String valueToDelete) {
		super("Confirm delete dialog", "Are you sure you want to delete this value: " + valueToDelete);
	}

}
