package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import com.blackdartq.WguDatabaseProject.Controllers.ControllerUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

class Customer{
    int customerId;
    String customerName;
    int addressId;

    Customer(int Id, String customerName, int addressId){
        this.customerId = Id;
        this.customerName = customerName;
        this.addressId = addressId;
    }
}

public class CustomerDB extends DatabaseUtil implements DatabaseTemplate{
    private ArrayList<Customer> customers = new ArrayList<>();

    public int getCustomersSize(){
        return customers.size();
    }

    public ArrayList getAllCustomerIDs(){
        getAllCustomers();
       ArrayList<Integer> output = new ArrayList<>();
       for(Customer customer : customers){
           output.add(customer.customerId);
       }
       return output;
    }

    public ArrayList getAllCustomerNames(){
        getAllCustomers();
        ArrayList<String> output = new ArrayList<>();
        for(Customer customer : customers){
            output.add(customer.customerName);
        }
        return output;
    }

    @Override
    public void deleteById(int id) {
        for(Customer customer : customers){
            if(customer.customerId == id){
                try {
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM customer WHERE customerId = ?");
                    System.out.println("deleting id: " + id);
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

    public int getCustomerAddressIdByIndex(int index){
        return customers.get(index).addressId;
    }

    private void getAllCustomers(){
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
}
