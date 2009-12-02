package pl.pwr.logger.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GLogger implements EntryPoint
{
   /**
    * Create a remote service proxy to talk to the server-side Greeting service.
    */
   private final LoggingServiceAsync loggingService = GWT
         .create(LoggingService.class);

   /**
    * This is the entry point method.
    */
   public void onModuleLoad()
   {
      loggingService.verify(new AsyncCallback<Boolean>()
      {
         public void onFailure(Throwable caught)
         {
            caught.printStackTrace();
         }

         public void onSuccess(Boolean result)
         {
            
         }
         
      });
   }

}
