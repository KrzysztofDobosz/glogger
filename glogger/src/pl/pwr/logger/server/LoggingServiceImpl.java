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
      /*
       * double[][] v1 = { { 1., 2. }, { 3., 4. }, { 5., 6. } }; Matrix alphaM =
       * new Matrix(v1); double[][] v2 = { { 3., 2., 1. } }; Matrix betaM = new
       * Matrix(v2); double[][] v3 = { { 1. }, { 0. } }; Matrix input1 = new
       * Matrix(v3); double[][] v4 = { { 1. }, { 1. } }; Matrix input2 = new
       * Matrix(v4); double[][] v5 = { { 0. }, { 1. } }; Matrix input3 = new
       * Matrix(v5); double[][] v6 = { { 0. }, { 0. } }; Matrix input4 = new
       * Matrix(v6);
       * 
       * double[][] v7 = { { 1. } }; Matrix out1 = new Matrix(v7); double[][] v8
       * = { { 1. } }; Matrix out2 = new Matrix(v8); double[][] v9 = { { 0. } };
       * Matrix out3 = new Matrix(v9); double[][] v10 = { { 0. } }; Matrix out4
       * = new Matrix(v10);
       * 
       * NeuralNetwork nn = new NeuralNetwork(alphaM, betaM, 1.0, 0.6); Matrix
       * res = nn.getOutput(input1); res.print(1, 10); res =
       * nn.getOutput(input2); res.print(1, 10); res = nn.getOutput(input3);
       * res.print(1, 10); res = nn.getOutput(input4); res.print(1, 10);
       * 
       * for (int i = 0; i < 10000; i++) { nn.learn(input1, out1);
       * nn.learn(input2, out2); nn.learn(input3, out3); nn.learn(input4, out4);
       * }
       * 
       * res = nn.getOutput(input1); res.print(1, 10); res =
       * nn.getOutput(input2); res.print(1, 10); res = nn.getOutput(input3);
       * res.print(1, 10); res = nn.getOutput(input4); res.print(1, 10);
       * 
       * System.out.println(data); String[] lines = data.split(","); double[]
       * values = new double[lines.length]; double[][] matrix = { values };
       * 
       * for (int i = 0; i < lines.length; i++) values[i] = new
       * Double(lines[i]); System.out.println("asdasfd " + values.length);
       * 
       * byte[] m = Crypto.encrypt(matrix, Crypto.getKey());
       * 
       * DatabaseConnector db = new DatabaseConnector(
       * "jdbc:mysql://localhost/gloggerdb", "gloggeruser", "gloggerpass");
       * db.setParams("krzycho", 2, m); double[][] mm =
       * Crypto.decrypt(db.getParams("krzycho", 2), Crypto .getKey()); for (int
       * i = 0; i < mm.length; i++) for (int j = 0; j < mm[i].length; j++)
       * System.out.println(mm[i][j]);
       */
      // return null;

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
