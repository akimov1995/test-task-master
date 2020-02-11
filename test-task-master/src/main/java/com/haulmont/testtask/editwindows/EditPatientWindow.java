package com.haulmont.testtask.editwindows;

import com.haulmont.testtask.models.Patient;
import com.haulmont.testtask.daoclasses.PatientDAO;
import com.haulmont.testtask.views.PatientTableView;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

import java.sql.SQLException;

public class EditPatientWindow  extends Window {
    private TextField patientName;
    private TextField patientSurname;
    private TextField patientPatronymic;
    private TextField phoneNumber;
    private Button okButton;

    public EditPatientWindow(PatientTableView view) {
        super();
        center();
        setModal(true);
        setClosable(false);
        setResizable(false);

        FormLayout content = getLayout();

        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    if(!patientName.isEmpty()&&patientName.isValid()&&!patientSurname.isEmpty()
                            &&patientSurname.isValid()&&!patientPatronymic.isEmpty()&&
                            patientPatronymic.isValid()&&!phoneNumber.isEmpty()&&phoneNumber.isValid()) {

                        PatientDAO patientDAO = new PatientDAO();
                        Patient patient = new Patient();
                        patient.setName(patientName.getValue());
                        patient.setSurname(patientSurname.getValue());
                        patient.setPatronymic(patientPatronymic.getValue());
                        patient.setPhoneNumber(phoneNumber.getValue());
                        patientDAO.addPatient(patient);
                        view.updateTable();
                        close();
                    }
                    else {
                        Notification.show("Неправильный ввод");
                    }
                }
                catch (SQLException ex) {
                    Notification.show("Ошибка");
                }
            }
        });

        setContent(content);
    }

    public EditPatientWindow(Patient patient, PatientTableView view){
        super();
        center();
        setModal(true);
        setClosable(false);
        setResizable(false);

        FormLayout content = getLayout();
        patientName.setValue(patient.getName());
        patientSurname.setValue(patient.getSurname());
        patientPatronymic.setValue(patient.getPatronymic());
        phoneNumber.setValue(patient.getPhoneNumber());

        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    if(!patientName.isEmpty()&&patientName.isValid()&&!patientSurname.isEmpty()
                            &&patientSurname.isValid()&&!patientPatronymic.isEmpty()&&
                            patientPatronymic.isValid()&&!phoneNumber.isEmpty()&&phoneNumber.isValid()) {

                        PatientDAO patientDAO = new PatientDAO();
                        patient.setName(patientName.getValue());
                        patient.setSurname(patientSurname.getValue());
                        patient.setPatronymic(patientPatronymic.getValue());
                        patient.setPhoneNumber(phoneNumber.getValue());
                        patientDAO.updatePatient(patient);
                        view.updateTable();
                        close();
                    }
                    else {
                       Notification.show("Неправильный ввод");
                    }
                }
                catch (SQLException ex) {
                    Notification.show("Ошибка");
                }
            }
        });

        setContent(content);
    }

    private FormLayout getLayout(){
        RegexpValidator nameValidator = new RegexpValidator("^([А-Я]{1}[а-яё]" +
                "{1,24}|[A-Z]{1}[a-z]{1,24})$", "Ошибка ввода");

        patientName = new TextField("Имя");
        patientName.setWidth("370px");
        patientName.addValidator(nameValidator);
        patientSurname = new TextField("Фамилия");
        patientSurname.setWidth("370px");
        patientSurname.addValidator(nameValidator);

        patientPatronymic = new TextField("Отчество");
        patientPatronymic.setWidth("370px");
        patientPatronymic.addValidator(nameValidator);
        phoneNumber = new TextField("Номер телефона");
        phoneNumber.setWidth("370px");

        RegexpValidator phoneValidator = new RegexpValidator("^((8|\\+7)[\\- ]?)" +
                "?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", "Ошибка ввода");
        phoneNumber.addValidator(phoneValidator);

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        okButton = new Button("Ок");
        Button addButton = new Button("Отмена");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });

        layout.addComponent(okButton);
        layout.addComponent(addButton);

        FormLayout content = new FormLayout(patientName,patientSurname,patientPatronymic,phoneNumber,layout);
        content.setMargin(true);
        content.setSpacing(true);
        content.setSizeUndefined();
        return content;
    }

}