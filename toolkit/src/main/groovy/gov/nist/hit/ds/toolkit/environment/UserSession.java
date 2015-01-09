package gov.nist.hit.ds.toolkit.environment;

import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmajur on 12/1/14.
 */
public class UserSession {
    File externalCache;

    public UserSession(File _externalCache) { externalCache = _externalCache; }

    public List<String> names() {
        List<String> names = new ArrayList<String>();

        File dir = new File(externalCache, "TestLogCache");
        for (String name : dir.list()) {
            File file = new File(dir, name);
            if (file.isDirectory()) names.add(name);
        }

        return names;
    }

    public void add(String name) {
        File dir = new File(externalCache, "TestLogCache");
        File file = new File(dir, name);
        if (file.isDirectory()) return;
        if (file.exists()) throw new ToolkitRuntimeException("Cannot create user sesson " + name + " - non-directory file of same name exists in " + dir);
        file.mkdir();
    }

    public void delete(String name) {
        File dir = new File(externalCache, "TestLogCache");
        File file = new File(dir, name);
        if (!file.exists()) return;
        if (file.isDirectory()) Io.delete(file);
    }
}
