/**
 *
 */
package gov.nist.hit.ds.repository.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This enumeration contains some of the frequently used property keys, as used in {@see Properties#getProperty(java.io.Serializable)}, which is common to all types of domains (repositories or assets).
 *
 * @author Sunil.Bhaskarla
 */

public enum PropertyKey {
    UNIDENTIFIED(false) {},

    ASSET_ID(false) {
        @Override
        public String toString() {
            return "id";
        }
    },
    ASSET_NAME(false) {
        @Override
        public String toString() {
            return "name";
        }
    },
    ASSET_TYPE(false) {
        @Override
        public String toString() {
            return "type";
        }
    },
    /**
     * Display top n children, with any more hidden under the ellipses node
     */
    CHILD_FETCH_SIZE(false) {
        @Override
        public String toString() {
            return "childFetchSize";
        }
    },
    /**
     * Sort asset children based on the provided sort order. For example: childSortOrder={"UPDATED_DATE":"desc","COLOR":"desc"}
     */
    CHILD_SORT_ORDER(false) {
        @Override
        public String toString() {
            return "childSortOrder";
        }
    },
    COLOR(false) {
        @Override
        public String toString() {
            return "color";
        }
    },
    /**
     * This property value is expected to be a JSON-formatted string. For example: columnWidths={"ID":"50px"}
     */
    COLUMN_WIDTHS(true) {
        @Override
        public String toString() {
            return "columnWidths";
        }
    },
    COMPARE_HASH(false) {
        @Override
        public String toString() {
            return "compareHash";
        }
    },
    CREATED_DATE(false) {
        @Override
        public String toString() {
            return "createdDate";
        }
    },
    DESCRIPTION(false) {
        @Override
        public String toString() {
            return "description";
        }
    },
    DISPLAY_NAME(false) {
        @Override
        public String toString() {
            return "displayName";
        }
    },
    DISPLAY_ORDER(false) {
        @Override
        public String toString() {
            return "displayOrder";
        }
    },
    EXPIRATION_DATE(false) {
        @Override
        public String toString() {
            return "expirationDate";
        }
    },
    HASH(true) {
        @Override
        public String toString() {
            return "hash";
        }
    },
    INDEX_CURRENT_DAY_ITEMS(false) {
        @Override
        public String toString() {
            return "indexCurrentDayItems";
        }
    },
    INDEX_SESSION(true) {
        @Override
        public String toString() {
            return "indexSession";
        }
    },
    LAST_MODIFIED_TIME(true) {
        @Override
        public String toString() {
            return "lastModifiedTime";
        }
    },
    LIFETIME(true) {
        @Override
        public String toString() {
            return "lifetime";
        }
    },
    LOCATION(true) {
        @Override
        public String toString() {
            return "location";
        }
    },
    MIME_TYPE(false) {
        @Override
        public String toString() {
            return "mimeType";
        }
    },
    MODIFIED_DATE(false) {
        @Override
        public String toString() {
            return "modifiedDate";
        }
    },
    PARENT_ID(false) {
        @Override
        public String toString() {
            return "parentId";
        }
    },
    REPOSITORY_ID(false) {
        @Override
        public String toString() {
            return "repositoryId";
        }
    },
    REPOSITORY_ACCESS(true) {
        @Override
        public String toString() {
            return "reposAcs";
        }
    },
    REPOSITORY_TYPE(true) {
        @Override
        public String toString() {
            return "repositoryType";
        }
    },
    STATUS(false) {
        @Override
        public String toString() {
            return "status";
        }
    },
    UPDATED_DATE(false) {
        @Override
        public String toString() {
            return "updatedDate";
        }
    };

    private boolean internalUseOnly;

    private SortOrder sortOrder = SortOrder.UNSPECIFIED;

    public boolean isInternalUseOnly() {
        return internalUseOnly;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }


    public static PropertyKey getPropertyKey(String Pn) {
        if (Pn != null)
            for (PropertyKey p : PropertyKey.values()) {
                if (PnIdentifier.stripQuotes(Pn).equals(p.toString())) {
                    return p;
                }
            }
        return null;
    }

    public static String[] getStrings(PropertyKey[] orderByKeys) {
        String[] orderKeys = null;
        if (orderByKeys != null) {
            List<String> orderBy = new ArrayList<String>(orderByKeys.length);

            for (PropertyKey pk : orderByKeys) {
                String sortOrder = "";
                if (SortOrder.UNSPECIFIED != pk.getSortOrder()) {
                    sortOrder = pk.getSortOrder().toString();
                }
                String orderStr = pk.getPropertyName() + " " + sortOrder;
//                System.out.println("*** " + orderStr);
                orderBy.add(orderStr);
            }


            orderKeys = orderBy.toArray(new String[orderBy.size()]);

        }
        return orderKeys;
    }


    public static PropertyKey[] getPropertyKeys(Map<String, String> hashMap) {
        PropertyKey[] propertyKeys = null;

        if (hashMap!=null && !hashMap.isEmpty()) {
            propertyKeys = new PropertyKey[hashMap.size()];

            int cx=0;
            for (String s : hashMap.keySet()) {

                try {
                    PropertyKey propertyKey = PropertyKey.valueOf(s);

                    propertyKey.setSortOrder(SortOrder.valueOf(hashMap.get(s)));

                    propertyKeys[cx++] = propertyKey;

                } catch (Throwable t) {

                }
            }
        }

        return propertyKeys;
    }

    /**
     * This may be unsafe since plain strings can be harmful. Using an enumeration conversion protects against this case.
     */
    /*
    public static String[] getKeys(HashMap<String, String> hashMap) {

    }
    */

    public String getPropertyName() {
        if (isInternalUseOnly()) {
            return this.toString();
        } else {
            return PnIdentifier.getQuotedIdentifer(this.toString());
        }
    }

    public void setInternalUseOnly(boolean internalUseOnly) {
        this.internalUseOnly = internalUseOnly;
    }

    /**
     * @param isInternal This flag represents a property identifier that is primarily for non-business related data, however, some identifiers can fall in both categories.
     */
    private PropertyKey(boolean isInternal, SortOrder sortOrder) {
        setInternalUseOnly(isInternal);
        setSortOrder(sortOrder);
    }

    private PropertyKey(boolean isInternal) {
        setInternalUseOnly(isInternal);
        setSortOrder(SortOrder.UNSPECIFIED);
    }

}
