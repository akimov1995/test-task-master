package com.haulmont.testtask.daoclasses;

import com.haulmont.testtask.dbutill.JDBCUtils;
import com.haulmont.testtask.models.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public void addPatient(Patient patient) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PATIENT " +
                    "(patient_name,patient_surname,patient_patronymic,phone_number) VALUES (?,?,?,?);");

            preparedStatement.setString(1,patient.getName());
            preparedStatement.setString(2,patient.getSurname());
            preparedStatement.setString(3,patient.getPatronymic());
            preparedStatement.setString(4,patient.getPhoneNumber());
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

    public void updatePatient(Patient patient) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE PATIENT SET " +
                    "patient_name = ?, patient_surname = ?, patient_patronymic = ?, phone_number = ? " +
                    "WHERE patient_id = ?;");

            preparedStatement.setString(1,patient.getName());
            preparedStatement.setString(2,patient.getSurname());
            preparedStatement.setString(3,patient.getPatronymic());
            preparedStatement.setString(4,patient.getPhoneNumber());
            preparedStatement.setLong(5,patient.getId());
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

    public List<Patient> getAllPatients() {
        Connection connection = JDBCUtils.getConnection();
        List<Patient> patients = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM PATIENT;");
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getLong("patient_id"));
                patient.setName(resultSet.getString("patient_name"));
                patient.setSurname(resultSet.getString("patient_surname"));
                patient.setPatronymic(resultSet.getString("patient_patronymic"));
                patient.setPhoneNumber(resultSet.getString("phone_number"));
                patients.add(patient);
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
        return patients;
    }

    public void deletePatient(Long id) throws SQLException{
        Connection connection = JDBCUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("DELETE FROM PATIENT WHERE patient_id = ?");
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
    public Patient getPatientById(Long id) throws SQLException{
        Connection connection = JDBCUtils.getConnection();
        Patient patient = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM PATIENT WHERE patient_id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            patient = new Patient();
            resultSet.next();
            patient.setId(resultSet.getLong("patient_id"));
            patient.setName(resultSet.getString("patient_name"));
            patient.setSurname(resultSet.getString("patient_surname"));
            patient.setPatronymic(resultSet.getString("patient_patronymic"));
            patient.setPhoneNumber(resultSet.getString("phone_number"));
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
        return patient;
    }
}
