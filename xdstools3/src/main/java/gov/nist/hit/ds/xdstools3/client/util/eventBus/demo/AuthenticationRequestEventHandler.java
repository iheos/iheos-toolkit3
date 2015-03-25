package gov.nist.hit.ds.xdstools3.client.util.eventBus.demo;


import gov.nist.hit.ds.xdstools3.client.util.eventBus.OpenTabEventHandler;

public interface AuthenticationRequestEventHandler extends OpenTabEventHandler {
        void onAuthenticationChanged(AuthenticationRequestEvent authenticationEvent);
    }
