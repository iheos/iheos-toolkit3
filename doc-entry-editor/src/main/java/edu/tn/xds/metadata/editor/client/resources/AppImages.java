package edu.tn.xds.metadata.editor.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 */
public interface AppImages extends ClientBundle {
    AppImages INSTANCE = GWT.create(AppImages.class);

    @Source("baseResources/add.gif")
    ImageResource add();

    @Source("baseResources/clear.gif")
    ImageResource clear();

    @Source("baseResources/delete.gif")
    ImageResource delete();

    @Source("baseResources/checkbox.png")
    ImageResource checkbox();

    //    @Source("baseResources/help.ico.gif")
    @Source("baseResources/comment.png")
    ImageResource help();

    @Source("baseResources/file.png")
    ImageResource file();

    @Source("baseResources/save-disk.png")
    ImageResource save();

}
