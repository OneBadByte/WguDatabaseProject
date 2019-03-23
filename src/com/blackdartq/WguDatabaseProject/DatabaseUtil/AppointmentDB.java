package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppointmentDB extends DatabaseUtil implements DatabaseTemplate {
    private ArrayList<Appointment> appointments = new ArrayList<>();
    public AppointmentDB(){
        getAppointmentsFromDatabase();
    }

    /**
     * Check all start times against the current time
     */
    public boolean checkAppointmentStartTimes(){
        boolean output = false;
//        for(Appointment appointment : appointments){
//            if(appointment)
//        }
        return true;
    }

    /**
     * Modifies an appointment in the database
     */
    public void updateAppointment(Appointment appointment){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE appointment " +
                            "SET customerId = ?, title = ?, description = ?, location = ?, contact = ?, " +
                            "type = ?, url = ? " +
                            "WHERE appointmentId = ?;"
            );
            statement.setInt(1, appointment.customerId);
            statement.setString(2, appointment.title);
            statement.setString(3, appointment.description);
            statement.setString(4, appointment.location);
            statement.setString(5, appointment.contact);
            statement.setString(6, appointment.type);
            statement.setString(7, appointment.url);
            statement.setInt(8, appointment.appointmentId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't update appointment in the database");
        }
    }

    /**
     * adds an appointment to the database
     */
    public void addAppointment(Appointment appointment){
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO appointment VALUE(NULL, ?, 1, ?, ?, ?, ?, ?, ?, " +
                            "CURDATE(), CURDATE(), " +
                            "CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');"
            );
            statement.setInt(1, appointment.customerId);
            statement.setString(2, appointment.title);
            statement.setString(3, appointment.description);
            statement.setString(4, appointment.location);
            statement.setString(5, appointment.contact);
            statement.setString(6, appointment.type);
            statement.setString(7, appointment.url);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't add appointment to the database");
        }
        getAppointmentsFromDatabase();
    }

    /**
     * gets all the appointments from the database
     */
    public void getAppointmentsFromDatabase(){
        final int APPOINTMENT_ID = 1;
        final int CUSTOMER_ID = 2;
        final int USER_ID = 3;
        final int TITLE = 4;
        final int DESCRIPTION = 5;
        final int LOCATION = 6;
        final int CONTACT = 7;
        final int TYPE = 8;
        final int URL = 9;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM appointment;"
            );
            ResultSet resultSet = statement.executeQuery();
            appointments.clear();
            while(resultSet.next()){
               appointments.add(new Appointment(
                       resultSet.getInt(APPOINTMENT_ID),
                       resultSet.getInt(CUSTOMER_ID),
                       resultSet.getInt(USER_ID),
                       resultSet.getString(TITLE),
                       resultSet.getString(DESCRIPTION),
                       resultSet.getString(LOCATION),
                       resultSet.getString(CONTACT),
                       resultSet.getString(TYPE),
                       resultSet.getString(URL)
               ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("couldn't get appointments from the database");
        }
    }

    /**
     * Get the appointment id from appointments by index
     */
    public int getAppointmentIdFromIndex(int index){
        return appointments.get(index).appointmentId;
    }
    /**
     * Get the appointment id from appointments by index
     */
    public Appointment getAppointmentFromIndex(int index){
        return appointments.get(index);
    }

    /**
     * gets the titles of all the appointments in the database
     */
    public ArrayList getAppointmentTitles(){
        ArrayList<String> output = new ArrayList<>();
        for(Appointment appointment : appointments){
            output.add(appointment.title);
        }
        return output;
    }

    /**
     *  deletes an appointment by it's index in appointments
     */
    public void deleteByIndex(int index){
        deleteById(getAppointmentIdFromIndex(index));
    }

    /**
     * Deletes an appointment from the database using the appointments ID
     */
    @Override
    public void deleteById(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM appointment WHERE appointmentId = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't delete appointment from database");
        }
    }

}
