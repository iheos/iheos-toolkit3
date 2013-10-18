/**
 * 
 */
package gov.nist.hit.ds.repository.simple.search;

import java.util.ArrayList;
import java.util.List;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria.Criteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm.Operator;


/**
 * @author Sunil.Bhaskarla
 *
 */
public class AssetNodeBuilder {

	public AssetNodeBuilder() {
		super();
	}
	
	public List<AssetNode> build(Repository repos, PropertyKey key) throws RepositoryException {
		return build(repos,key.toString());
	}
	
	public List<AssetNode> build(Repository repos, String orderBy) throws RepositoryException {
		
		List<AssetNode> topLevelAssets = new ArrayList<AssetNode>();
	
		SearchCriteria criteria = new SearchCriteria(Criteria.AND);
		
		criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTO,new String[]{null}));			
		
		AssetIterator iter = new SearchResultIterator(new Repository[]{repos}, criteria, orderBy);

		while (iter.hasNextAsset()) {

			Asset a = iter.nextAsset();
			AssetNode parent = new AssetNode(a.getRepository().getIdString()
					,a.getId().getIdString()
					,a.getAssetType().toString()
					,a.getDisplayName()
					,a.getDescription()
					,a.getMimeType()
					,a.getSource().getAccess().name());
			getChildren(repos,parent);
			topLevelAssets.add(parent);
		}
				
		return topLevelAssets;
		
	}
	
	private void getChildren(Repository repos, AssetNode parent) {
		SearchCriteria criteria = new SearchCriteria(Criteria.AND);

		criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTO,parent.getAssetId()));			
		
		AssetIterator iter;
		try {
			iter = new SearchResultIterator(new Repository[]{repos}, criteria, PropertyKey.DISPLAY_ORDER);
			
			while (iter.hasNextAsset()) {
				Asset a = iter.nextAsset();
				AssetNode child = new AssetNode(a.getRepository().getIdString()
						,a.getId().getIdString()
						,a.getAssetType().toString()
						,a.getDisplayName()
						,a.getDescription()
						,a.getMimeType()
						,a.getSource().getAccess().name());
				
				parent.addChild(child);
				getChildren(repos,child);
			}
			
			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
