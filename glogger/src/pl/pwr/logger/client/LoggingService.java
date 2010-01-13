package pl.pwr.logger.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("logger")
public interface LoggingService extends RemoteService
{
   Boolean verify(String userName, String data);
   void train(String userName, ArrayList<String> dataArray);
}
