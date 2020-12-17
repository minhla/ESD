package smartcare.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import smartcare.models.Doctor;

/**
 *
 * @author minh-la
 */
public class DoctorDAO {

    public int registerDoctor(Doctor doctor) throws ClassNotFoundException {

        String INSERT_USER_SQL = "INSERT INTO USERS "
                + " (uuid, firstname, lastname, usertype, dob, phone, email, address, password) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int result = 0;

        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager
                .getConnection("jdbc:derby://localhost:1527/SmartCare", "SmartCare", "HSVu2G");
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, doctor.getDoctorID());
            preparedStatement.setString(2, doctor.getFirstName());
            preparedStatement.setString(3, doctor.getLastName());
            preparedStatement.setString(4, doctor.getUserType());
            preparedStatement.setString(5, doctor.getDOB());
            preparedStatement.setString(6, doctor.getPhone());
            preparedStatement.setString(7, doctor.getEmail());
            preparedStatement.setString(8, doctor.getAddress());
            preparedStatement.setString(9, doctor.getPassword());

            System.out.println(preparedStatement);
            
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
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
