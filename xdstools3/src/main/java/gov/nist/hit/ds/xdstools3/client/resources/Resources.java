package gov.nist.hit.ds.xdstools3.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

import java.io.Serializable;

/** Usage example for Text Resources: Resources.INSTANCE.getContactPageContents().getText();
 *  Usage example for Img Resources: new Image(Resources.INSTANCE.getRemoveIcon());
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

    @Source("icons/Button_Blank_Blue_Icon_32.png")
    ImageResource getBlueRoundIcon();

    @Source("icons/glyphicons/glyphicons-458-transfer.png")
    ImageResource getTransferIcon();

    @Source("icons/glyphicons/glyphicons_027_search.png")
    ImageResource getSearchIcon();

    @Source("icons/glyphicons/glyphicons_086_display.png")
    ImageResource getDisplayIcon();

    @Source("icons/glyphicons/glyphicons_123_message_out.png")
    ImageResource getMessageOutIcon();

    @Source("icons/glyphicons/glyphicons_136_cogwheel.png")
    ImageResource getCogwheelIcon();

    @Source("icons/glyphicons/glyphicons_194_circle_question_mark.png")
    ImageResource getCircleQuestionMarkIcon();

    @Source("icons/glyphicons/glyphicons_280_settings.png")
    ImageResource getSettingsIcon();

    @Source("icons/glyphicons/glyphicons-30-notes-2.png")
    ImageResource getNotesIcon();

    @Source("icons/glyphicons/glyphicons-151-edit.png")
    ImageResource getEditIcon();

    @Source("icons/glyphicons/glyphicons-152-new-window.png")
    ImageResource getExportIcon();

    @Source("icons/glyphicons/glyphicons-163-ipad.png")
    ImageResource getIpadIcon();

}
