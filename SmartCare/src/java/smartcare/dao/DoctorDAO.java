package smartcare.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import smartcare.models.database.Jdbc;
import java.sql.Connection;
import smartcare.models.Doctor;

/**
 *
 * @author minh-la
 */
public class DoctorDAO {

    public void registerDoctor(Doctor doctor) throws ClassNotFoundException {
        
        String INSERT_USER_SQL = "INSERT INTO USERS "
                + " (uuid, firstname, lastname, usertype, dob, phone, email, address, password) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Jdbc jdbc = new Jdbc();
//'dcotorname', 'stirng', 191, 
        jdbc.addRecords("USERS (firstname, lastname, usertype, dob, phone, email, address, password)", 
                "('" + doctor.getFirstName()+ "', "
                +"'"    + doctor.getLastName()+"',"
                +"'"    + doctor.getUserType()+"',"
                +"'"    + doctor.getDOB()+"',"
                +"'"    + doctor.getPhone()+"',"
                +"'"    + doctor.getEmail()+"',"
                +"'"    + doctor.getAddress()+"',"
                +"'"    + doctor.getPassword() +"')"
        );
//        try (Connection connection = DriverManager
//                .getConnection("jdbc:derby://localhost:1527/SmartCare", "SmartCare", "HSVu2G");
//                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
//            preparedStatement.setString(1, doctor.getDoctorID());
//            preparedStatement.setString(2, doctor.getFirstName());
//            preparedStatement.setString(3, doctor.getLastName());
//            preparedStatement.setString(4, doctor.getUserType());
//            preparedStatement.setString(5, doctor.getDOB());
//            preparedStatement.setString(6, doctor.getPhone());
//            preparedStatement.setString(7, doctor.getEmail());
//            preparedStatement.setString(8, doctor.getAddress());
//            preparedStatement.setString(9, doctor.getPassword());
//
//            System.out.println(preparedStatement);
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
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
