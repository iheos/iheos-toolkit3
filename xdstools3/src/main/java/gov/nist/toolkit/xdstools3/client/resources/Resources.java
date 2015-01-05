package gov.nist.toolkit.xdstools3.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Created by dazais on 12/29/2014.
 */
public interface Resources extends ClientBundle {
    public static final Resources INSTANCE = GWT.create(Resources.class);

    @Source("help-contents/mhd-validator.txt")
    TextResource helpContentsSample();

}
