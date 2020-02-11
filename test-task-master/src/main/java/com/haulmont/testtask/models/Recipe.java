package com.haulmont.testtask.models;

import java.sql.Date;

public class Recipe {
    private Long id;
    private String description;
    private Date date;
    private int validityPeriod;
    private String priority;
    private Doctor doctor;
    private Patient patient;

    public Recipe() {
    }

    public Recipe(Long id, String description, Date date, int validityPeriod, String priority, Doctor doctor, Patient patient) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.validityPeriod = validityPeriod;
        this.priority = priority;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(int validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
