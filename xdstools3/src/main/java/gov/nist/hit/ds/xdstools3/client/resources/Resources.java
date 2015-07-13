package gov.nist.hit.ds.xdstools3.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Created by dazais on 12/29/2014.
 */
public interface Resources extends ClientBundle {
    public static final Resources INSTANCE = GWT.create(Resources.class);

    // ----- Load text files -----//
    @Source("help-contents/mhd-validator.htm")
    TextResource getMHDValidatorHelpContents();


    @Source("contact/contact.htm")
    TextResource getContactPageContents();


}
