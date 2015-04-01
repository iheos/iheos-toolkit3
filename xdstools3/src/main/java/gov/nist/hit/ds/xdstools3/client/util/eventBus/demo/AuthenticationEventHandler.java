package gov.nist.hit.ds.xdstools3.client.util.eventBus.demo;


import gov.nist.hit.ds.xdstools3.client.util.eventBus.OpenTabEventHandler;

public interface AuthenticationEventHandler extends OpenTabEventHandler {
        void onAuthenticationChanged(AuthenticationEvent authenticationEvent);
    }
