package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "**********";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctors doctors = new Doctors(connection);
            Appointments appointments = new Appointments(connection);
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1.Add Patient");
                System.out.println("2.View Patient");
                System.out.println("3.View Doctors");
                System.out.println("4.Book Appointment");
                System.out.println("5.View Appointments");
                System.out.println("6.Exit");
                System.out.println("Enter Your Choice: ");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        doctors.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient, doctors, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        Appointments.viewAppointments();
                        System.out.println();
                        break;
                    case 6:
                        System.out.println("THANK YOU,,,!!! ");
                        System.out.println("SEE YOU SOON,,,!!!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice!!!");
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static void bookAppointment(Patient patient, Doctors doctors, Connection connection, Scanner scanner){
        System.out.println("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if (patient.getPatientById(patientId) && doctors.getDoctorById(doctorId)){
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date)VALUES(?, ?, ?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected>0){
                        System.out.println("Appointment Booked!!");
                    }else {
                        System.out.println("Failed to Book Appointment!!!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor not available on this date!!!");
            }
        }else {
            System.out.println("Either Doctor or Patient doesn't exits!!! ");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? ";
        try{
            PreparedStatement preparedStatement =connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int count = resultSet.getInt(1);
                if (count==0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
