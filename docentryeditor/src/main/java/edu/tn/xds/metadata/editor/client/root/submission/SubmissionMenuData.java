package edu.tn.xds.metadata.editor.client.root.submission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry;

/**
 * Created by onh2 on 7/11/2014.
 */
public class SubmissionMenuData {
    public static final SubmissionMenuData.SubmissionMenuDataProperties props = GWT.create(SubmissionMenuData.SubmissionMenuDataProperties.class);
    private String key;
    private String value;
    //    private Place place;
    // FIXME this class will have to become generic and handle <M> instead of DocumentModel,
    // thus it will be able to deal with SubmissionSet and Association also.
    private XdsDocumentEntry model;

    public SubmissionMenuData(String key, String value) {
        this.value = value;
        this.key = key;
    }

    public SubmissionMenuData(String key, String value, XdsDocumentEntry model) {
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

    public XdsDocumentEntry getModel() {
        return model;
    }

    public void setModel(XdsDocumentEntry model) {
        this.model = model;
    }

    public interface SubmissionMenuDataProperties extends PropertyAccess<SubmissionMenuData> {

        ModelKeyProvider<SubmissionMenuData> key();

        @Editor.Path("value")
        LabelProvider<SubmissionMenuData> valueLabel();

        ValueProvider<SubmissionMenuData, String> value();


    }
}
