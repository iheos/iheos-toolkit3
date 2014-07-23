package gov.nist.toolkit.xdstools3.client.eventBusUtils.demo;


import gov.nist.toolkit.xdstools3.client.eventBusUtils.OpenTabEventHandler;

public interface AuthenticationRequestEventHandler extends OpenTabEventHandler {
        void onAuthenticationChanged(AuthenticationRequestEvent authenticationEvent);
    }
