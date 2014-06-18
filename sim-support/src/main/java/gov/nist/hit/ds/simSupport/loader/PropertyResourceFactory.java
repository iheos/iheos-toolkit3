package gov.nist.hit.ds.simSupport.loader;

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyResourceFactory {
    String propertiesPath;
    Properties props = null;
    private static Logger logger = Logger.getLogger(PropertyResourceFactory.class);

    public PropertyResourceFactory(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    public void load() {
        props = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream(propertiesPath);
        try {
            if (in != null) props.load(in);
        }
        catch (Exception e) { }
        finally {
            if (in == null) {
                String msg = "Cannot load SimChain definition from <" + propertiesPath + ">";
                logger.error(msg);
                throw new ToolkitRuntimeException(msg);
            }
            try {
                in.close();
            } catch (Exception e) {}
        }
    }

    public Properties getProperties() throws IOException {
        if (props == null)
            load();
        return props;
    }
}
