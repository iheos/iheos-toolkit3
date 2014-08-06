/**
 * 
 */
package gov.nist.hit.ds.repository.simple.search;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria.Criteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * @author Sunil.Bhaskarla
 *
 */
public class AssetNodeBuilder {
	private static Logger logger = Logger.getLogger(AssetNodeBuilder.class.getName());
	 public static enum Depth {		 
			CHILDREN,
			PARENT_ONLY
	};	 
	private Depth retrieveDepth;
	
	public AssetNodeBuilder() {
		super();
		setRetrieveDepth(Depth.CHILDREN);
	}
	
	public AssetNodeBuilder(Depth depth) {
		super();
		setRetrieveDepth(depth);
	}
	
	public List<AssetNode> build(Repository repos, PropertyKey orderByKey) throws RepositoryException {
		return build(repos, orderByKey.toString());
	}
	
	public List<AssetNode> build(Repository repos, String orderByKey) throws RepositoryException {
			
		List<AssetNode> topLevelAssets = new ArrayList<AssetNode>();

        AssetIterator iter = getTopLevelAssetIterator(repos);

		while (iter.hasNextAsset()) {

			Asset a = iter.nextAsset();
			AssetNode parent = new AssetNode(a.getRepository().getIdString()
					,a.getId().getIdString()
					,(a.getAssetType()==null)?null:a.getAssetType().toString()
					,a.getDisplayName()
					,a.getDescription()
					,a.getMimeType()
					,a.getSource().getAccess().name());
			parent.setParentId(a.getProperty(PropertyKey.PARENT_ID));
            parent.setColor(a.getProperty(PropertyKey.COLOR));
			if (a.getPath()!=null) {
				// parent.setLocation(a.getPath().toString());
				parent.setLocation(a.getPropFileRelativePart());
			}
			parent.setContentAvailable(a.hasContent());
			
			if (Depth.CHILDREN.equals(getRetrieveDepth())) {
				getChildren(repos,parent);				
			} else {
				addChildIndicator(repos, parent);
			}

			topLevelAssets.add(parent);
		}
				
		return topLevelAssets;
		
	}

    private AssetIterator getTopLevelAssetIterator(Repository repos) throws RepositoryException {
        SearchCriteria criteria = new SearchCriteria(Criteria.AND);

        criteria.append(new SearchTerm(PropertyKey.PARENT_ID, Operator.EQUALTO,new String[]{null}));

        return new SearchResultIterator(new Repository[]{repos}, criteria);
    }


