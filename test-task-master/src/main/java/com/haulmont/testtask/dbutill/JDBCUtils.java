package com.haulmont.testtask.dbutill;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtils {
    private static Connection connection;

    static {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        }
        catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() {
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:file:database/taskDatabase",
                    "SA","");
            /*File file = new File("sql-scripts/createTables.sql");
            String sqlQuery = FileUtils.readFileToString(file,"utf-8");
            connection.createStatement().executeUpdate(sqlQuery);
            */
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return connection;
    }


}
