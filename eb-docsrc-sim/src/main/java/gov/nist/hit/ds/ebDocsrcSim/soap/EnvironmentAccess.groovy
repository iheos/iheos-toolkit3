package gov.nist.hit.ds.ebDocsrcSim.soap

import gov.nist.hit.ds.utilities.io.Io
import gov.nist.hit.ds.xdsException.EnvironmentNotSelectedException
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException

/**
 * Created by bmajur on 1/14/15.
 */
class EnvironmentAccess implements SecurityParams {
    File environmentDir

    EnvironmentAccess(File _environmentDir) { environmentDir = _environmentDir }

    def validate() {
        if (!environmentDir.isDirectory()) throwup('Does not exist.')
        if (!keystoreDir().isDirectory()) throwup('keystore directory does not exist.')
        if (!keystoreFile().exists()) throwup('keystore file does not exist.')
        if (!propertiesFile().exists()) throwup('keystore properties file does not exist.')
    }

    def throwup(msg) { throw new ToolkitRuntimeException("Environment: ${environmentDir}: ${message}") }

    File codesFile() { new File(environmentDir, 'codes.xml')}

    File keystoreDir() { new File(environmentDir, 'keystore') }

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
