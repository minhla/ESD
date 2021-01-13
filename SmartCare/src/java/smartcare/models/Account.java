/*
Class: Account
Description: Used in generating hashed passwords
Created: 29/12/2020
Updated: 29/12/2020
Author/s: Michael Tonkin
*/
package smartcare.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import smartcare.models.database.Jdbc;

public class Account 
{
    
    /*
    Method: generatePasswordHash
    Description: Generates an MD5 hash of a given string (should be used for passwords).
    */
    public String generatePasswordHash(String plaintext)
    {
        String ciphertext = null;
        System.out.println("Encrypting " + plaintext);
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plaintext.getBytes());
            
            byte[] bytes = md.digest();
            
            StringBuilder sb = new StringBuilder();
            for( int i = 0; i < bytes.length; i++ )
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                ciphertext = sb.toString();
            }
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        
        return ciphertext;
    }

    /*
    Method: loginStmt
    Description: a prebuilt statement to be used in conjunction with the login system.
    Will search database for login credentials.
    Params: String table - the table to be searched
    String email - The user's email.
    String password - the user's password.
     */
    public boolean loginStmt(String table, String email, String password, Jdbc jdbc) {
        Statement stmt = null;
        String sql = "SELECT email, password FROM " + table;
        Connection conn = jdbc.connect();
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(password);
            password = generatePasswordHash(password);
            System.out.println(email);
            System.out.println("pas = " + password);
            while (rs.next()) {
                String eml = rs.getString("email");
                System.out.println(eml);
                String pas = rs.getString("password");
                System.out.println(pas);
                if (eml.equalsIgnoreCase(email) && pas.equals(password)) {
                    System.out.println("Login credentials accepted.");
                    return true;
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Failed to execute login statement");
            e.printStackTrace();
        }
        System.out.println("Login credentials not found");
        return false;
    }
    
}
