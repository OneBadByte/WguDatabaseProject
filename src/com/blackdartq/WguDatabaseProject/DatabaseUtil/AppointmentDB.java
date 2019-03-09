package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;

class Appointment{
    public int customerId;
    public int userId;
    public String title;

}

public class AppointmentDB implements DatabaseTemplate {
    private ArrayList<Appointment> appointments = new ArrayList<>();

    /**
     *
     */
    @Override
    public void deleteById(int id) {

    }

//    public void Vget
}
