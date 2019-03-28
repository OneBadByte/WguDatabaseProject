package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                            "type = ?, url = ?, start = ?, end = ? " +
                            "WHERE appointmentId = ?;"
            );
            statement.setInt(1, appointment.customerId);
            statement.setString(2, appointment.title);
            statement.setString(3, appointment.description);
            statement.setString(4, appointment.location);
            statement.setString(5, appointment.contact);
            statement.setString(6, appointment.type);
            statement.setString(7, appointment.url);
            statement.setTimestamp(8, appointment.start);
            statement.setTimestamp(9, appointment.end);
            statement.setInt(10, appointment.appointmentId);

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
                            "?, ?, " +
                            "CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');"
            );
            statement.setInt(1, appointment.customerId);
            statement.setString(2, appointment.title);
            statement.setString(3, appointment.description);
            statement.setString(4, appointment.location);
            statement.setString(5, appointment.contact);
            statement.setString(6, appointment.type);
            statement.setString(7, appointment.url);
            statement.setTimestamp(8, appointment.start);
            statement.setTimestamp(9, appointment.end);
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
        final int START = 10;
        final int END = 11;
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
                       resultSet.getString(URL),
                       resultSet.getTimestamp(START),
                       resultSet.getTimestamp(END)
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
     * gets all the start timestamps
     */
    public ArrayList getAllStartTimestamps(){
        ArrayList<LocalDateTime> output = new ArrayList<>();
        for(Appointment appointment : appointments){
           output.add(appointment.start.toLocalDateTime());
        }
        return output;
    }

    /**
     * Checks the localDateTime passed to method is within 15 minutes
     */
    public boolean checkLocalDateTimeWithin15Minutes(LocalDateTime localDateTime){
        LocalDateTime now = LocalDateTime.now();
        if(now.getYear() != localDateTime.getYear()){
           return false;
        }
        if(now.getMonthValue() != localDateTime.getMonthValue()){
            return false;
        }
        if(now.getDayOfMonth() != localDateTime.getDayOfMonth()){
            return false;
        }
        if(now.getHour() != localDateTime.getHour()){
            return false;
        }
        int timeDelta = localDateTime.getMinute() - now.getMinute();
        if( timeDelta > 15 || timeDelta < 0 ){
            return false;
        }
        return true;
    }

    public int getMinutesTillStartFromIndex(int index){
        LocalDateTime now = LocalDateTime.now();
        return appointments.get(index).start.toLocalDateTime().getMinute() - now.getMinute();
    }

    public ArrayList getAllStartTimesWithin15Minutes(){
        ArrayList<Integer> appointmentIndexOutput = new ArrayList<Integer>();
        int count = 0;
        for(Appointment appointment : appointments){
            if(checkLocalDateTimeWithin15Minutes(appointment.start.toLocalDateTime())){
                appointmentIndexOutput.add(count);
            }
            count++;
        }
        return appointmentIndexOutput;
    }

    /**
     * gets a LocalDateTime from the given appointment index
     */

    public LocalDateTime getStartLocalDateTimeByIndex(int index){
        Appointment appointment = getAppointmentFromIndex(index);
        return appointment.start.toLocalDateTime();
    }

    public LocalDateTime getEndLocalDateTimeByIndex(int index){
        Appointment appointment = getAppointmentFromIndex(index);
        return appointment.end.toLocalDateTime();
    }

    public String getTimeFromLocalDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return localDateTime.format(formatter);
    }

    private String getDateFromLocalDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/u");
        return localDateTime.format(formatter);
    }

    public String getStartTimeFromIndex(int index){
        return getTimeFromLocalDateTime(getStartLocalDateTimeByIndex(index));
    }

    public String getStartTimeDateFromIndex(int index){
        return getDateFromLocalDateTime(getStartLocalDateTimeByIndex(index));
    }

    public String getEndTimeFromIndex(int index){
        return getTimeFromLocalDateTime(getEndLocalDateTimeByIndex(index));
    }

    public String getTitleFromIndex(int index){
        return getAppointmentFromIndex(index).title;
    }

    public String getEndTimeDateFromIndex(int index){
        return getDateFromLocalDateTime(getEndLocalDateTimeByIndex(index));
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
