package com.blackdartq.WguDatabaseProject.DatabaseUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//class AddressHolder{
//    ArrayList<String> countries = new ArrayList<>();
//}


public class AddressDB extends DatabaseUtil {
//    ArrayList<AddressHolder> addressHolders = new ArrayList<>();
    private Collection<String> countries = new ArrayList<>();

    public Collection<String> getCountries() {
        return countries;
    }

    public void getCountriesFromDatabase(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT country FROM country;");
            countries.clear();
            while(resultSet.next()){
                countries.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

