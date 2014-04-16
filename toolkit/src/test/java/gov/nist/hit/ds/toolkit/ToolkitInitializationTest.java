package gov.nist.hit.ds.toolkit;

import org.junit.Test;

import java.io.File;

/**
 * Created by bmajur on 3/14/14.
 */
public class ToolkitInitializationTest {

    @Test
    public void toolkitLocationTest() {
        // actual initialization happens as a static method on package
        // initialization - all we have to do is reference it.

        File toolkitPropertiesFile = Toolkit.toolkitPropertiesFile();
        assert(toolkitPropertiesFile != null);
        assert(toolkitPropertiesFile.exists());
    }
}
