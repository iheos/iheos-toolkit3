package gov.nist.toolkit.xdstools3.client.events;


    public interface AuthenticationRequestEventHandler extends EventHandler {
        void onAuthenticationChanged(AuthenticationRequestEvent authenticationEvent);
    }
