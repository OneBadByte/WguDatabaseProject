package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import java.sql.Time;
import java.sql.Timestamp;

public class Appointment{
    public int appointmentId;
    public int customerId;
    public int userId;
    public String title;
    public String description;
    public String location;
    public String contact;
    public String type;
    public String url;
    public Timestamp start;
    public Timestamp end;
    // TODO add start and end date times

    public Appointment(){}

    public Appointment(int appointmentId, int customerId, int userId, String title, String description,
                       String location, String contact, String type,
                       String url, Timestamp start, Timestamp end) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
    }
}
