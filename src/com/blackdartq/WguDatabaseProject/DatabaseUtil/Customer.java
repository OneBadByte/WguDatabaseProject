package com.blackdartq.WguDatabaseProject.DatabaseUtil;

public class Customer{
    int customerId;
    String customerName;
    int addressId;

    public Customer(){
    }

    Customer(int Id, String customerName, int addressId){
        this.customerId = Id;
        this.customerName = customerName;
        this.addressId = addressId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
