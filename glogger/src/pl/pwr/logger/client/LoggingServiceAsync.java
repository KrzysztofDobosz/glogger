package pl.pwr.logger.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface LoggingServiceAsync
{
   void verify(String userName, String data, AsyncCallback<Boolean> callback);

   void train(String userName, ArrayList<String> dataArray,
         AsyncCallback<Void> callback);
}
