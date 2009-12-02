package pl.pwr.logger.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
      } catch (SQLException e)
      {
         e.printStackTrace();
         throw e;
      } catch (ClassNotFoundException e)
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
      } catch (SQLException e)
      {
         e.printStackTrace();
         throw e;
      }
   }
   
   public String getParams(String userLogin, int layerNr)
   {
      String res = "";
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
         result.next();
         res += result.getString(1);
         
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
   
   public void setParams(String userLogin, int layerNr, String params)
   {
      try
      {
         connect();
         
         Statement db = conn.createStatement();
         String query = "";
         if (layerNr == 1)
            query += "UPDATE `User` SET first_layer_params='"
               + params + "' WHERE login='" + userLogin + "';";
         else
            query += "UPDATE `User` SET second_layer_params='"
               + params + "' WHERE login='" + userLogin + "';";
         
         db.executeUpdate(query);
         
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
