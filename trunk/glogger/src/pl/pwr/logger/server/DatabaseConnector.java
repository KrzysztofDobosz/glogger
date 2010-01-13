package pl.pwr.logger.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Blob;

public class DatabaseConnector
{
   private String url, login, password;
   private Connection conn;

   public DatabaseConnector(String dbUrl, String dbLogin, String dbPassword)
   {
      url = dbUrl;
      login = dbLogin;
      password = dbPassword;
   }

   private void connect() throws SQLException, ClassNotFoundException
   {
      try
      {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(url, login, password);
      }
      catch (SQLException e)
      {
         e.printStackTrace();
         throw e;
      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
         throw e;
      }
   }

   private void disconnect() throws SQLException
   {
      try
      {
         if (conn != null)
            conn.close();
      }
      catch (SQLException e)
      {
         e.printStackTrace();
         throw e;
      }
   }

   public byte[] getParams(String userLogin, int layerNr)
   {
      byte[] res =
      {};
      try
      {
         connect();

         Statement db = conn.createStatement();
         String query = "";
         if (layerNr == 1)
            query += "SELECT first_layer_params FROM `User` WHERE login='"
                  + userLogin + "';";
         else
            query += "SELECT second_layer_params FROM `User` WHERE login='"
                  + userLogin + "';";

         ResultSet result = db.executeQuery(query);
         if (result.next())
         {
            Blob b = (Blob) result.getBlob(1);
            res = b.getBytes(1, (int) b.length());
         }

         disconnect();
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
      }

      return res;
   }

   public void setParams(String userLogin, int layerNr, byte[] params)
   {
      try
      {
         connect();

         Statement db = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_UPDATABLE);
         String query = "";
         query += "SELECT * FROM `User` WHERE login='" + userLogin + "';";
         ResultSet rs = db.executeQuery(query);
         if (rs.next())
         {
            if (layerNr == 1)
            {
               Blob b = (Blob) rs.getBlob("first_layer_params");
               b.truncate(0);
               b.setBytes(1, params);
               rs.updateBlob("first_layer_params", b);
            }
            else
            {
               Blob b = (Blob) rs.getBlob("second_layer_params");
               b.truncate(0);
               b.setBytes(1, params);
               rs.updateBlob("second_layer_params", b);
            }
            rs.updateRow();
         }

         disconnect();
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
      }
   }

}
