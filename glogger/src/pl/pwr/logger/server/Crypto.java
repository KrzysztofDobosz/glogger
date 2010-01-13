package pl.pwr.logger.server;

import java.security.Key;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto
{
   public static byte[] encrypt(double[][] matrix, Key key)
   {
      String plain = "";
      for (int i = 0; i < matrix.length; i++)
         for (int j = 0; j < matrix[i].length; j++)
            if (j == matrix[i].length - 1)
               plain += matrix[i][j] + ":";
            else
               plain += matrix[i][j] + ";";

      byte[] encrypted = plain.getBytes();

      Cipher c;
      try
      {

         c = Cipher.getInstance("AES");
         c.init(Cipher.ENCRYPT_MODE, key);

         return c.doFinal(encrypted);

      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return encrypted;
   }

   public static double[][] decrypt(byte[] encrypted, Key key)
   {
      String decrypted = "";
      Cipher c;
      try
      {
         c = Cipher.getInstance("AES");

         c.init(Cipher.DECRYPT_MODE, key);

         decrypted = new String(c.doFinal(encrypted));

      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      String[] lines = decrypted.split(":");

      ArrayList<ArrayList<String>> strMatrix = new ArrayList<ArrayList<String>>();

      for (int i = 0; i < lines.length; i++)
      {
         String[] line = lines[i].split(";");
         ArrayList<String> arrLine = new ArrayList<String>();

         for (int j = 0; j < line.length; j++)
         {
            arrLine.add(line[j]);
         }
         strMatrix.add(arrLine);

      }

      double[][] matrix = new double[strMatrix.size()][strMatrix.get(0).size()];

      for (int i = 0; i < matrix.length; i++)
         for (int j = 0; j < matrix[i].length; j++)
            matrix[i][j] = new Double(strMatrix.get(i).get(j));

      return matrix;
   }

   public static Key getKey()
   {
      byte[] keyspec = new byte[16];
      for (byte b : keyspec)
         b = 'a';

      return new SecretKeySpec(keyspec, "AES");
   }

}
