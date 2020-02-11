package com.haulmont.testtask.models;

public class Doctor {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String specialization;

    public Doctor() {
    }

    public Doctor(Long id, String name, String surname, String patronymic, String specialization) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

   public String getCaption() {
        return name+" "+surname+" "+patronymic;
   }
}
