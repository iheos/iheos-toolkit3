package gov.nist.toolkit.xdstools3.client.util.eventBus.demo;


import gov.nist.toolkit.xdstools3.client.util.eventBus.OpenTabEventHandler;

public interface AuthenticationEventHandler extends OpenTabEventHandler {
        void onAuthenticationChanged(AuthenticationEvent authenticationEvent);
    }
