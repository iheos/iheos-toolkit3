package gov.nist.toolkit.xdstools3.client.events.demo;

import com.google.gwt.event.shared.GwtEvent;


    public class AuthenticationEvent extends GwtEvent<AuthenticationEventHandler> {

        public static Type<AuthenticationEventHandler> TYPE = new Type<AuthenticationEventHandler>();

        @Override
        public Type<AuthenticationEventHandler> getAssociatedType() {
            return TYPE;
        }

        @Override
        protected void dispatch(AuthenticationEventHandler handler) {
            handler.onAuthenticationChanged(this);
        }
}
