package edu.tn.xds.metadata.editor.client.widgets.uploader;

import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent;

import javax.inject.Inject;

/**
 * Created by onh2 on 8/21/2014.
 */
public class FileUploadDialog extends Dialog {

    @Inject
    FileUploadMVP fileUploadMVP;
    @Inject
    MetadataEditorEventBus eventBus;

    public FileUploadDialog() {
        super();
        setBodyBorder(false);
        setHeadingText("File Upload");
        setHideOnButtonClick(true);

        // delete the default button
        getButtonBar().remove(0);
        setModal(true);
    }

    public void init() {
        fileUploadMVP.init();
        fileUploadMVP.start();
        add(fileUploadMVP.getDisplay());
        bind();
    }

    private void bind() {
        fileUploadMVP.getView().getBtnCancel().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent selectEvent) {
                hide();
            }
        });
        eventBus.addFileLoadedHandler(new NewFileLoadedEvent.NewFileLoadedHandler() {

            @Override
            public void onNewFileLoaded(NewFileLoadedEvent event) {
                hide();
            }
        });
    }
}