    public List<AssetNode> getImmediateChildren(Repository repos, AssetNode parent) throws RepositoryException {
		List<AssetNode> children = new ArrayList<AssetNode>();

		SearchCriteria criteria = new SearchCriteria(Criteria.AND);

		criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTOANY, new String[]{parent.getLocation(), parent.getAssetId()})); 
		
		AssetIterator iter;
		try {
			iter = new SearchResultIterator(new Repository[]{repos}, criteria, new PropertyKey[]{PropertyKey.DISPLAY_ORDER, PropertyKey.CREATED_DATE} );
			while (iter.hasNextAsset()) {
				Asset a = iter.nextAsset();
				AssetNode child = new AssetNode(a.getRepository().getIdString()
						,(a.getId()!=null)?a.getId().getIdString():null
						,(a.getAssetType()==null)?null:a.getAssetType().toString()
						,a.getDisplayName()
						,a.getDescription()
						,a.getMimeType()
						,a.getSource().getAccess().name());
				child.setParentId(a.getProperty(PropertyKey.PARENT_ID));
                child.setColor(a.getProperty(PropertyKey.COLOR));
				if (a.getPath()!=null) {
					// child.setLocation(a.getPath().toString());
					child.setLocation(a.getPropFileRelativePart());
				}
				child.setContentAvailable(a.hasContent());

				addChildIndicator(repos, child);
				
				children.add(child);				
			}			
		} catch (RepositoryException e) {
			logger.warning(e.toString());
		}

		return children;
		
	}

    /**
     *
     * @param repos
     * @param target
     * @param initial Set to True for the initial call
     * @return
     * @throws RepositoryException
     */
	public AssetNode getParentChain(Repository repos, AssetNode target, boolean initial) throws RepositoryException {
		SearchCriteria criteria = new SearchCriteria(Criteria.OR);
		criteria.append(new SearchTerm(PropertyKey.ASSET_ID,Operator.EQUALTO, target.getParentId()));
		criteria.append(new SearchTerm(PropertyKey.LOCATION,Operator.EQUALTO, target.getParentId()));

 		
		AssetIterator iter;
		try {
			if (initial) {
				target.addChildren(getImmediateChildren(repos, target));
			}
			iter = new SearchResultIterator(new Repository[]{repos}, criteria);
			
			if (iter.hasNextAsset()) { // Get the first one
				Asset a = iter.nextAsset();
				// logger.fine("is --- " + child.getLocation() + " = " + a.getPropFileRelativePart());
				if (!target.getLocation().equals(a.getPropFileRelativePart())) {
					AssetNode parent = new AssetNode(a.getRepository().getIdString()
							,(a.getId()!=null)?a.getId().getIdString():null
							,(a.getAssetType()==null)?null:a.getAssetType().toString()
							,a.getDisplayName()
							,a.getDescription()
							,a.getMimeType()
							,a.getSource().getAccess().name());
					parent.setParentId(a.getProperty(PropertyKey.PARENT_ID));
                    parent.setColor(a.getProperty(PropertyKey.COLOR));
					if (a.getPath()!=null) {
						// child.setLocation(a.getPath().toString());
						parent.setLocation(a.getPropFileRelativePart());
					}
					parent.setContentAvailable(a.hasContent());
					
					List<AssetNode> children = getImmediateChildren(repos, parent);
					List<AssetNode> childrenUpdate = new ArrayList<AssetNode>();
					for (AssetNode c : children) {
						if (c.getLocation()!=null && c.getLocation().equals(target.getLocation())) {
							childrenUpdate.add(target);
						} else
							childrenUpdate.add(c);
					}
					parent.addChildren(childrenUpdate);
					
					return getParentChain(repos, parent, false);
					
				}
			} 
		} catch (Exception ex) {
			logger.warning(ex.toString());
		}
		return target;
	}

    /**
     * Gets the parent chain in the context of the entire repository tree (not just the parent chain) from a target asset node.
     * {@code
     *  Example:
     *  repos:  [root]
     *  parent1 [parent chain]
     *      - child
     *          -target [x]
     *  parent2
     *  parent3
     *  parent4
     * }
     * @param repos
     * @param target
     * @return
     * @throws RepositoryException
     */
    public List<AssetNode> getParentChainInTree(Repository repos, AssetNode target) throws RepositoryException {

        // 1. Get the parent chain
        AssetNode parentChain = getParentChain(repos, target, true);

        // 2. Insert the parent chain of interest in the root context tree
        List<AssetNode> topLevelAssets = new ArrayList<AssetNode>();
        AssetIterator iter = getTopLevelAssetIterator(repos);

        while (iter.hasNextAsset()) {

            Asset a = iter.nextAsset();
            AssetNode parent = null;

            if (a.getId()!=null && a.getId().getIdString().equals(parentChain.getAssetId())) {
                parent = parentChain;
            } else {
                parent = new AssetNode(a.getRepository().getIdString()
                        ,a.getId().getIdString()
                        ,(a.getAssetType()==null)?null:a.getAssetType().toString()
                        ,a.getDisplayName()
                        ,a.getDescription()
                        ,a.getMimeType()
                        ,a.getSource().getAccess().name());
                parent.setParentId(a.getProperty(PropertyKey.PARENT_ID));
                parent.setColor(a.getProperty(PropertyKey.COLOR));
                if (a.getPath()!=null) {
                    // parent.setLocation(a.getPath().toString());
                    parent.setLocation(a.getPropFileRelativePart());
                }
                parent.setContentAvailable(a.hasContent());

                addChildIndicator(repos, parent);

            }

            topLevelAssets.add(parent);
        }

        return topLevelAssets;


    }

	private boolean addChildIndicator(Repository repos, AssetNode potentialParent) throws RepositoryException {
		SearchCriteria criteria = new SearchCriteria(Criteria.AND);
		criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTOANY, new String[]{potentialParent.getLocation(), potentialParent.getAssetId()})); 
		int childRecords = 	new DbIndexContainer().getHitCount(repos, criteria);
		if (childRecords>0) {
			logger.fine(">>> HAS children");
			potentialParent.addChild(new AssetNode("","","","HASCHILDREN","","",""));				
		}
		return true;
	}
	
	
	private void getChildren(Repository repos, AssetNode parent) {
		if (parent.getAssetId()==null)
			return;
		
		SearchCriteria criteria = new SearchCriteria(Criteria.AND);

		criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTOANY, new String[]{parent.getLocation(), parent.getAssetId()})); // parent.getAssetId()
		 		
		AssetIterator iter;
		try {
			iter = new SearchResultIterator(new Repository[]{repos}, criteria, new PropertyKey[]{PropertyKey.DISPLAY_ORDER, PropertyKey.CREATED_DATE});
			
			while (iter.hasNextAsset()) {
				Asset a = iter.nextAsset();
				AssetNode child = new AssetNode(a.getRepository().getIdString()
						,(a.getId()!=null)?a.getId().getIdString():null
						,(a.getAssetType()==null)?null:a.getAssetType().toString()
						,a.getDisplayName()
						,a.getDescription()
						,a.getMimeType()
						,a.getSource().getAccess().name());
				child.setParentId(a.getProperty(PropertyKey.PARENT_ID));
                child.setColor(a.getProperty(PropertyKey.COLOR));
				if (a.getPath()!=null) {
					// child.setLocation(a.getPath().toString());
					child.setLocation(a.getPropFileRelativePart());
				}
				child.setContentAvailable(a.hasContent());
				parent.addChild(child);
				getChildren(repos,child);
			}
			
			
		} catch (RepositoryException e) {
			logger.warning(e.toString());
		}


	}

	public Depth getRetrieveDepth() {
		return retrieveDepth;
	}

	public void setRetrieveDepth(Depth retrieveDepth) {
		this.retrieveDepth = retrieveDepth;
	}
}
