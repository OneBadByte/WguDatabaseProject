package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import com.blackdartq.WguDatabaseProject.FileUtil.FileUtil;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

interface DatabaseTemplate{
    void deleteById(int id);
//    void addRow(ArrayList arrayList);
//    void updateById(int id, ArrayList arrayList);
}

public class DatabaseUtil {
    private final String DATABASE_NAME = "U04e9K";
    private final String JDBC_CONNECTION_STRING = "jdbc:mysql://52.206.157.109/" + DATABASE_NAME;
    private final String USERNAME = "U04e9K";
    private final String PASSWORD = "53688218206";
    private final String[] TABLES = {"user", "country", "city", "address", "customer", "appointment"};
    public Connection connection;

    public DatabaseUtil(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection(JDBC_CONNECTION_STRING, USERNAME, PASSWORD);
            System.out.println("Connected");
            createTables();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    public boolean checkForAllTables(){
        boolean output = true;
        List<String> tablesToCheckFor = Arrays.asList(this.TABLES);
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SHOW TABLES;");
            if(!rs.next()){
                output = false;
            }
            while (rs.next()){
                if(!tablesToCheckFor.contains(rs.getString(1))){
                    System.out.println("table isn't there " + rs.getString(1));
                   output = false;
                }
            }
            } catch (SQLException e1) {
            e1.printStackTrace();
        }
        System.out.println("All tables created already " + output);
        return output;
    }

    public void createTables(){
        if(!checkForAllTables()) {
            System.out.println("Creating Tables");
            String databaseCommands = FileUtil.readFromFile("src/com/blackdartq/WguDatabaseProject/ScratchFiles/setup.sql");
            String[] dbCommandArray = databaseCommands.split(";");
            for (String command : dbCommandArray) {
                runSimpleExecute(command + ";");
            }
            System.out.println("Finished");
        }
    }

    public void dropTables(){
        String[] tables = {
                "appointment",
                "user",
                "customer",
                "address",
                "city",
                "country",
        };

        for(String table : tables){
            runSimpleExecute("DROP TABLE " + table);
        }
        System.out.println("Dropped all tables");
    }

    public void runSimpleExecute(String command){
        try {
            Statement statement = this.connection.createStatement();
            boolean resultSet = statement.execute(command);
        } catch (SQLException e) {
            System.out.println(e);
        }

    }


}
