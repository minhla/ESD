/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import smartcare.models.database.Jdbc;

/**
 *
 * @author Michael
 */
public class LoginModel {
    
    private Jdbc jdbc = Jdbc.getJdbc();;
        
/*
    Method: loginStmt
    Description: a prebuilt statement to be used in conjunction with the login system.
                Will search database for login credentials.
    Params: String table - the table to be searched
            String email - The user's email.
            String password - the user's password.
    */
    public boolean loginStmt(String username, String password)
    {
        Statement stmt = null;
        String sql = "SELECT username, password FROM USERS";
        
        Connection conn = jdbc.connect();
        
        try
        {
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next())
        {
            String eml = rs.getString("username");
            String pas = rs.getString("password");
            
            if (eml.equalsIgnoreCase(username) && pas.equals(password))
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
