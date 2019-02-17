package com.blackdartq.WguDatabaseProject.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController extends ControllerUtil {

    //++++++ FXML Controls ++++++
    // Labels
    @FXML
    private Label headerLabel;

    // Buttons
    @FXML
    private Button loginButton;

    // TextFields
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;

    //---------------------------

    //++++++ FXML Control functions ++++++
    @FXML
    public void onLoginButtonClicked(){
        if(validateLogin()) {
            this.changeSceneTo(this.MAIN_FXML, new SampleController(), loginButton);
        } else{
            System.out.println("Failed To Login");
        }

    }

    //---------------------------

    //++++++ FXML Control Helpers ++++++
    public boolean validateLogin(){
        String email = this.getTextFieldText(emailTextField);
        String password = this.getTextFieldText(passwordTextField);
        boolean test = email.equals("test") && password.equals("test");
        return test;
    }

    //---------------------------
}
