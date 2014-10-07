package gov.nist.hit.ds.repository;

import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.SimpleRepository;

/**
 * This class contains repository related helper methods.
 * Created by skb1 on 10/1/14.
 */
public class RepositoryHelper {

    /**
     * Composes a repository object.
     * @param id The repository Id.
     * @param src The repository source.
     * @return The repository object.
     * @throws gov.nist.hit.ds.repository.api.RepositoryException
     */
    public static Repository composeRepositoryObject(String id, String src) throws RepositoryException {
        SimpleRepository repos
                = new SimpleRepository(new SimpleId(id));
        repos.setSource(Configuration.getRepositorySrc(RepositorySource.Access.valueOf(src)));

        return repos;
    }


    /**
     * Gets a repository object array from {@code String} array.
     * @param repos repository array
     */
    public static Repository[] getReposList(String[][] repos) {

        Repository[] reposList = null;
        try {

            int reposCt = repos.length;
            reposList = new Repository[reposCt];

            for (int cx=0; cx<reposCt; cx++) {
                try {
                    reposList[cx] =  RepositoryHelper.composeRepositoryObject(repos[cx][0], repos[cx][1]);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                    ; // incorrect or missing data repository config file
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reposList;
    }

}
