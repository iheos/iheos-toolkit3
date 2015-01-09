package gov.nist.hit.ds.repository.shared;

/**
 * Created by skb1 on 12/22/14.
 */
public enum SortOrder {
    ASCENDING(){
        @Override
        public String toString() {
            return "asc";
        }
    }
    ,DESCENDING(){
        @Override
        public String toString() {
            return "desc";
        }
    }
    ,UNSPECIFIED(){
        @Override
        public String toString() {
            return "";
        }
    }

}
