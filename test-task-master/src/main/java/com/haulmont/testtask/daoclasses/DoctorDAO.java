package com.haulmont.testtask.daoclasses;

import com.haulmont.testtask.dbutill.JDBCUtils;
import com.haulmont.testtask.models.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorDAO {

    public void addDoctor(Doctor doctor) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO DOCTOR " +
                    "(doctor_name,doctor_surname,doctor_patronymic,specialization) VALUES (?,?,?,?);");

            preparedStatement.setString(1,doctor.getName());
            preparedStatement.setString(2,doctor.getSurname());
            preparedStatement.setString(3,doctor.getPatronymic());
            preparedStatement.setString(4,doctor.getSpecialization());
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
    public void updateDoctor(Doctor doctor) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE DOCTOR SET " +
                    "doctor_name = ?, doctor_surname = ?, doctor_patronymic = ?, specialization = ? " +
                    "WHERE doctor_id = ?;");

            preparedStatement.setString(1,doctor.getName());
            preparedStatement.setString(2,doctor.getSurname());
            preparedStatement.setString(3,doctor.getPatronymic());
            preparedStatement.setString(4,doctor.getSpecialization());
            preparedStatement.setLong(5,doctor.getId());
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


    public List<Doctor> getAllDoctors() {
        Connection connection = JDBCUtils.getConnection();
        List<Doctor> doctors = new ArrayList<>();
        try {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM DOCTOR;");
        ResultSet resultSet = ps.executeQuery();
        while(resultSet.next()) {
            Doctor doctor = new Doctor();
            doctor.setId(resultSet.getLong("doctor_id"));
            doctor.setName(resultSet.getString("doctor_name"));
            doctor.setSurname(resultSet.getString("doctor_surname"));
            doctor.setPatronymic(resultSet.getString("doctor_patronymic"));
            doctor.setSpecialization(resultSet.getString("specialization"));
            doctors.add(doctor);
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
        return doctors;
    }

    public void deleteDoctor(Long id) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("DELETE FROM DOCTOR WHERE doctor_id = ?");
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

    public Doctor getDoctorById(Long id) {
        Connection connection = JDBCUtils.getConnection();
        Doctor doctor = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM DOCTOR WHERE doctor_id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            doctor = new Doctor();
            resultSet.next();
            doctor.setId(resultSet.getLong("doctor_id"));
            doctor.setName(resultSet.getString("doctor_name"));
            doctor.setSurname(resultSet.getString("doctor_surname"));
            doctor.setPatronymic(resultSet.getString("doctor_patronymic"));
            doctor.setSpecialization(resultSet.getString("specialization"));
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
        return doctor;
    }

    public Map<Doctor, Integer> getStatistic() {
        Connection connection = JDBCUtils.getConnection();
        Map<Doctor, Integer> resultMap = new HashMap<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT d.*, COUNT(r.doctor_id) FROM DOCTOR AS d " +
                    "LEFT OUTER JOIN Recipe AS r ON d.doctor_id = " +
                    "r.doctor_id GROUP BY d.doctor_id");
            while (resultSet.next()) {
                Long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                String surname = resultSet.getString(3);
                String patronymic = resultSet.getString(4);
                String specialization = resultSet.getString(5);
                int recipeCount = resultSet.getInt(6);

                Doctor doctor = new Doctor(id,name,surname,patronymic,specialization);
                resultMap.put(doctor,recipeCount);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return resultMap;
    }

}
