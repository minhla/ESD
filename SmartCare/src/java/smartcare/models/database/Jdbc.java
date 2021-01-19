/*
Class: Jdbc
Description: Connects to the database
Created: 05/12/2020
Updated: 06/12/2020
Author/s: Michael Tonkin, Giacomo Pellizzari.
*/


package smartcare.models.database;

import java.sql.*;
import java.util.ArrayList;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Jdbc implements ServletContextListener{

    //Singleton instance
    private static Jdbc JDBC_INSTANCE = null;

    //The URL for the database
    static final String DB_URL = "jdbc:derby://localhost:1527/SmartCare";

    //Database credentials
    static final String USER = "SmartCare";
    static final String PASS = "HSVu2G";

    private Jdbc()
    {

    }


    public static Jdbc getJdbc(){
        if(JDBC_INSTANCE == null){
            JDBC_INSTANCE = new Jdbc();
        }
        return JDBC_INSTANCE;
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
    Method: getResultList
    Description: executes a select query and returns the whole result
    Params: String column - the column you with to search
            String condition - where to stop looking for the result
            String table - the table to search
    Returns: String - result of query divided by '/n'.
    */
    public ArrayList<String> getResultList(String column, String condition, String table, int numOfColumns)
    {
        ArrayList<String> results = new ArrayList<String>();
        Statement stmt = null;
        String sql = "SELECT " + column + " FROM " + table + " WHERE " + condition;
        String result = "";
        Connection conn = this.connect();
        System.out.println(sql);

        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //ResultSetMetaData rsMetaData = rs.getMetaData();
            while (rs.next())
            {
                for(int i = 1; i <= numOfColumns; i++){
                    results.add(rs.getString(i));
                }
            }
            rs.close();
            stmt.close();
            conn.close();

        }
        catch(SQLException e)
        {
            System.out.println("Failed to execute getResultList statement");
            e.printStackTrace();
        }

        return results;
    }


    /*
    Method: getResultSet
    Description: executes a select query and returns the whole result
    Params: String column - the column you with to search
            String condition - where to stop looking for the result
            String table - the table to search
    Returns: String - result of query divided by '/n'.
    */
    @Deprecated
    public String getResultSet(String column, String condition, String table, int numOfColumns)
    {
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        String sql = "SELECT " + column + " FROM " + table + " WHERE " + condition;
        String result = "";
        Connection conn = this.connect();
        System.out.println(sql);

        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //ResultSetMetaData rsMetaData = rs.getMetaData();
            int index = 1;
            while (rs.next())
            {
                for(int i = 1; i <= numOfColumns; i++){
                    sb.append(rs.getString(i)).append(" ");
                }

                sb.append("\n<br>");
                index++;
            }
            rs.close();
            stmt.close();
            conn.close();

        }
        catch(SQLException e)
        {
            System.out.println("Failed to execute getResultSet statement");
            e.printStackTrace();
        }

        return sb.toString();
    }

        /*
    Method: getALLResultSet
    Description: get all result that macth with conditions
    Params: String column - the column you with to search
            String condition - where to stop looking for the result
            String table - the table to search
    Returns: String - result of query divided by '/n'.
    */
    @Deprecated
    public String getAllResultSet(String column, String condition, String table, int numOfColumns)
    {
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        String sql = "SELECT " + column + " FROM " + table + " WHERE " + condition;
        String result = "";
        Connection conn = this.connect();
        System.out.println(sql);

        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //ResultSetMetaData rsMetaData = rs.getMetaData();
            while (rs.next())
            {
                for(int i = 1; i <= numOfColumns; i++){
                    sb.append(rs.getString(i)).append(" ");
                }

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

        return sb.toString();
    }
    /*
    Method: getValueStmt
    Description: executes a select query
    Params: String column - the column you with to search
            String condition - where to stop looking for the result
            String table - the table to search
    Returns: String - result of query.
    */
    @Deprecated
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
    Method: addRecords
    Description: Enables to add values to a table
    Params: String table - the table to be updated (and columns)
            String data - part of statement representing data to insert
    */
    public int addRecords(String table, String data){
        int flag = 0;
        Statement stmt = null;
        String sql = "insert into " + table + " values " + data;
        System.out.println(sql);

        Connection conn = this.connect();

        try
        {
            stmt = conn.createStatement();
            flag = stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        }
        catch(SQLException e)
        {
            System.out.println("Failed to execute update table: " + table);
            e.printStackTrace();
        }
        return flag;
    }
    
   
  /*
    Method: delete
    Description: Deletes an entry in the database.
    Params: String table - the table to be updated (and columns)
            String condition - forms part of the "WHERE..." sql statement. Use to
                                select which item should be deleted
    */
    public int delete(String table, String condition){
        int flag = 0;
        Statement stmt = null;
        String sql = "DELETE FROM " + table + " WHERE " + condition;
        System.out.println(sql);

        Connection conn = this.connect();

        try
        {
            stmt = conn.createStatement();
            flag = stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        }
        catch(SQLException e)
        {
            System.out.println("Failed to execute delete from: " + table);
            e.printStackTrace();
        }
        return flag;
    }


    /*
    Description: updates an entry in the database.
    Params: String   sql query to be updated 
           
    */
    public int updateQuery(String sql){
        int flag = 0;
        Statement stmt = null;
        System.out.println(sql);

        Connection conn = this.connect();

        try
        {
            stmt = conn.createStatement();
            flag = stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        }
        catch(SQLException e)
        {
            System.out.println("Failed to execute update query. " );
            e.printStackTrace();
        }
        return flag;
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
