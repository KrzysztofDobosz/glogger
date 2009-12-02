package pl.pwr.logger.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface LoggingServiceAsync
{
   void verify(AsyncCallback<Boolean> callback);

}
