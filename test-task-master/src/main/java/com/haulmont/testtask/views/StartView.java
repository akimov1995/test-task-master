package com.haulmont.testtask.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class StartView extends VerticalLayout implements View {

    public StartView() {
        setMargin(true);
        setSpacing(true);

        Panel viewPanel = new Panel("Выберите таблицу");
        Button doctorButton = new Button("Врач");
        Button patientButton = new Button("Пациент");
        doctorButton.addStyleName(ValoTheme.BUTTON_LINK);
        patientButton.addStyleName(ValoTheme.BUTTON_LINK);
        Button recipeButton = new Button("Рецепт");
        recipeButton.addStyleName(ValoTheme.BUTTON_LINK);

        doctorButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    getUI().getNavigator().navigateTo("doctor");
                }
                catch (Exception e) {
                    Notification.show("Ошибка");
                }
            }
        });

        patientButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    getUI().getNavigator().navigateTo("patient");
                }
                catch (Exception e) {
                    Notification.show("Ошибка");
                }
            }
        });

        recipeButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    getUI().getNavigator().navigateTo("recipe");
                }
                catch (Exception e) {
                    Notification.show("Ошибка");
                }
            }
        });

        setDefaultComponentAlignment(Alignment.TOP_LEFT);
        VerticalLayout verticalLayout=new VerticalLayout();
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        viewPanel.setWidth("180px");
        viewPanel.setHeight("210px");
        verticalLayout.addComponent(patientButton);
        verticalLayout.addComponent(doctorButton);
        verticalLayout.addComponent(recipeButton);
        viewPanel.setContent(verticalLayout);
        addComponent(viewPanel);
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
