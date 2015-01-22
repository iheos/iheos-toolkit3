package gov.nist.hit.ds.toolkit.environment

import gov.nist.hit.ds.toolkit.Toolkit
import gov.nist.hit.ds.utilities.io.Io
import gov.nist.hit.ds.xdsExceptions.EnvironmentNotSelectedException
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import org.apache.commons.io.FilenameUtils

/**
 * Identifies the selected Environment.
 *
 * Created by bmajur on 1/14/15.
 */
class EnvironmentAccess implements SecurityParams {
    File file
    String name

    EnvironmentAccess(File _environmentDir) {
        file = _environmentDir
        FilenameUtils.getBaseName(file.toString())
        validate()
    }
    EnvironmentAccess(String _environmentName) {
        name = _environmentName
        file = new File(Toolkit.environmentsFile(), name)
        validate()
    }

    def validate() {
        if (!file.isDirectory()) throwup('Does not exist.')
        if (!keystoreDir().isDirectory()) return
        if (!keystoreFile().exists()) throwup('keystore file does not exist.')
        if (!propertiesFile().exists()) throwup('keystore properties file does not exist.')
    }

    def throwup(msg) { throw new ToolkitRuntimeException("Environment: ${file}: ${msg}") }

    File codesFile() { new File(file, 'codes.xml')}

    File keystoreDir() { new File(file, 'keystore') }

    File keystoreFile() { new File(keystoreDir(), 'keystore') }

    File propertiesFile() { new File(keystoreDir(), 'keystore.properties') }

    @Override
    File getKeystore() { keystoreFile() }

    @Override
    String getKeystorePassword() throws IOException, EnvironmentNotSelectedException {
        Properties props = new Properties()
        props.load(Io.getInputStreamFromFile(propertiesFile()))
        return props.getProperty('keyStorePassword')
    }
}
