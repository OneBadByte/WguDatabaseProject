package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getStart() {
        return start.toLocalDateTime();
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end.toLocalDateTime();
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }
}
