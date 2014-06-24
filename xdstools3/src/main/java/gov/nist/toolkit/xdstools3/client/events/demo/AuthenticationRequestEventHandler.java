package gov.nist.toolkit.xdstools3.client.events.demo;


import gov.nist.toolkit.xdstools3.client.events.EventHandler;

public interface AuthenticationRequestEventHandler extends EventHandler {
        void onAuthenticationChanged(AuthenticationRequestEvent authenticationEvent);
    }
