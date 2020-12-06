/*
Class: Jdbc
Description: Connects to the database
Created: 05/12/2020
Updated: 05/12/2020
Author/s: Michael Tonkin.
*/


package smartcare.models.database;

import java.sql.*;
import javax.servlet.ServletContextListener;

public class Jdbc implements ServletContextListener{
    //The URL for the database
    static final String DB_URL = "jdbc:derby://localhost:1527/SmartCare";
    
    //Database credentials
    static final String USER = "SmartCare";
    static final String PASS = "HSVu2G";
    
    public Jdbc()
    {

    }
    
    public Connection connect()
    {      
        
        Connection conn = null;

        try
        {
            System.out.println("Attempting to connect to driver manager...");
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        catch(ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        
        return conn;
    }
   
    public String getValueStmt(String column, String condition, String table)
    {
        Statement stmt = null;
        String sql = "SELECT " + column + " FROM " + table + " WHERE " + condition;
        String result = "";
        Connection conn = this.connect();
        
        try
        {
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsMetaData = rs.getMetaData();
        if (rs.next()) 
        {
        result = rs.getString(1);
        }
        rs.close();
        stmt.close();
        conn.close();  
        
        }
        catch(SQLException e)
        {
            System.out.println("Failed to execute getValueStmt statement");
            e.printStackTrace();
        }     
        
        return result;
    }
    
    
    public boolean loginStmt(String table, String email, String password)
    {
        Statement stmt = null;
        String sql = "SELECT email, password FROM " + table;
        
        Connection conn = this.connect();
        
        try
        {
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next())
        {
            String eml = rs.getString("email");
            String pas = rs.getString("password");
            
            if (eml.equalsIgnoreCase(email) && pas.equals(password))
            {   
                System.out.println("Login credentials accepted.");
                return true;
            }
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
        }
        catch(SQLException e)
        {
            System.out.println("Failed to execute login statement");
            e.printStackTrace();
        }
        System.out.println("Login credentials not found");
        return false;
    }
}
    

