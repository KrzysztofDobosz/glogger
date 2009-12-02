package pl.pwr.logger.server;

import pl.pwr.logger.client.LoggingService;
import Jama.Matrix;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoggingServiceImpl extends RemoteServiceServlet implements
      LoggingService
{
   public Boolean verify()
   {
      double[][] v1 = {{1.,2.},{3.,4.},{5.,6.}};
      Matrix alphaM = new Matrix(v1);
      double[][] v2 = {{3.,2.,1.}};
      Matrix betaM = new Matrix(v2);
      double[][] v3 = {{1.},{0.}};
      Matrix input1 = new Matrix(v3);
      double[][] v4 = {{1.},{1.}};
      Matrix input2 = new Matrix(v4);
      double[][] v5 = {{0.},{1.}};
      Matrix input3 = new Matrix(v5);
      double[][] v6 = {{0.},{0.}};
      Matrix input4 = new Matrix(v6);
      
      double[][] v7 = {{1.}};
      Matrix out1 = new Matrix(v7);
      double[][] v8 = {{0.}};
      Matrix out2 = new Matrix(v8);
      double[][] v9 = {{1.}};
      Matrix out3 = new Matrix(v9);
      double[][] v10 = {{0.}};
      Matrix out4 = new Matrix(v10);
      
      NeuralNetwork nn = new NeuralNetwork(alphaM, betaM, 0.01, 0.6);
      Matrix res = nn.getOutput(input1);
      res.print(1, 10);
      
      for (int i = 0; i < 10; i++)
      {
         nn.learn(input1, out1);
         nn.learn(input2, out2);
         nn.learn(input3, out3);
         nn.learn(input4, out4);
      }
      
      res = nn.getOutput(input1);
      res.print(1, 10);
      
      return null;
   }
   
}
