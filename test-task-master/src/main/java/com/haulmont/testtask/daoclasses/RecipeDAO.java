package com.haulmont.testtask.daoclasses;

import com.haulmont.testtask.dbutill.JDBCUtils;
import com.haulmont.testtask.models.Recipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

    public void addRecipe(Recipe recipe) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Recipe " +
                    "(description, recipe_date, validity_period, priority, patient_id, " +
                    "doctor_id) VALUES (?,?,?,?,?,?);");

            preparedStatement.setString(1,recipe.getDescription());
            preparedStatement.setDate(2,recipe.getDate());
            preparedStatement.setInt(3,recipe.getValidityPeriod());
            preparedStatement.setString(4,recipe.getPriority());
            preparedStatement.setLong(6,recipe.getDoctor().getId());
            preparedStatement.setLong(5,recipe.getPatient().getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex){
            throw ex;
        }
        finally {
            if(connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
    public void updateRecipe(Recipe recipe) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE RECIPE SET " +
                    "description = ?, recipe_date = ?, validity_period = ?, priority = ?, doctor_id = ?, " +
                    "patient_id = ? " +
                    "WHERE recipe_id = ?;");

            preparedStatement.setString(1, recipe.getDescription());
            preparedStatement.setDate(2, recipe.getDate());
            preparedStatement.setInt(3, recipe.getValidityPeriod());
            preparedStatement.setString(4, recipe.getPriority());
            preparedStatement.setLong(5, recipe.getDoctor().getId());
            preparedStatement.setLong(6, recipe.getPatient().getId());
            preparedStatement.setLong(7, recipe.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex){
            throw ex;
        }
        finally {
            if(connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }


    public List<Recipe> getAllRecipes() {
        Connection connection = JDBCUtils.getConnection();

        List<Recipe> recipes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Recipe;");
            ResultSet resultSet = ps.executeQuery();
            PatientDAO patientDAO = new PatientDAO();
            DoctorDAO doctorDAO = new DoctorDAO();
            while(resultSet.next()) {
                Recipe recipe = new Recipe();
                recipe.setId(resultSet.getLong("recipe_id"));
                recipe.setDescription(resultSet.getString("description"));
                recipe.setDate(resultSet.getDate("recipe_date"));
                recipe.setPriority(resultSet.getString("priority"));
                recipe.setValidityPeriod(resultSet.getInt("validity_period"));
                recipe.setDoctor(doctorDAO.getDoctorById(resultSet.getLong("doctor_id")));
                recipe.setPatient(patientDAO.getPatientById(resultSet.getLong("patient_id")));
                recipes.add(recipe);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            if(connection!=null) {
                try {
                    connection.close();
                }
                catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return recipes;
    }

    public void deleteRecipe(Long id) throws SQLException{
        Connection connection = JDBCUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("DELETE FROM Recipe WHERE recipe_id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw ex;
        }
        finally {
            if(connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
