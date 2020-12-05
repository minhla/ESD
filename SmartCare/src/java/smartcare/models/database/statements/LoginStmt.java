/*
Class: DBConnect
Description: Attempt to find a username and password in the database
Created: 05/12/2020
Updated: 05/12/2020
Author/s: Michael Tonkin.
*/
package smartcare.models.database.statements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import smartcare.models.database.DBConnect;

public class LoginStmt {
    
    DBConnect db = new DBConnect();
    
    public boolean execute(String table, String email, String password)
    {
        Statement stmt = null;
        String sql = "SELECT email, password FROM " + table;
        
        Connection conn = db.connect();
        
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
