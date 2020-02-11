package com.haulmont.testtask.editwindows;

import com.haulmont.testtask.models.Doctor;
import com.haulmont.testtask.daoclasses.DoctorDAO;
import com.haulmont.testtask.views.DoctorTableView;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import java.sql.SQLException;

public class EditDoctorWindow extends Window {
    private TextField doctorName;
    private TextField doctorSurname;
    private TextField doctorPatronymic;
    private TextField specialization;
    private Button okButton;

    public EditDoctorWindow(DoctorTableView view) {
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
                    if(doctorName.isValid()&&!doctorName.isEmpty()&&!doctorSurname.isEmpty()&&doctorSurname.isValid()
                            &&!doctorPatronymic.isEmpty()&&doctorPatronymic.isValid()&&
                            !specialization.isEmpty()&&specialization.isValid()) {

                        DoctorDAO doctorDAO = new DoctorDAO();
                        Doctor newDoctor = new Doctor();
                        newDoctor.setName(doctorName.getValue());
                        newDoctor.setSurname(doctorSurname.getValue());
                        newDoctor.setPatronymic(doctorPatronymic.getValue());
                        newDoctor.setSpecialization(specialization.getValue());
                        doctorDAO.addDoctor(newDoctor);
                        view.updateTable();
                        close();
                    }
                    else {
                        Notification.show("Неправильный ввод");
                    }
                }
                catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        setContent(content);
    }

    public EditDoctorWindow(Doctor doctor, DoctorTableView view){
        super();
        center();
        setModal(true);
        setClosable(false);
        setResizable(false);

        FormLayout content = getLayout();
        doctorName.setValue(doctor.getName());
        doctorSurname.setValue(doctor.getSurname());
        doctorPatronymic.setValue(doctor.getPatronymic());
        specialization.setValue(doctor.getSpecialization());

        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    if(doctorName.isValid()&&!doctorName.isEmpty()&&doctorSurname.isValid()&&!doctorSurname.isEmpty()&&
                            doctorPatronymic.isValid()&&!doctorPatronymic.isEmpty()
                            &&!specialization.isEmpty()&&specialization.isValid()) {

                        DoctorDAO doctorDAO = new DoctorDAO();
                        Doctor newDoctor = new Doctor();
                        newDoctor.setId(doctor.getId());
                        newDoctor.setName(doctorName.getValue());
                        newDoctor.setSurname(doctorSurname.getValue());
                        newDoctor.setPatronymic(doctorPatronymic.getValue());
                        newDoctor.setSpecialization(specialization.getValue());
                        doctorDAO.updateDoctor(newDoctor);
                        view.updateTable();
                        close();
                    }
                    else {
                        Notification.show("Неправильный ввод");
                    }
                }
                catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        setContent(content);
    }

    private FormLayout getLayout(){
        doctorName = new TextField("Имя");
        doctorName.setWidth("370px");

        RegexpValidator nameValidator = new RegexpValidator("^([А-Я]{1}[а-яё]{1,24}|[A-Z]{1}[a-z]{1,24})$",
                "Ошибка ввода");

        RegexpValidator specValidator = new RegexpValidator("^([А-ЯЁа-яё]{1,30}|[A-Za-z]{4,30})$",
                "Ошибка ввода");

        doctorName.addValidator(nameValidator);
        doctorSurname = new TextField("Фамилия");
        doctorSurname.setWidth("370px");
        doctorSurname.addValidator(nameValidator);

        doctorPatronymic = new TextField("Отчество");
        doctorPatronymic.setWidth("370px");
        doctorPatronymic.addValidator(nameValidator);

        specialization = new TextField("Специализация");
        specialization.setWidth("370px");
        specialization.addValidator(specValidator);

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

        FormLayout content = new FormLayout(doctorName,doctorSurname,doctorPatronymic,specialization,layout);
        content.setMargin(true);
        content.setSpacing(true);
        content.setSizeUndefined();
        return content;
    }

}

