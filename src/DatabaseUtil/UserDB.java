package DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;

public class UserDB extends DatabaseUtil implements DatabaseTemplate{

    public void addRow(){
        java.util.Date date = new java.util.Date();
        Date sqlDate = new Date(2019, 2, 16);
        Timestamp ts = new Timestamp(date.getTime());
//        System.out.println(ts.toString());
        try {
            PreparedStatement cs = connection.prepareStatement("INSERT INTO user VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)");
            // userId is set to null so it can auto increment
//            cs.setInt(1, 1);
            // username
            cs.setString(1, "test");
            // password
            cs.setString(2, "test");
            // active
            cs.setShort(3, (short) 1);
            // createDate
            cs.setDate(4, sqlDate);
            // created By
            cs.setString(5, "test");
            // lastUpdate
            cs.setTimestamp(6, ts);
            // last update by
            cs.setString(7, "test");
            cs.executeUpdate();
        } catch (SQLException e) {
//            e.printStackTrace();
            System.out.println(e);
        }
    }

    public ArrayList getUser(String columnName){
        ArrayList<Object> arrayList = new ArrayList<>();
        try {
            Statement  statement = this.connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            ResultSet resultSet = statement.executeQuery("SELECT " + columnName + " from user");
//            resultSet.findColumn(columnName);
            boolean done = false;
            int count = 1;
            resultSet.first();
            while (!resultSet.isAfterLast()){
//              resultSet.absolute(count);
              arrayList.add(resultSet.getObject(1));
              System.out.println(arrayList.get(count-1));
              count++;
              resultSet.next();
            }
            System.out.println(arrayList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public void deleteByUserId(int userId){
        try {
            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM user WHERE userId=?");
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
