package edu.tn.xds.metadata.editor.client.root.submission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;

/**
 * Created by onh2 on 7/11/2014.
 */
public class SubmissionMenuData {
    public static final SubmissionMenuData.SubmissionMenuDataProperties props = GWT.create(SubmissionMenuData.SubmissionMenuDataProperties.class);
    private String key;
    private String value;
    //    private Place place;
    private DocumentModel model;

    public SubmissionMenuData(String key, String value) {
        this.value = value;
        this.key = key;
    }

    public SubmissionMenuData(String key, String value, DocumentModel model) {
        this.key = key;
        this.value = value;
        this.model = model;
//        this.place = new EditorPlace();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

//    public Place getPlace() {
//        return place;
//    }
//
//    public void setPlace(Place place) {
//        this.place = place;
//    }

    public DocumentModel getModel() {
        return model;
    }

    public void setModel(DocumentModel model) {
        this.model = model;
    }

    public interface SubmissionMenuDataProperties extends PropertyAccess<SubmissionMenuData> {

        ModelKeyProvider<SubmissionMenuData> key();

        @Editor.Path("value")
        LabelProvider<SubmissionMenuData> valueLabel();

        ValueProvider<SubmissionMenuData, String> value();


    }
}
