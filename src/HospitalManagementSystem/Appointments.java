package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Appointments {
    private static Connection connection;

    public Appointments(Connection connection) {
        this.connection = connection;
    }

    public static void viewAppointments() {
        String query = "SELECT a.id AS appointment_id, p.name AS patient_name, d.name AS doctor_name, d.specialization, a.appointment_date " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "ORDER BY a.appointment_date";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Appointments:");
            System.out.println("+----------------+------------------+------------------+----------------------+-------------------+");
            System.out.println("| Appointment ID |   Patient Name   |   Doctor Name    |   Specialization     | Appointment Date  |");
            System.out.println("+----------------+------------------+------------------+----------------------+-------------------+");

            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("appointment_id");
                String patientName = resultSet.getString("patient_name");
                String doctorName = resultSet.getString("doctor_name");
                String specialization = resultSet.getString("specialization");
                String appointmentDate = resultSet.getString("appointment_date");

                System.out.printf("|  %-12d  |   %-14s |   %-13s  |   %-17s  |   %-14s  |\n",
                        appointmentId, patientName, doctorName, specialization, appointmentDate);
                System.out.println("+----------------+------------------+------------------+----------------------+-------------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}