package gov.nist.ds.rest.client.exceptionHandling;



/**
 * Class to map application related exceptions. Based on code from Codingpedia article
 * by Adrian Matei.
 *
 * @author amacoder from Codingpedia
 * http://www.codingpedia.org/ama/error-handling-in-rest-api-with-jersey/
 *
 */
public class AppException extends Exception {

    private static final long serialVersionUID = -8999932578270387947L;

    /**
     * contains redundantly the HTTP status of the response sent back to the client in case of error, so that
     * the developer does not have to look into the response headers. If null a default
     */
    Integer status;

    /** link documenting the exception */
    String link;


    /**
     *
     * @param status
     * @param message
     * @param link
     */
    public AppException(int status, String message, String link) {
        super(message);
        this.status = status;
        this.link = link;
    }

    public AppException() { }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}