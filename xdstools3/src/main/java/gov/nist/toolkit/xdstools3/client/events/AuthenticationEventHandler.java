package gov.nist.toolkit.xdstools3.client.events;


    public interface AuthenticationEventHandler extends EventHandler {
        void onAuthenticationChanged(AuthenticationEvent authenticationEvent);
    }
