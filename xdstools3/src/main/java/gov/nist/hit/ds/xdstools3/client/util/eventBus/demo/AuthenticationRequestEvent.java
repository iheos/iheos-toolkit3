package gov.nist.hit.ds.xdstools3.client.util.eventBus.demo;

import com.google.gwt.event.shared.GwtEvent;


public class AuthenticationRequestEvent extends GwtEvent<AuthenticationRequestEventHandler> {

    public static Type<AuthenticationRequestEventHandler> TYPE = new Type<AuthenticationRequestEventHandler>();

    @Override
    public Type<AuthenticationRequestEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AuthenticationRequestEventHandler handler) {
        handler.onAuthenticationChanged(this);
    }
}