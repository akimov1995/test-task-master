package com.haulmont.testtask.views;

import com.haulmont.testtask.editwindows.EditDoctorWindow;
import com.haulmont.testtask.editwindows.StatisticWindow;
import com.haulmont.testtask.models.Doctor;
import com.haulmont.testtask.daoclasses.DoctorDAO;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorTableView extends VerticalLayout implements View {
    private DoctorDAO doctorDAO;
    private Grid grid;
    private List<Doctor> doctorList;

    public DoctorTableView() {
        setMargin(true);
        setSpacing(true);
        doctorList = new ArrayList<>();
        grid = new Grid("ВРАЧ");

        grid.addColumn("name").setHeaderCaption("Имя");
        grid.addColumn("surname").setHeaderCaption("Фамилия");
        grid.addColumn("patronymic").setHeaderCaption("Отчество");
        grid.addColumn("specialization").setHeaderCaption("Специализация");
        grid.setWidth("1000px");
        grid.setHeight("500px");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        doctorDAO = new DoctorDAO();
        //updateTable();
        Button addButton = new Button("Добавить");
        Button updateButton = new Button("Изменить");
        Button deleteButton = new Button("Удалить");
        Button statisticButton = new Button("Показать статистику");

        deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);

        addButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    UI.getCurrent().addWindow(new EditDoctorWindow(DoctorTableView.this));
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        updateButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object selected = ((Grid.SingleSelectionModel)grid.getSelectionModel()).getSelectedRow();
                    if(selected == null) {
                        Notification.show("Не выбран врач");
                    }
                    else {
                        UI.getCurrent().addWindow(new EditDoctorWindow((Doctor)selected, DoctorTableView.this));
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
                        doctorDAO.deleteDoctor(((Doctor) selected).getId());
                        updateTable();
                        grid.getSelectionModel().reset();
                    }
                    catch (SQLException e)
                    {
                        Notification.show("Ошибка удаления");
                    }
                }
                else {
                    Notification.show("Не выбран врач");
                }
            }
        });

        statisticButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    UI.getCurrent().addWindow(new StatisticWindow());
                }
                catch (Exception e) {
                    Notification.show("Ошибка");
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

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(grid);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.addComponent(addButton);
        buttonLayout.addComponent(updateButton);
        buttonLayout.addComponent(deleteButton);
        buttonLayout.addComponent(statisticButton);

        addComponent(buttonLayout);
        addComponent(linkButton);
    }

    public void updateTable() {
        doctorList = doctorDAO.getAllDoctors();
        grid.setContainerDataSource(new BeanItemContainer<>(Doctor.class,doctorList));
        grid.recalculateColumnWidths();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        updateTable();
    }
}
