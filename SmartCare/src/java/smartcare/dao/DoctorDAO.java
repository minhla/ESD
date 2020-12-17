package smartcare.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import smartcare.models.User;

/**
 *
 * @author minh-la
 */
public class DoctorDAO {
    
    public int registerDoctor(User doctor) throws ClassNotFoundException {

    String INSERT_USER_SQL = "INSERT INTO USERS "
            + " (uuid, firstname, lastname, usertype, dob, phone, email, address, password) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    int result = 0;

    Class.forName ("com.mysql.jdbc.Driver");
    
    try (Connection connection = DriverManager
            .getConnection("jdbc:derby://localhost:1527/SmartCare", "SmartCare", "HSVu2G");

    PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
    preparedStatement.setString(1, doctor.getUserID());
    
    }
    catch (SQLException e) {
    printSQLException(e);
    }
    return result;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}


