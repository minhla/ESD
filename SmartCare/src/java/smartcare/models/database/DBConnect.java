/*
Class: DBConnect
Description: Connects to the database
Created: 05/12/2020
Updated: 05/12/2020
Author/s: Michael Tonkin.
*/


package smartcare.models.database;

import java.sql.*;
import javax.servlet.ServletContextListener;

public class DBConnect implements ServletContextListener{
    //The URL for the database
    static final String DB_URL = "jdbc:derby://localhost:1527/SmartCare";
    
    //Database credentials
    static final String USER = "SmartCare";
    static final String PASS = "HSVu2G";
    
    public DBConnect()
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
    
    
    
}
