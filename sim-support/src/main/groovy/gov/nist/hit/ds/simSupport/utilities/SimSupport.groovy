package gov.nist.hit.ds.simSupport.utilities
import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.repository.api.RepositoryFactory
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.toolkit.Toolkit
import gov.nist.hit.ds.toolkit.installation.Installation
/**
 * Created by bmajur on 7/4/14.
 */

/**
 * Initialization of Repository facilities supporting validators and simulators.
 */
class SimSupport {
    static RepositorySource repoSource
    static RepositoryFactory repoFactory
    static Repository eventRepo
//    static Repository simRepo
    static eventLogName = 'EventLog'
    static simRepoName = 'Sim'
//    File repoDataDir
    static hasRun = false

    static initialize() {
        if (hasRun) return
        hasRun = true

        initToolkit()
        initActors()
        initFactories()
        initRepositories()
    }

    static initToolkit() {
        // these may be redundant when running full toolkit
        Toolkit.initialize()
        Installation.reset()
        Installation.installation().initialize()
        Configuration.configuration()
    }

    static initActors() {
//        new ActorTransactionTypeFactory().clear()
//        new ActorTransactionTypeFactory().loadFromResource('at.xml')
    }

    static initFactories() {
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoFactory = new RepositoryFactory(repoSource)
    }

    static initRepositories() {
        eventRepo = RepoUtils.mkRepository(eventLogName, new SimpleType('eventLog'))
//        simRepo = RepoUtils.mkRepository(simRepoName, new SimpleType('simRepos'))
    }
}
