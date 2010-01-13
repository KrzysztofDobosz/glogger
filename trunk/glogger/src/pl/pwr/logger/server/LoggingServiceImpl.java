package pl.pwr.logger.server;

import java.util.ArrayList;

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
   public static String dbname = "gloggerdb";
   public static String dbuser = "gloggeruser";
   public static String dbpass = "gloggerpass";
   public static double nnBeta = 1.0;
   public static double nnEta = 0.6;

   public Boolean verify(String userName, String data)
   {
      DatabaseConnector db = new DatabaseConnector("jdbc:mysql://localhost/"
            + dbname, dbuser, dbpass);

      Matrix flp = new Matrix(Crypto.decrypt(db.getParams(userName, 1), Crypto
            .getKey()));
      Matrix slp = new Matrix(Crypto.decrypt(db.getParams(userName, 2), Crypto
            .getKey()));
      NeuralNetwork nn = new NeuralNetwork(flp, slp, nnBeta, nnEta);

      String[] lines = data.split(",");
      double[][] matrix = new double[lines.length][1];
      for (int i = 0; i < lines.length; i++)
         matrix[i][0] = new Double(lines[i]);

      Matrix result = nn.getOutput(new Matrix(matrix));
      System.out.println(result.get(0, 0));
      if (result.get(0, 0) > 0.5)
         return true;
      else
         return false;

   }

   public Boolean train(String userName, ArrayList<String> dataArray)
   {
      double[][] one =
      {
      { 1. } };
      double[][] zero =
      {
      { 0. } };

      Matrix flp = Matrix.identity(100, 50);
      Matrix slp = Matrix.identity(1, 100);
      NeuralNetwork nn = new NeuralNetwork(flp, slp, nnBeta, nnEta);
      ArrayList<Matrix> inputs = new ArrayList<Matrix>();
      ArrayList<Matrix> outputs = new ArrayList<Matrix>();
      for (String data : dataArray)
      {
         String[] lines = data.split(",");
         double[][] matrix = new double[lines.length][1];
         for (int i = 0; i < lines.length; i++)
            matrix[i][0] = new Double(lines[i]);
         inputs.add(new Matrix(matrix));
         outputs.add(new Matrix(one));
      }
      for (int i = 0; i < dataArray.size() * 4; i++)
      {
         inputs.add(Matrix.random(50, 1));
         outputs.add(new Matrix(zero));
      }
      for (int i = 0; i < 1000; i++)
      {
         for (int j = 0; j < inputs.size(); j++)
         {
            nn.learn(inputs.get(j), outputs.get(j));
         }
      }

      byte[] flpBytes = Crypto.encrypt(nn.getAlphaMatrix().getArray(), Crypto
            .getKey());
      byte[] slpBytes = Crypto.encrypt(nn.getBetaMatrix().getArray(), Crypto
            .getKey());

      DatabaseConnector db = new DatabaseConnector("jdbc:mysql://localhost/"
            + dbname, dbuser, dbpass);
      db.setParams(userName, 1, flpBytes);
      db.setParams(userName, 2, slpBytes);
      return true;
   }
}
