package com.haulmont.testtask.views;

import com.haulmont.testtask.editwindows.EditPatientWindow;
import com.haulmont.testtask.models.Patient;
import com.haulmont.testtask.daoclasses.PatientDAO;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientTableView extends VerticalLayout implements View  {
    private PatientDAO patientDAO;
    private Grid grid;
    private List<Patient> patientList;

    public PatientTableView() {
        setMargin(true);
        setSpacing(true);

        patientList = new ArrayList<>();
        grid = new Grid("ПАЦИЕНТ");

        grid.addColumn("name").setHeaderCaption("Имя");
        grid.addColumn("surname").setHeaderCaption("Фамилия");
        grid.addColumn("patronymic").setHeaderCaption("Отчество");
        grid.addColumn("phoneNumber").setHeaderCaption("Телефон");
        grid.setWidth("1000px");
        grid.setHeight("500px");

        patientDAO = new PatientDAO();
        Button addButton = new Button("Добавить");
        Button updateButton = new Button("Изменить");
        Button deleteButton = new Button("Удалить");
        deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);

        addButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    UI.getCurrent().addWindow(new EditPatientWindow(PatientTableView.this));
                }
                catch (Exception e) {
                    Notification.show("Ошибка");
                }
            }
        });

        updateButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object selected = ((Grid.SingleSelectionModel)grid.getSelectionModel()).getSelectedRow();
                    if(selected != null) {
                        UI.getCurrent().addWindow(new EditPatientWindow((Patient) selected,
                                PatientTableView.this));
                    }
                    else {
                        Notification.show("Не выбран пациент");
                    }
                }
                catch (Exception e) {
                    Notification.show("Ошибка");
                }
            }
        });

        deleteButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object selected = ((Grid.SingleSelectionModel)grid.getSelectionModel()).getSelectedRow();
                if(selected != null) {
                    try {
                        patientDAO.deletePatient(((Patient) selected).getId());
                        updateTable();
                        grid.getSelectionModel().reset();
                    }
                    catch (SQLException ex){
                        Notification.show("Ошибка удаления");
                    }
                }
                else {
                    Notification.show("Не выбран пациент");
                }
            }
        });

        Button linkButton = new Button("Вернуться на главный экран");
        linkButton.addStyleName(ValoTheme.BUTTON_LINK);
        linkButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getUI().getNavigator().navigateTo("");
            }
        });

        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        addComponent(grid);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.addComponent(addButton);
        buttonLayout.addComponent(updateButton);
        buttonLayout.addComponent(deleteButton);

        addComponent(buttonLayout);
        addComponent(linkButton);
    }

    public void updateTable() {
        patientList = patientDAO.getAllPatients();
        grid.setContainerDataSource(new BeanItemContainer<>(Patient.class,patientList));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
     updateTable();
    }
}
