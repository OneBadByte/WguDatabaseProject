package com.blackdartq.WguDatabaseProject.DatabaseUtil;

public class Address{
    public int addressId;
    public String address;
    public String address2;
    public int cityId;
    public String postalCode;
    public String phone;

    public Address(int addressId, String address, String address2, int cityId, String postalCode, String phone) {
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
    }
}
