package com.blackdartq.WguDatabaseProject.Controllers;

import com.blackdartq.WguDatabaseProject.FileUtil.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController extends ControllerUtil {

    //++++++ com.blackdartq.WguDatabaseProject.FXML Controls ++++++
    // Labels
    @FXML
    private Label headerLabel;
    @FXML
    private Label incorrectUsernamePasswordLabel;

    // Buttons
    @FXML
    private Button loginButton;

    // TextFields
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;

    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control functions ++++++
    @FXML
    public void onLoginButtonClicked(){
        if(validateLogin()) {
            this.changeSceneTo(this.MAIN_FXML, new MainViewController(), loginButton);
        } else{
            incorrectUsernamePasswordLabel.setText("Username or Password was incorrect");
            incorrectUsernamePasswordLabel.setStyle("-fx-background-color: #F6BFBE;");
            System.out.println("Failed To Login");
        }

    }

    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++
    public boolean validateLogin(){
        String email = this.getTextFieldText(emailTextField);
        String password = this.getTextFieldText(passwordTextField);
        boolean test = email.equals("test") && password.equals("test");
        if(test){
            FileUtil.appendWriteToFile(this.LOG_FILE, "user: " + email + " logged in using: " + password);
        }else{
            FileUtil.appendWriteToFile(this.LOG_FILE, "user: " + email + " failed to log in using: " + password);
        }
        return test;
    }

    //---------------------------
}
