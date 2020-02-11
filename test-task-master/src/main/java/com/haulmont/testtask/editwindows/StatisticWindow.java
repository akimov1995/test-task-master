package com.haulmont.testtask.editwindows;

import com.haulmont.testtask.models.Doctor;
import com.haulmont.testtask.daoclasses.DoctorDAO;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.util.Map;

public class StatisticWindow extends Window {

    public StatisticWindow() {
        super();
        center();
        setModal(true);
        setResizable(false);

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        Grid grid = new Grid("Статистика");
        grid.setWidth("900px");
        grid.setHeight("600px");
        grid.addColumn("doctor").setHeaderCaption("Врач");
        grid.addColumn("count").setHeaderCaption("Кол-во рецептов");

        DoctorDAO doctorDAO = new DoctorDAO();
        Map<Doctor, Integer> statisticMap = doctorDAO.getStatistic();

        for(Map.Entry entry: statisticMap.entrySet()){
            grid.addRow(((Doctor)entry.getKey()).getCaption(),entry.getValue().toString());
        }

        layout.addComponent(grid);
        setContent(layout);
    }
}
