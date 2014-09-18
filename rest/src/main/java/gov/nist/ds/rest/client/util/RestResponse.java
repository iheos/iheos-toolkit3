package gov.nist.ds.rest.client.util;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by dazais on 9/11/2014.
 */
@XmlType
public abstract class RestResponse {

    public static int STATUS_FAILURE = -1;
    public static int STATUS_LOGIN_INCORRECT = -5;
    public static int STATUS_LOGIN_REQUIRED = -7;
    public static int STATUS_LOGIN_SUCCESS = -8;
    public static int STATUS_MAX_LOGIN_ATTEMPTS_EXCEEDED = -6;
    public static int STATUS_SERVER_TIMEOUT = -100;
    public static int STATUS_SUCCESS = 0;
    public static int STATUS_TRANSPORT_ERROR = -90;
    public static int STATUS_VALIDATION_ERROR = -4;

    private int status;

    public void setStatus(int status) {
        this.status = status;
    }
}
