package com.haulmont.testtask.views;

import com.haulmont.testtask.daoclasses.PatientDAO;
import com.haulmont.testtask.daoclasses.RecipeDAO;
import com.haulmont.testtask.editwindows.EditRecipeWindow;
import com.haulmont.testtask.models.Doctor;
import com.haulmont.testtask.models.Patient;
import com.haulmont.testtask.models.Recipe;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecipeTableView extends VerticalLayout implements View {
    private RecipeDAO doctorDAO;
    private Grid grid;
    private List<Recipe> doctorList;
    private TextField filterDescription;
    private ComboBox priorityBox;
    private ComboBox patientBox;
    private boolean isFilter;
    private String filterPriority;
    private Long filterId;
    private String desc;
    private int filterNum;

    public RecipeTableView() {
        setMargin(true);
        setSpacing(true);
        doctorList = new ArrayList<>();
        grid = new Grid("РЕЦЕПТ");
        doctorDAO = new RecipeDAO();
        isFilter = false;

        grid.addColumn("description").setHeaderCaption("Описание");
        grid.addColumn("patient").setHeaderCaption("Пациент");
        grid.addColumn("doctor").setHeaderCaption("Доктор");
        grid.addColumn("date").setHeaderCaption("Дата");
        grid.addColumn("validityPeriod").setHeaderCaption("Срок действия");
        grid.addColumn("priority").setHeaderCaption("Приоритет");
        grid.setWidth("1000px");
        grid.setHeight("500px");

        Button addButton = new Button("Добавить");
        Button updateButton = new Button("Изменить");
        Button deleteButton = new Button("Удалить");
        deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);

        addButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    UI.getCurrent().addWindow(new EditRecipeWindow(RecipeTableView.this));
                }
                catch (Exception e) {
                    Notification.show("Произошла ошибка");
                }
            }
        });

        updateButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object selected = ((Grid.SingleSelectionModel)grid.getSelectionModel()).getSelectedRow();
                    if(selected == null) {
                        Notification.show("Не выбран рецепт");
                    }
                    else {
                        UI.getCurrent().addWindow(new EditRecipeWindow
                                (RecipeTableView.this, (Recipe)selected));
                        }
                    }
                    catch (Exception e) {
                        Notification.show("Произошла ошибка");
                    }
                }
            });

            deleteButton.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    Object selected = ((Grid.SingleSelectionModel)grid.getSelectionModel()).getSelectedRow();
                    if(selected != null) {
                        try {
                            doctorDAO.deleteRecipe(((Recipe) selected).getId());
                            updateTable();
                            grid.getSelectionModel().reset();
                        }
                        catch (SQLException ex) {
                            Notification.show("Произошла ошибка");
                        }
                    }
                    else {
                        Notification.show("Не выбран врач");
                    }
                }
            });

            Button gridName = new Button("Вернуться на главный экран");
            gridName.addStyleName(ValoTheme.BUTTON_LINK);
            gridName.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    getUI().getNavigator().navigateTo("");
                }
            });
            setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            addComponent(grid);


            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setSpacing(true);
            horizontalLayout.addComponent(addButton);
            horizontalLayout.addComponent(updateButton);
            horizontalLayout.addComponent(deleteButton);

            addComponent(horizontalLayout);
            Panel filterPanel = new Panel("Панель фильтров");

            filterPanel.setWidth("430px");
            filterPanel.setHeight("195px");
            HorizontalLayout panelLayout = new HorizontalLayout();
            panelLayout.setSpacing(true);
            panelLayout.setMargin(true);
            VerticalLayout firstLayout = new VerticalLayout();
            VerticalLayout secondLayout = new VerticalLayout();
            filterDescription = new TextField("Описание");
            firstLayout.addComponent(filterDescription);
            priorityBox = new ComboBox("Приоритет");
            priorityBox.addItems("Нормальный","Срочный","Немедленный");

            priorityBox.setNullSelectionAllowed(true);
            priorityBox.setTextInputAllowed(false);
            firstLayout.addComponent(priorityBox);
            patientBox = new ComboBox("Выберите Пациента");
            patientBox.setNullSelectionAllowed(true);
            patientBox.setTextInputAllowed(false);
            secondLayout.addComponent(patientBox);
            secondLayout.addComponent(new Label(""));
            Button selectButton = new Button("Применить");

            selectButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

            if (!filterDescription.isEmpty() && patientBox.getValue() != null && priorityBox.getValue() != null) {
                filterNum = 1;
                desc = filterDescription.getValue();
                filterId = ((Patient) patientBox.getValue()).getId();
                filterPriority = priorityBox.getValue().toString();
                isFilter = true;
                updateTable();
            }
            else if (!filterDescription.isEmpty() && patientBox.getValue() == null && priorityBox.getValue() == null) {
                filterNum = 2;
                desc = filterDescription.getValue();
                filterId = null;
                filterPriority = null;
                isFilter = true;
                updateTable();
            }
            else if (!filterDescription.isEmpty() && patientBox.getValue() == null && priorityBox.getValue() != null) {
                filterNum = 3;
                desc = filterDescription.getValue();
                filterId = null;
                filterPriority = (String) priorityBox.getValue();
                isFilter = true;
                updateTable();
            }
            else if (filterDescription.isEmpty() && patientBox.getValue() != null && priorityBox.getValue() != null) {
                filterNum = 4;
                desc = null;
                filterId = ((Patient) patientBox.getValue()).getId();
                filterPriority = (String) priorityBox.getValue();
                isFilter = true;
                updateTable();
            }
            else if (!filterDescription.isEmpty() && patientBox.getValue() != null && priorityBox.getValue() == null) {
                filterNum = 5;
                desc = filterDescription.getValue();
                filterId = ((Patient) patientBox.getValue()).getId();
                filterPriority = null;
                isFilter = true;
                updateTable();
            }
            else if (filterDescription.isEmpty() && patientBox.getValue() != null && priorityBox.getValue() == null) {
                filterNum = 6;
                desc = null;
                filterId = ((Patient) patientBox.getValue()).getId();
                filterPriority = null;
                isFilter = true;
                updateTable();
            }
            else if (filterDescription.isEmpty() && patientBox.getValue() == null && priorityBox.getValue() != null) {
                filterNum = 7;
                desc = null;
                filterId = null;
                filterPriority = (String) priorityBox.getValue();
                isFilter = true;
                updateTable();
            }

            else if (filterDescription.isEmpty() && patientBox.getValue() == null && priorityBox.getValue() == null) {
                if(isFilter){
                    isFilter=false;
                    updateTable();
                    Notification.show("Фильтра снят");
                }
                else{
                    Notification.show("Не выбран фильтр");
                }
            }
            }
            });

            secondLayout.addComponent(selectButton);
            panelLayout.addComponent(firstLayout);
            panelLayout.addComponent(secondLayout);
            filterPanel.setContent(panelLayout);
            addComponent(filterPanel);
            addComponent(gridName);
        }

    public void updateTable() {
        doctorList = doctorDAO.getAllRecipes();
        if(isFilter){
            filter(filterNum);
            filterDescription.clear();
            patientBox.clear();
            priorityBox.clear();
        }

        PatientDAO patientDAO = new PatientDAO();
        List<Patient> patientList = patientDAO.getAllPatients();
        patientBox.setContainerDataSource(new BeanItemContainer<>(Patient.class,patientList));
        patientBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        patientBox.setItemCaptionPropertyId("caption");

        BeanItemContainer container = new BeanItemContainer<>(Recipe.class,doctorList);
        GeneratedPropertyContainer generatedPropertyContainer = new GeneratedPropertyContainer(container);
        generatedPropertyContainer.addGeneratedProperty("doctor", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object o, Object o1) {
                return ((Doctor)item.getItemProperty("doctor").getValue()).getCaption();
            }
            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        generatedPropertyContainer.addGeneratedProperty("patient", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object o, Object o1) {
                return ((Patient)item.getItemProperty("patient").getValue()).getCaption();
            }
            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        generatedPropertyContainer.addGeneratedProperty("date", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object o, Object o1) {
                Date date = (Date)item.getItemProperty("date").getValue();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
                return simpleDateFormat.format(date);
            }
            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        grid.setContainerDataSource(generatedPropertyContainer);
        grid.recalculateColumnWidths();
    }

    private void filter(int number){
        ArrayList<Recipe> result = new ArrayList<>();

        if(number==1){

            for(int i=0;i<doctorList.size();i++){
                if(doctorList.get(i).getDescription().indexOf(desc)!=-1&&
                        doctorList.get(i).getPriority().equals(filterPriority)&&
                doctorList.get(i).getPatient().getId().equals(filterId)){
                    result.add(doctorList.get(i));
                }
            }
        }
        else if(number==2){

            for(int i=0;i<doctorList.size();i++){
                if(doctorList.get(i).getDescription().indexOf(desc)!=-1){
                    result.add(doctorList.get(i));
                }
            }
        }
        else if(number==3){

            for(int i=0;i<doctorList.size();i++){
                if(doctorList.get(i).getDescription().indexOf(desc)!=-1&&
                        doctorList.get(i).getPriority().equals(filterPriority)){
                    result.add(doctorList.get(i));
                }
            }
        }
        else if(number==4){

            for(int i=0;i<doctorList.size();i++){
                if(doctorList.get(i).getPatient().getId().equals(filterId)&&
                        doctorList.get(i).getPriority().equals(filterPriority)){
                    result.add(doctorList.get(i));
                }
            }
        }
        else if(number==5){

            for(int i=0;i<doctorList.size();i++){
                if(doctorList.get(i).getPatient().getId().equals(filterId)&&
                        doctorList.get(i).getDescription().indexOf(desc)!=-1){
                    result.add(doctorList.get(i));
                }
            }
        }
        else if(number==6){

            for(int i=0;i<doctorList.size();i++){
                if(doctorList.get(i).getPatient().getId().equals(filterId)){
                    result.add(doctorList.get(i));
                }
            }
        }
        else if(number==7){

            for(int i=0;i<doctorList.size();i++){
                if(doctorList.get(i).getPriority().equals(filterPriority)){
                    result.add(doctorList.get(i));
                }
            }
        }

        doctorList = result;
    }

    @Override
        public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
            isFilter=false;
            updateTable();
        }
    }

