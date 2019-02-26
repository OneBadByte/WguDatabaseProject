package com.blackdartq.WguDatabaseProject;

import com.blackdartq.WguDatabaseProject.Controllers.ControllerUtil;
import com.blackdartq.WguDatabaseProject.Controllers.LoginController;
import com.blackdartq.WguDatabaseProject.Controllers.MainViewController;
import com.blackdartq.WguDatabaseProject.DatabaseUtil.UserDB;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

//        com.blackdartq.WguDatabaseProject.DatabaseUtil databaseUtil = new com.blackdartq.WguDatabaseProject.DatabaseUtil();
//        databaseUtil.createTables();
//        databaseUtil.dropTables();
        UserDB userDB = new UserDB();
//        userDB.createTables();
//        userDB.createTables();
        userDB.addRow();
        userDB.addRow();
        userDB.addRow();
        userDB.getUser("userName");
        userDB.deleteById(2);
        userDB.getUser("userName");
//        userDB.dropTables();
//        userDB.dropTables();

        ControllerUtil controllerUtil = new ControllerUtil();
        controllerUtil.changeSceneTo(controllerUtil.LOGIN_FXML, new LoginController(), primaryStage);
//        controllerUtil.changeSceneTo(controllerUtil.MAIN_FXML, new MainViewController(), primaryStage);
//        Platform.exit();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
