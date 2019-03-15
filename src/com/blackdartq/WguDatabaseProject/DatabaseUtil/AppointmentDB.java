package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class Appointment{
    public int appointmentId;
    public int customerId;
    public int userId;
    public String title;
    public String description;
    public String location;
    public String contact;
    public String type;
    public String url;
    // TODO add start and end date times


    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
    }
}

public class AppointmentDB extends DatabaseUtil implements DatabaseTemplate {
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public AppointmentDB(){
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

    public int getAppointmentIdFromIndex(int index){
        return appointments.get(index).appointmentId;
    }

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
