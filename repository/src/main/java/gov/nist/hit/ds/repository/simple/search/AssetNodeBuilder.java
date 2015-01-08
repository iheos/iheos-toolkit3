/**
 * 
 */
package gov.nist.hit.ds.repository.simple.search;

import gov.nist.hit.ds.repository.ContentHelper;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.Parameter;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.shared.PropertyKey;
import gov.nist.hit.ds.repository.shared.SearchCriteria;
import gov.nist.hit.ds.repository.shared.SearchCriteria.Criteria;
import gov.nist.hit.ds.repository.shared.SearchTerm;
import gov.nist.hit.ds.repository.shared.SearchTerm.Operator;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.shared.id.SimpleTypeId;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	}
	private Depth retrieveDepth;
	
	public AssetNodeBuilder() {
		super();
		setRetrieveDepth(Depth.CHILDREN);
	}
	
	public AssetNodeBuilder(Depth depth) {
		super();
		setRetrieveDepth(depth);
	}

    /**
     * Build a top-level tree using the sort order as specified by the repository type.
     * @param repos
     * @return
     * @throws RepositoryException
     */
    public List<AssetNode> build(Repository repos, int offset) throws RepositoryException {

        String jsonStr = getReposChildSortOrder(repos);
        if (jsonStr!=null && !"".equals(jsonStr)) {
            try {
                Map<String, String> o = ContentHelper.getHashMapFromJsonStr(jsonStr);

                if (o!=null && !o.isEmpty()) {
                    logger.fine("Size from JsonStr to HashMap is: " + o.size());
                    return build(repos, PropertyKey.getPropertyKeys(o), offset);

                }

            } catch (Throwable t) {
                t.printStackTrace();
            }

        }

        return build(repos,null,offset);
    }

    private String getReposChildSortOrder(Repository repos) throws RepositoryException {
        if (repos.getType()!=null && repos.getType().getProperties()!=null)
            return repos.getType().getProperties().getProperty(PropertyKey.CHILD_SORT_ORDER.toString());
        else
            return null;
    }


    /**
     * Build a top-level tree using the sort order as specified by the parameter.
     * @param repos
     * @param orderByKeys
     * @return
     * @throws RepositoryException
     */
	public List<AssetNode> build(Repository repos, PropertyKey[] orderByKeys, int offset) throws RepositoryException {
			
		List<AssetNode> topLevelAssets = new ArrayList<AssetNode>();

        AssetIterator iter = getTopLevelAssetIterator(repos, orderByKeys, offset, getChildFetchSize(repos.getType().getKeyword(), SimpleType.REPOSITORY));

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
				parent.setRelativePath(a.getPropFileRelativePart());
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
        return getTopLevelAssetIterator(repos,null,0,0);
    }

    private AssetIterator getTopLevelAssetIterator(Repository repos, PropertyKey[] orderByKeys, int offset, int fetchSize) throws RepositoryException {
        SearchCriteria criteria = new SearchCriteria(Criteria.AND);

        criteria.append(new SearchTerm(PropertyKey.PARENT_ID, Operator.EQUALTO,new String[]{null}));

        return new SearchResultIterator(new Repository[]{repos}, criteria, orderByKeys,offset,fetchSize);
    }

    public List<AssetNode> getImmediateChildren(Repository repos, AssetNode parent) throws RepositoryException {
        return getImmediateChildren(repos, parent, null, 0);
    }

    public List<AssetNode> getImmediateChildren(Repository repos, AssetNode parent, int offset) throws RepositoryException {
        return getImmediateChildren(repos, parent, null, offset);
    }

    public List<AssetNode> getImmediateChildren(Repository repos, AssetNode parent, SearchCriteria detailCriteria, int offset) throws RepositoryException {
		List<AssetNode> children = new ArrayList<AssetNode>();

        if (parent.getExtendedProps().get("_offset")!=null) // This is a placeholder leaf node
            return children;

        SearchCriteria parentCriteria = new SearchCriteria(Criteria.AND);


        // This may not be need since we are now using assetIds consistently
//        parentCriteria.append(new SearchTerm(PropertyKey.PARENT_ID, Operator.EQUALTOANY, new String[]{parent.getLocation(), parent.getAssetId()}));

        // Use only Parent Id
        parentCriteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTO,parent.getAssetId()));


        SearchCriteria criteria = null;

        if (detailCriteria!=null) {
            criteria = new SearchCriteria(Criteria.AND);
            criteria.append(parentCriteria);
            criteria.append(detailCriteria);
        } else {
            criteria = parentCriteria;
        }

		
		AssetIterator iter;
		try {
//            logger.info("parent id: " + parent.getAssetId());
//            logger.info("parent fullpath: " + parent.getFullPath());
//            logger.info("parent type: " + parent.getType());

            PropertyKey[] propertyKeys = null;
            String keyword = parent.getType();
            String domain = SimpleType.ASSET;

            propertyKeys = getSortOrderPropertyKeys(keyword, domain);
            int childFetchSize = getChildFetchSize(keyword, domain);

			iter = new SearchResultIterator(new Repository[]{repos}, criteria, propertyKeys,offset,childFetchSize);
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
					child.setRelativePath(a.getPropFileRelativePart());
                    child.setFullPath(a.getPath().toString());
				}

                String offsetIndicator = a.getProperty("_offset");

                if (offsetIndicator==null) {
				    child.setContentAvailable(a.hasContent());
                    addChildIndicator(repos, child);
                } else {
                    child.getExtendedProps().put("_offset",offsetIndicator);
                    // logger.fine(">>> HAS more parent-level nodes");
                    child.addChild(new AssetNode("","","","HASCHILDREN","","",""));
                }
				
				children.add(child);				
			}			
		} catch (RepositoryException e) {
			logger.warning(e.toString());
		}

		return children;
		
	}

    private PropertyKey[] getSortOrderPropertyKeys(String keyword, String domain) {
        String jsonStr = getTypeProperty(keyword, domain, PropertyKey.CHILD_SORT_ORDER);
        PropertyKey[] propertyKeys;

        if (jsonStr!=null) {
            propertyKeys = PropertyKey.getPropertyKeys(ContentHelper.getHashMapFromJsonStr(jsonStr));
        } else {
            propertyKeys = new PropertyKey[]{PropertyKey.DISPLAY_ORDER, PropertyKey.CREATED_DATE};
        }
        return propertyKeys;
    }

    private int getChildFetchSize(String keyword, String domain) {
        String str = getTypeProperty(keyword, domain, PropertyKey.CHILD_FETCH_SIZE);


        if (!"".equals(str) && str!=null) {
            try {
                return Integer.parseInt(str);
            } catch (Throwable t) {
                logger.fine(t.toString());
            }
        }
        return 0;
    }

    private String getTypeProperty(String keyword, String domain, PropertyKey propertyKey) {
        try {
            Parameter p = new Parameter();

            p.setDescription("Type file must exist in the types dir. Type: " + keyword + "domain: " + domain);
            p.assertNotNull(Configuration.configuration().getType(new SimpleTypeId(keyword, domain)));


            return Configuration.configuration().getType(new SimpleTypeId(keyword, domain)).getProperties().getProperty(propertyKey.toString());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
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
		criteria.append(new SearchTerm(PropertyKey.ASSET_ID, Operator.EQUALTO, target.getParentId()));
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
				if (!target.getRelativePath().equals(a.getPropFileRelativePart())) {
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
						parent.setRelativePath(a.getPropFileRelativePart());
					}
					parent.setContentAvailable(a.hasContent());
					
					List<AssetNode> children = getImmediateChildren(repos, parent);
					List<AssetNode> childrenUpdate = new ArrayList<AssetNode>();
					for (AssetNode c : children) {
						if (c.getRelativePath()!=null && c.getRelativePath().equals(target.getRelativePath())) {
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
                    parent.setRelativePath(a.getPropFileRelativePart());
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

//		criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTOANY, new String[]{potentialParent.getLocation(), potentialParent.getAssetId()}));
        criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTO, potentialParent.getAssetId()));

        int childRecords = 	new DbIndexContainer().getHitCount(repos, criteria);
        if (childRecords>0) {
            logger.fine(">>> HAS children");
            potentialParent.addChild(new AssetNode("","","","HASCHILDREN","","",""));
        }
        return true;



	}
	
	
	public AssetNode getChildren(Repository repos, AssetNode parent) throws RepositoryException {
//		if (parent!=null && parent.getAssetId()==null)
//			return null;
		
		SearchCriteria criteria = new SearchCriteria(Criteria.AND);

        // This may not be need since we are now using assetIds consistently
//		criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTOANY, new String[]{parent.getLocation(), parent.getAssetId()})); // parent.getAssetId()

        criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTO,parent.getAssetId()));
		 		
		AssetIterator iter;
		try {

            String keyword = parent.getType();
            String domain = SimpleType.ASSET;

            PropertyKey[] propertyKeys = getSortOrderPropertyKeys(keyword, domain);

			iter = new SearchResultIterator(new Repository[]{repos}, criteria, propertyKeys,0,0);
			
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
					child.setRelativePath(a.getPropFileRelativePart());
				}
				child.setContentAvailable(a.hasContent());
				parent.addChild(child);
				getChildren(repos,child);
			}
			
			
		} catch (RepositoryException e) {
			logger.warning(e.toString());
		}

        return parent;
	}

	public Depth getRetrieveDepth() {
		return retrieveDepth;
	}

	public void setRetrieveDepth(Depth retrieveDepth) {
		this.retrieveDepth = retrieveDepth;
	}
}
