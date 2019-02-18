package com.blackdartq.WguDatabaseProject;

import DatabaseUtil.UserDB;
import com.blackdartq.WguDatabaseProject.Controller.ControllerUtil;
import com.blackdartq.WguDatabaseProject.Controller.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

//        DatabaseUtil databaseUtil = new DatabaseUtil();
//        databaseUtil.createTables();
//        databaseUtil.dropTables();
        UserDB userDB = new UserDB();
        userDB.createTables();
        userDB.addRow();
        userDB.addRow();
        userDB.addRow();
        userDB.getUser("userName");
        userDB.deleteByUserId(2);
        userDB.getUser("userName");
        userDB.dropTables();
//        userDB.dropTables();

//        ControllerUtil controllerUtil = new ControllerUtil();
//        controllerUtil.changeSceneTo(controllerUtil.LOGIN_FXML, new LoginController(), primaryStage);
        Platform.exit();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
