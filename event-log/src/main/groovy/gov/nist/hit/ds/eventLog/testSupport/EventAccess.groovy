package gov.nist.hit.ds.eventLog.testSupport

import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration

/**
 * Created by bmajur on 7/13/14.
 */
class EventAccess {
    RepositorySource repoSource
    File repoDataDir
    def eventLogDirName = 'Events'
    File simsDir
    String simid
    Event event

    EventAccess(String simid, Event event) {
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simsDir = new File(repoDataDir, 'Sim')
        this.simid = simid
        this.event = event
    }

    File simDir() {
        return new File(simsDir, simid)
    }

    File eventLogDir() { new File(simDir(), eventLogDirName) }

    File eventDir() {
        assert event
        assert event.eventAsset
        new File(eventLogDir(), event.eventAsset.getDisplayName())
    }

    File reqBodyFile() {
        new File(new File(eventDir(), 'Input Output Messages'), 'Request Body.bytes')
    }

    File eventPropertiesFile() {
        new File(eventDir(), "${event.eventAsset.getDisplayName()}.parent.props.txt")
    }

    File respBodyFile() {
        new File(new File(eventDir(), 'Input Output Messages'), 'Response Body.bytes')
    }

    File assertionGroupFile(String agName) {
        new File(new File(eventDir(), 'Validators'), "${agName}.csv")
    }

    File assertionGroupFile(String agName, subGroups) {
        File file = new File(eventDir(), 'Validators')
        subGroups.each { dirname ->
            file = new File(file, dirname)
        }
        return new File(file, "${agName}.csv")
    }

    File propertiesFile(String agName) {
        new File(new File(eventDir(), 'Validators'), "${agName}.props.txt")
    }

    File propertiesFile(String agName, subGroups) {
        File file = new File(eventDir(), 'Validators')
        subGroups.each { dirname ->
            file = new File(file, dirname)
        }
        return new File(file, "${agName}.props.txt")
    }
    File faultFile() {
        new File(eventDir(), 'SoapFault.txt')
    }

}



