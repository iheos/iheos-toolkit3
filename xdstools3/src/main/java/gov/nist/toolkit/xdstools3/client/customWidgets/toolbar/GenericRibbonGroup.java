package gov.nist.toolkit.xdstools3.client.customWidgets.toolbar;


import com.smartgwt.client.widgets.toolbar.RibbonGroup;

/**
 * Class based on SmartGWT RibbonGroup for toolbars. This class holds basic toolbar formatting.
 *
 * @see com.smartgwt.client.widgets.toolbar.RibbonBar
 * Created by dazais on 11/10/2014.
 */

public class GenericRibbonGroup extends RibbonGroup {

    public GenericRibbonGroup(){
        setStyleName("toolbarGroup");
        setTitleStyle("toolbarGroupTitle");
        setBorder("1px solid #CCD1FF"); /* Setting the border inside the CSS does not work on this component, for some reason */
    }

}
