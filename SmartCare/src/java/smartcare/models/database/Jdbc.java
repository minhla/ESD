/*
Class: Jdbc
Description: Connects to the database
Created: 05/12/2020
Updated: 06/12/2020
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
    
    /*
    Method: connect
    Description: provides a connection to the database. Should be called in any
                 sql statement method.
    Params: none
    Returns: Connection - a connection to the database
    */
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
   
    /*
    Method: getValueStmt
    Description: executes a select query
    Params: String column - the column you with to search
            String condition - where to stop looking for the result
            String table - the table to search
    Returns: String - result of query.
    */
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
    
    
    /*
    Method: loginStmt
    Description: a prebuilt statement to be used in conjunction with the login system.
                Will search database for login credentials.
    Params: String table - the table to be searched
            String email - The user's email.
            String password - the user's password.
    */
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
            
            if (eml.equalsIgnoreCase(email) && pas.equalsIgnoreCase(password))
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
    

