package edu.tn.xds.metadata.editor.client.root;

import com.google.gwt.place.shared.PlaceController;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import edu.tn.xds.metadata.editor.client.editor.DocumentModelEditorMVP;
import edu.tn.xds.metadata.editor.client.event.EditNewEvent;
import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent;
import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent.NewFileLoadedHandler;
import edu.tn.xds.metadata.editor.client.event.SaveFileEvent;
import edu.tn.xds.metadata.editor.client.widgets.uploader.FileUploadMVP;

import javax.inject.Inject;

public class NorthPanel extends ContentPanel {

    private final TextButton loadButton;
    private final TextButton newButton;
    private final TextButton saveButton;
    @Inject
	PlaceController placeController;
	@Inject
	FileUploadMVP fileUploadMVP;
	@Inject
	DocumentModelEditorMVP documentModelEditorMVP;
	@Inject
	MetadataEditorEventBus eventBus;
	private Dialog loadingDialog;

	public NorthPanel() {
		setHeaderVisible(false);
		setBorders(false);

		HBoxLayoutContainer c = new HBoxLayoutContainer();
		c.setPadding(new Padding(5,20,5,5));
		c.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		c.setPack(BoxLayoutPack.END);

		BoxLayoutData layoutData = new BoxLayoutData(new Margins(0, 10, 0, 0));

		newButton = new TextButton("New");
		newButton.setSize("50", "-1");
		c.add(newButton, layoutData);

		loadButton = new TextButton("Load");
		loadButton.setSize("50", "-1");
		c.add(loadButton, layoutData);

		saveButton = new TextButton("Download file");
		saveButton.setSize("50", "-1");
		saveButton.disable();
		c.add(saveButton, layoutData);

		this.add(c);
	}

	private void bindUI() {
		loadButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				loadingDialog = new Dialog();
				loadingDialog.setBodyBorder(false);
				loadingDialog.setHeadingText("File Upload");
				loadingDialog.setHideOnButtonClick(true);
				loadingDialog.add(fileUploadMVP.getDisplay());

				// delete the default button
				loadingDialog.getButtonBar().remove(0);
				loadingDialog.setModal(true);
				loadingDialog.show();
			}

		});
		fileUploadMVP.getView().getBtnCancel().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				loadingDialog.hide();

			}

		});
		eventBus.addFileLoadedHandler(new NewFileLoadedHandler() {

			@Override
			public void onNewFileLoaded(NewFileLoadedEvent event) {
                if(loadingDialog!=null) {
                    loadingDialog.hide();
                    saveButton.enable();
                }
			}
		});
        eventBus.addEditNewEventHandler(new EditNewEvent.EditNewHandler() {
            @Override
            public void onEditNew(EditNewEvent event) {
                if(saveButton!=null) {
                    saveButton.enable();
                }
            }
        });
		newButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				saveButton.enable();
                eventBus.fireEditNewEvent(new EditNewEvent());
//				placeController.goTo(new EditorPlace());
            }

		});
		saveButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				eventBus.fireSaveFileEvent(new SaveFileEvent());
			}
		});

	}

	public void start() {
		fileUploadMVP.init();
		fileUploadMVP.start();

		bindUI();
	}

}
