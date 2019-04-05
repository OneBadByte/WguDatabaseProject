package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerDB extends DatabaseUtil{
    private ArrayList<Customer> customers = new ArrayList<>();

    public int getCustomersSize(){
        return customers.size();
    }

    public ArrayList<Integer> getAllCustomerIDs(){
        getAllCustomersFromDatabase();
       ArrayList<Integer> output = new ArrayList<>();
       for(Customer customer : customers){
           output.add(customer.customerId);
       }
       return output;
    }

    public ArrayList getAllCustomerNames(){
        getAllCustomersFromDatabase();
        ArrayList<String> output = new ArrayList<>();
        for(Customer customer : customers){
            output.add(customer.customerName);
        }
        return output;
    }

    public Customer getCustomerById(int id){
       for(Customer customer: customers){
           if(customer.customerId == id){
               return customer;
           }
       }
       throw new RuntimeException("couldn't get customer by ID");
    }

    public String getCustomerNameById(int id){
        return getCustomerById(id).customerName;
    }

    public int getCustomerIndexById(int id){
        int output = 0;
        for(Customer customer : customers){
            if(customer.customerId == id){
                return output;
            }
            output++;
        }
        throw new RuntimeException("couldn't retrieve customer index by id");
    }

    public void deleteById(int id) {
        for(Customer customer : customers){
            if(customer.customerId == id){
                try {
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM customer WHERE customerId = ?");
                    statement.setInt(1, id);
                    statement.execute();
                } catch (SQLException e) {
                    throw new RuntimeException("couldn't delete customer by id: " + id + "\n because " + e);
                }
            }
        }
    }

    public void deleteCustomerByIndex(int index){
        if(customers.size() == 0 || index < 0){
            return;
        }
       int id = customers.get(index).customerId;
       deleteById(id);
    }

    /**
     * Gets the customer name from customers by index
     */

    public String getCustomerNameByIndex(int index){
        return customers.get(index).customerName;
    }

    public int getCustomerIdByIndex(int index){
        return customers.get(index).customerId;
    }

    public int getCustomerAddressIdByIndex(int index){
        return customers.get(index).addressId;
    }

    public void getAllCustomersFromDatabase(){
        final int CUSTOMER_ID = 1;
        final int CUSTOMER_NAME = 2;
        final int ADDRESS_ID = 3;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT customerId, customerName, addressId FROM customer;");

            // checks that there are two columns returned by the database
            int columnCount = resultSet.getMetaData().getColumnCount();
            if(columnCount != 3){
                throw new RuntimeException("customer data could not get customer id and customer name");
            }

            // adds all the customer data to the customer ArrayList
            customers.clear();
            while(resultSet.next()){
                this.customers.add(new Customer(
                        resultSet.getInt(CUSTOMER_ID),
                        resultSet.getString(CUSTOMER_NAME),
                        resultSet.getInt(ADDRESS_ID)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Customer data was unavailable");
        }
    }

    public void addCustomer(String customerName, int addressId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO customer VALUE(NULL, ?, ?, 1, CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');");
            preparedStatement.setString(1, customerName);
            preparedStatement.setInt(2, addressId);
            preparedStatement.execute();
        }catch (Exception e){
            throw new RuntimeException("couldn't add customer");
        }
    }

    /**
     * Updates the users name
     * TODO add address changing powers
     */
    public void updateCustomer(int customerId, String customerName){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE customer SET customerName = ? WHERE customerId = ?"
            );
            statement.setString(1, customerName);
            statement.setInt(2, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("couldn't update customer information");
        }

    }
}


















