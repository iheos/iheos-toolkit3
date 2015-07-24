package gov.nist.hit.ds.xdstools3.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

/** Usage: Resources.INSTANCE.getContactPageContents().getText();
 *  Usage: new Image(Resources.INSTANCE.getRemoveIcon());
 *
 * Created by dazais on 12/29/2014.
 */
public interface Resources extends ClientBundle {
    public static final Resources INSTANCE = GWT.create(Resources.class);

    // ----- Load text files -----//
    @Source("help-contents/mhd-validator.htm")
    TextResource getMHDValidatorHelpContents();

    @Source("contact/contact.htm")
    TextResource getContactPageContents();


    // ----- Load icons -----//
    @Source("icons/delete.png")
    ImageResource getRemoveIcon();

    @Source("icons/refresh-128.png")
    ImageResource getRefreshIcon();

    @Source("icons/play.png")
    ImageResource getPlayIcon();
}
