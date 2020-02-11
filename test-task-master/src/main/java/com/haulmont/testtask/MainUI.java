package com.haulmont.testtask;

import com.haulmont.testtask.views.*;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        Navigator navigator = new Navigator(this,this);
        navigator.addView("",new StartView());
        navigator.addView("doctor", new DoctorTableView());
        navigator.addView("patient", new PatientTableView());
        navigator.addView("recipe", new RecipeTableView());
    }

}