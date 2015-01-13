package gov.nist.hit.ds.toolkit.environment;

import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException;

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

        File dir = new File(externalCache, "user_sessions");
        for (String name : dir.list()) {
            File file = new File(dir, name);
            if (file.isDirectory()) names.add(name);
        }

        return names;
    }

    /**
     * Adds a new session by creating a new session directory inside folder user_sessions
     * If folder user_sessions does not exist, it is created.
     * If the session already exists, nothing is done.
     * TODO this behavior should probably be changed later to warn the user that a session with that name already exists
     * If a file, not a directory, with the session name exists, an error is thrown. That case should not be encountered.
     * @param name  the name of the new session
     */
    public void add(String name) {
        File dir = new File(externalCache, "user_sessions");
        if (!dir.exists()) dir.mkdir();
        File file = new File(dir, name);
        if (file.isDirectory()) return;
        if (file.exists()) throw new ToolkitRuntimeException("Cannot create user session " + name + " - non-directory file of same name exists in " + dir);
        file.mkdir();
    }

    public void delete(String name) {
        File dir = new File(externalCache, "user_sessions");
        File file = new File(dir, name);
        if (!file.exists()) return;
        if (file.isDirectory()) Io.delete(file);
    }
}
