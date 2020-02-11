package com.haulmont.testtask.editwindows;

import com.haulmont.testtask.daoclasses.DoctorDAO;
import com.haulmont.testtask.daoclasses.PatientDAO;
import com.haulmont.testtask.daoclasses.RecipeDAO;
import com.haulmont.testtask.models.Doctor;
import com.haulmont.testtask.models.Patient;
import com.haulmont.testtask.models.Recipe;
import com.haulmont.testtask.views.RecipeTableView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

import java.util.Date;
import java.util.List;

public class EditRecipeWindow extends Window {
    private TextField description;
    private ComboBox patientBox;
    private ComboBox doctorBox;
    private DateField dateField;
    private TextField validityPeriod;
    private ComboBox priorityBox;
    private Button okButton;
    private Button cancelButton;
    private List<Patient> patientList;
    private BeanItemContainer container;
    private  List<Doctor> doctorList;
    private BeanItemContainer doctorContainer;

    public EditRecipeWindow(RecipeTableView view) {
        super();
        center();
        setModal(true);
        setClosable(false);
        setResizable(false);

        FormLayout content = getLayout();
        container = new BeanItemContainer<>(Patient.class,patientList);
        patientBox.setContainerDataSource((container));
        patientBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        patientBox.setItemCaptionPropertyId("caption");
        priorityBox.setValue("Срочный");

        if(patientList.size()>0) {
            patientBox.setValue(patientList.get(0));
        }

        doctorContainer = new BeanItemContainer<>(Doctor.class,doctorList);
        doctorBox.setContainerDataSource(new BeanItemContainer<>(Doctor.class,doctorList));
        doctorBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        doctorBox.setItemCaptionPropertyId("caption");

        if(doctorList.size()>0) {
            doctorBox.setValue(doctorList.get(0));
        }

        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    if(dateField.isValid()&&dateField.getValue()!=null&&!description.isEmpty()
                            &&priorityBox.getValue()!=null&&patientBox.getValue()!=null&&doctorBox.getValue()!=null&&
                            validityPeriod.isValid()&&!validityPeriod.isEmpty()) {

                        RecipeDAO recipeDAO = new RecipeDAO();
                        Date date = dateField.getValue();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        Recipe recipe = new Recipe();
                        recipe.setDate(sqlDate);
                        recipe.setValidityPeriod(Integer.parseInt(validityPeriod.getValue()));
                        recipe.setPriority((String) priorityBox.getValue());
                        recipe.setDescription(description.getValue());
                        recipe.setDoctor((Doctor) doctorBox.getValue());
                        recipe.setPatient((Patient) patientBox.getValue());
                        recipeDAO.addRecipe(recipe);
                        view.updateTable();
                        close();
                    }
                    else {
                        Notification.show("Неправильный ввод");
                    }
                }
                catch (Exception e){
                    Notification.show("Произошла ошибка");
                }
            }
        });

        setContent(content);
    }

    public EditRecipeWindow(RecipeTableView view, Recipe recipe) {
        super();
        center();
        setModal(true);
        setClosable(false);
        setResizable(false);

        FormLayout content = getLayout();
        description.setValue(recipe.getDescription());
        container = new BeanItemContainer(Patient.class);

        int indexSelected = 0;
        for(int i=0;i<patientList.size();i++){
            if(patientList.get(i).getId().equals(recipe.getPatient().getId())) {
                indexSelected = i;
            }
            container.addItem(patientList.get(i));
        }

        patientBox.setContainerDataSource(container);
        patientBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        patientBox.setItemCaptionPropertyId("caption");
        patientBox.setValue(patientList.get(indexSelected));
        doctorContainer = new BeanItemContainer(Doctor.class);

        for(int i=0;i<doctorList.size();i++){
            if(doctorList.get(i).getId().equals(recipe.getDoctor().getId())) {
                indexSelected = i;
            }
            doctorContainer.addItem(doctorList.get(i));
        }

        doctorBox.setContainerDataSource(doctorContainer);
        doctorBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        doctorBox.setItemCaptionPropertyId("caption");
        doctorBox.setValue(doctorList.get(indexSelected));

        dateField.setValue(recipe.getDate());
        validityPeriod.setValue(String.valueOf(recipe.getValidityPeriod()));
        priorityBox.setValue(recipe.getPriority());

        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    if(dateField.isValid()&&dateField.getValue()!=null&&!description.isEmpty()
                    &&priorityBox.getValue()!=null&&patientBox.getValue()!=null&&doctorBox.getValue()!=null&&
                    validityPeriod.isValid()&&!validityPeriod.isEmpty()) {

                        RecipeDAO recipeDAO = new RecipeDAO();
                        recipe.setDescription(description.getValue());
                        recipe.setPatient((Patient) patientBox.getValue());
                        recipe.setDoctor((Doctor) doctorBox.getValue());
                        recipe.setDate(new java.sql.Date(dateField.getValue().getTime()));
                        recipe.setPriority((String) priorityBox.getValue());
                        recipe.setValidityPeriod(Integer.parseInt(validityPeriod.getValue()));
                        recipeDAO.updateRecipe(recipe);
                        view.updateTable();
                        close();
                    }
                    else {
                        Notification.show("Неправильный ввод");
                    }
                }
                catch (Exception ex) {
                    Notification.show("Произошла ошибка");
                }
            }
        });

        setContent(content);
    }

    private FormLayout getLayout(){

        RegexpValidator periodValidator = new RegexpValidator("^[0-9]{1,3}$",
                "Ошибка ввода");
        description = new TextField("Описание");
        description.setWidth("370px");

        validityPeriod = new TextField("Срок действия");
        validityPeriod.setWidth("185px");
        validityPeriod.addValidator(periodValidator);

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        okButton = new Button("Ок");
        cancelButton = new Button("Отмена");
        layout.addComponent(okButton);
        layout.addComponent(cancelButton);

        doctorBox = new ComboBox("Выберите Врача");
        DoctorDAO doctorDAO = new DoctorDAO();
        doctorList =  doctorDAO.getAllDoctors();
        doctorBox.setNullSelectionAllowed(false);
        doctorBox.setTextInputAllowed(false);
        doctorBox.setWidth("370px");

        patientBox = new ComboBox("Выберите Пациента");
        PatientDAO patientDAO = new PatientDAO();
        patientList = patientDAO.getAllPatients();
        patientBox.setNullSelectionAllowed(false);
        patientBox.setTextInputAllowed(false);

        dateField = new DateField("Дата");
        dateField.setDateFormat("dd/MM/yyyy");
        dateField.setValue(new Date());
        patientBox.setWidth("370px");
        priorityBox = new ComboBox("Приоритет");
        priorityBox.addItems("Нормальный","Срочный","Немедленный");
        priorityBox.setNullSelectionAllowed(false);
        priorityBox.setTextInputAllowed(false);

        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });

        FormLayout content = new FormLayout(description,doctorBox,patientBox,dateField,
                validityPeriod,priorityBox,layout);
        content.setMargin(true);
        content.setSpacing(true);
        content.setSizeUndefined();
        return content;
    }
}
