package gov.nist.toolkit.xdstools3.client.eventBusUtils.demo;


import gov.nist.toolkit.xdstools3.client.eventBusUtils.OpenTabEventHandler;

public interface AuthenticationEventHandler extends OpenTabEventHandler {
        void onAuthenticationChanged(AuthenticationEvent authenticationEvent);
    }
