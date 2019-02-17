package DatabaseUtil;

import java.sql.*;

interface DatabaseTemplate{

}

public class DatabaseUtil {
    private final String DATABASE_NAME = "U04e9K";
    private final String JDBC_CONNECTION_STRING = "jdbc:mysql://52.206.157.109/" + DATABASE_NAME;
    private final String USERNAME = "U04e9K";
    private final String PASSWORD = "53688218206";
    public Connection connection;

    public DatabaseUtil(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection(JDBC_CONNECTION_STRING, USERNAME, PASSWORD);
            System.out.println("Connected");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    public void createTables(){

        String user = "CREATE TABLE user\n" +
                "(\n" +
                "  userId       INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "  userName     VARCHAR(50),\n" +
                "  password     VARCHAR(50),\n" +
                "  active       TINYINT,\n" +
                "  createDate   DATETIME,\n" +
                "  createdBy    varchar(40),\n" +
                "  lastUpdate   TIMESTAMP,\n" +
                "  lastUpdateBy VARCHAR(40)\n" +
                ");";


        String country = "CREATE TABLE country\n" +
                "(\n" +
                "  countryId    INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "  country      VARCHAR(50),\n" +
                "  createDate   DATETIME,\n" +
                "  createdBy    VARCHAR(40),\n" +
                "  lastUpdate   TIMESTAMP,\n" +
                "  lastUpdateBy VARCHAR(40)\n" +
                ");";

        String city = "CREATE TABLE city\n" +
                "(\n" +
                "  cityId       INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "  city         VARCHAR(50),\n" +
                "  countryId    INT(10),\n" +
                "  createDate   DATETIME,\n" +
                "  createdBy    VARCHAR(40),\n" +
                "  lastUpdate   TIMESTAMP,\n" +
                "  lastUpdateBy VARCHAR(40),\n" +
                "  FOREIGN KEY (countryId) REFERENCES country (countryId) ON DELETE NO ACTION ON UPDATE CASCADE\n" +
                ");";

        String address = "CREATE TABLE address\n" +
                "(\n" +
                "  addressId    INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "  address      VARCHAR(50),\n" +
                "  address2     VARCHAR(50),\n" +
                "  cityId       INT(10),\n" +
                "  postalCode   varchar(10),\n" +
                "  phone        varchar(20),\n" +
                "  createDate   DATETIME,\n" +
                "  createdBy    VARCHAR(40),\n" +
                "  lastUpdate   TIMESTAMP,\n" +
                "  lastUpdateBy VARCHAR(40),\n" +
                "  FOREIGN KEY (cityId) REFERENCES city (cityId) ON DELETE NO ACTION ON UPDATE CASCADE\n" +
                ");";

        String customer = "CREATE TABLE customer\n" +
                "(\n" +
                "  customerId   INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "  customerName VARCHAR(45),\n" +
                "  addressId    INT(10),\n" +
                "  active       TINYINT(1),\n" +
                "  createDate   DATETIME,\n" +
                "  createdBy    VARCHAR(40),\n" +
                "  lastUpdate   TIMESTAMP,\n" +
                "  lastUpdateBy VARCHAR(40),\n" +
                "  FOREIGN KEY (addressId) REFERENCES address (addressId) ON DELETE NO ACTION ON UPDATE CASCADE\n" +
                ");";

        String appointment = "CREATE TABLE appointment\n" +
                "(\n" +
                "  appointmentId INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "  customerId    INT(10),\n" +
                "  userId        INT,\n" +
                "  title         VARCHAR(255),\n" +
                "  description   TEXT,\n" +
                "  location      TEXT,\n" +
                "  contact       TEXT,\n" +
                "  type          TEXT,\n" +
                "  url           VARCHAR(255),\n" +
                "  start         DATETIME,\n" +
                "  end           DATETIME,\n" +
                "  createDate    DATETIME,\n" +
                "  createdBy     VARCHAR(40),\n" +
                "  lastUpdate    TIMESTAMP,\n" +
                "  lastUpdateBy  VARCHAR(40),\n" +
                "  FOREIGN KEY (customerId) REFERENCES customer (customerId) ON DELETE NO ACTION ON UPDATE CASCADE,\n" +
                "  FOREIGN KEY (userId) references user (userId) ON DELETE NO ACTION ON UPDATE CASCADE\n" +
                ");";

        runSimpleExecute(user);
        runSimpleExecute(country);
        runSimpleExecute(city);
        runSimpleExecute(address);
        runSimpleExecute(customer);
        runSimpleExecute(appointment);

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
