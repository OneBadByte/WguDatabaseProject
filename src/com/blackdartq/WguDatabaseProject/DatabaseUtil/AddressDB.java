package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AddressDB extends DatabaseUtil {

    // holders for all the addresses, cities, and countries
    private ArrayList<Address> addresses = new ArrayList<>();
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayList<Country> countries = new ArrayList<>();

    /**
     * Updates addresses, cities, and countries with database info
     */
    public void updateAllAddressData(){
        getCountriesFromDatabase();
        getCitiesFromDatabase();
        getAddressesFromTheDatabase();
    }

    //++++++++++++ COUNTRY METHODS +++++++++++++++++++++++++++
    /**
     * gets an ArrayList of the countries names
     */
    public ArrayList<String> getCountries() {
        getCountriesFromDatabase();
        ArrayList<String> output = new ArrayList<>();
        for(Country country : countries){
            output.add(country.countryName);
        }
        return output;
    }

    /**
     * gets the country database id of the index in countries
     */
    public int getCountryIdByIndex(int index){
        return countries.get(index).countryId;
    }

    /**
     * gets the countrys name from countries
     * @param id
     * @return
     */
    public String getCountryNameFromCountriesById(int id){
        for(Country country : countries){
            if(country.countryId == id){
                return country.countryName;
            }
        }
        throw new RuntimeException("couldn't find country id in countries");
    }

    /**
     * gets all the countries from the database and loads the countries ArrayList
     */
    private void getCountriesFromDatabase(){
        final int COUNTRY_ID = 1;
        final int COUNTRY_NAME = 2;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT countryId, country FROM country;");
            countries.clear();
            while(resultSet.next()){
                countries.add(new Country(resultSet.getInt(COUNTRY_ID), resultSet.getString(COUNTRY_NAME)));
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new RuntimeException("Couldn't get country from the database");
        }
    }

    //++++++++++++ CITY METHODS +++++++++++++++++++++++++++
    /**
     * Adds a city to the database
     */
    public void addCity(String city, int countryId){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO city VALUE(null, ?, ?, CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');");
            ps.setString(1, city);
            ps.setInt(2, countryId);
            ps.execute();
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new RuntimeException("Couldn't add city to the database");
        }
    }

    /**
     * Modifies a city
     */
    public void updateCity(int cityId, String city, int countryId){
        try {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE city SET city = ?, countryId = ? WHERE cityId= ?;"
        );
            statement.setString(1, city);
            statement.setInt(2, countryId);
            statement.setInt(3, cityId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("couldn't update city in database");
        }
    }

    /**
     * Modifies a city by using a city object
     */
    public void updateCity(City city){
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE city SET city = ?, countryId = ? WHERE cityId= ?;"
            );
            statement.setString(1, city.cityName);
            statement.setInt(2, city.countryId);
            statement.setInt(3, city.cityId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("couldn't update city in database");
        }
    }

    /**
     * Returns a city from cities using its id
     */
    public City getCityFromCitiesById(int id){
        for(City city : cities){
            if(city.cityId == id){
                return city;
            }
        }
        throw new RuntimeException("couldn't retrieve city in cities");
    }

    /**
     * Gets city from cities
     */
    public City getCityFromCitiesByCityId(int id){
        for(City city : cities){
            if(city.cityId == id){
                return city;
            }
        }
        throw new RuntimeException("couldn't find city in cities");
    }

    /**
     * Gets a city's ID number by the city name and country ID
     */
    public int getCityId(String cityName, int countryId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT cityId FROM city WHERE city = ? AND countryId = ?;"
            );
            preparedStatement.setString(1, cityName);
            preparedStatement.setInt(2, countryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.first();
            int id = resultSet.getInt(1);
            return id;
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new RuntimeException("Couldn't find city in the database");
        }

    }

    /**
     * gets the city's ID by using the address ID
     */
    public int getCityIdFromByAddressId(int addressId){
        for(Address address : addresses){
            if(addressId == address.addressId){
                return address.cityId;
            }
        }
        throw new RuntimeException("couldn't get city id with address id");
    }

    /**
     * Gets all the cities from the database and loads them into cities
     */
    private void getCitiesFromDatabase(){
        final int CITY_ID = 1;
        final int CITY_NAME = 2;
        final int COUNTRY_ID = 3;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT cityId, city, countryId FROM city;");
            cities.clear();
            while(resultSet.next()){
                cities.add(new City(
                        resultSet.getInt(CITY_ID),
                        resultSet.getString(CITY_NAME),
                        resultSet.getInt(COUNTRY_ID)));
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new RuntimeException("Couldn't get city from the database");
        }
    }

    //++++++++++++ ADDRESS METHODS +++++++++++++++++++++++++++

    /**
     * Adds an address to the database
     */
    public void addAddressToDatabase(String address, String address2, int cityId, String postalCode, String phone){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO address VALUE(NULL, ?, ?, ?, ?, ?, CURDATE(), 'test', CURRENT_TIMESTAMP, 'test')");
            preparedStatement.setString(1, address);
            preparedStatement.setString(2, address2);
            preparedStatement.setInt(3, cityId);
            preparedStatement.setString(4, postalCode);
            preparedStatement.setString(5, phone);
            preparedStatement.execute();
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new RuntimeException("Couldn't add address to the database");
        }
    }

    /**
     * Gets all the addresses from the database
     */
    public void getAddressesFromTheDatabase(){
        final int ADDRESS_ID = 1;
        final int ADDRESS = 2;
        final int ADDRESS2 = 3;
        final int CITY_ID = 4;
        final int POSTAL_CODE = 5;
        final int PHONE = 6;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT addressId, address, address2, cityId, postalCode, phone FROM address;");
            addresses.clear();
            while(resultSet.next()){
                addresses.add(new Address(
                        resultSet.getInt(ADDRESS_ID),
                        resultSet.getString(ADDRESS),
                        resultSet.getString(ADDRESS2),
                        resultSet.getInt(CITY_ID),
                        resultSet.getString(POSTAL_CODE),
                        resultSet.getString(PHONE)));
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new RuntimeException("Couldn't get address from the database");
        }
    }


    /**
     * gets the addresses ID from address data
     */
    public int getAddressId(String address, int cityId, String postalCode, String phoneNumber){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT addressId FROM address WHERE address = ? AND cityId = ? AND postalCode = ? AND phone = ?;"
            );
            preparedStatement.setString(1, address);
            preparedStatement.setInt(2, cityId);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.first();
            int id = resultSet.getInt(1);
            System.out.println("getting address ID: " + id);
            return id;
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new RuntimeException("Couldn't get address ID from database");
        }
    }

    /**
     * Gets the address info from addresses
     */
    public Address getAddressFromAddressesById(int id){
        for(Address address : addresses){
            if(address.addressId == id){
                return address;
            }
        }
        throw new RuntimeException("couldn't find address in addresses");
    }

    /**
     * Updates address information
     */
    public void updateAddress(Address address){
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE address " +
                            "SET address = ?, address2 = ?, postalCode = ?, phone = ?" +
                            " WHERE addressId = ?;"
            );
            statement.setString(1 , address.address);
            statement.setString(2 , address.address2);
            statement.setString(3 , address.postalCode);
            statement.setString(4 , address.phone);
            statement.setInt(5, address.addressId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't update address in database");
        }
    }
}

