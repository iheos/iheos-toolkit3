package gov.nist.hit.ds.simSupport.client

import gov.nist.hit.ds.repoSupport.RepoUtils
import gov.nist.hit.ds.repository.api.Repository

/**
 * Created by bmajur on 2/3/15.
 */
class SimIdentifier {
    Repository repo
    SimId simId

    SimIdentifier(_repo, _simid) {
        repo = (_repo instanceof Repository) ? repo = _repo : RepoUtils.getRepository(_repo)
        simId = (_simid instanceof SimId) ? _simid : new SimId(_simid)
    }

    String getRepoName() { RepoUtils.repositoryId(repo).idString }

    boolean equals(SimIdentifier other) { other &&  other.repoName == repoName && other.simId.id == simId.id }

    String toString() { "${repoName}/${simId.id}"}

    boolean isValid() { repo && simId?.getId()}
}
