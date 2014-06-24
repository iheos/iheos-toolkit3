package gov.nist.toolkit.xdstools3.client.events.demo;


import gov.nist.toolkit.xdstools3.client.events.EventHandler;

public interface AuthenticationEventHandler extends EventHandler {
        void onAuthenticationChanged(AuthenticationEvent authenticationEvent);
    }
