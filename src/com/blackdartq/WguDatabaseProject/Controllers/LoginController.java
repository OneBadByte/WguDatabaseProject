package com.blackdartq.WguDatabaseProject.Controllers;

import com.blackdartq.WguDatabaseProject.DatabaseUtil.UserDB;
import com.blackdartq.WguDatabaseProject.FileUtil.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.Locale;

public class LoginController extends ControllerUtil {

    //++++++ com.blackdartq.WguDatabaseProject.FXML Controls ++++++

    UserDB userDB = new UserDB();
    Locale locale = Locale.getDefault();

    public void initialize(){
       if(isRussianLocale()){
            setTextToRussian();
        }
    }

    private boolean isRussianLocale(){
        System.out.println(locale.getCountry());
        return locale.getCountry().equals("RS") || locale.getCountry().equals("RU");
    }

    private void setTextToRussian(){
        headerLabel.setText("Запланируйте это");
        emailTextField.setPromptText("по электронной почте");
        passwordTextField.setPromptText("пароль");
        loginButton.setText("войти в систему");
    }

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

    /**
     * checks if the login data is valid
     * sets the sets the label text to error message and changes background
     */
    @FXML
    public void onLoginButtonClicked(){
        if(validateLogin()) {
            this.changeSceneTo(this.MAIN_FXML, new MainViewController(), loginButton);
        } else{
            if(isRussianLocale()){
                incorrectUsernamePasswordLabel.setText("неверное имя пользователя или пароль");
            }else{
                incorrectUsernamePasswordLabel.setText("Username or Password was incorrect");
            }
            incorrectUsernamePasswordLabel.setStyle("-fx-background-color: #F6BFBE;");
        }
        emailTextField.clear();
        passwordTextField.clear();
    }

    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++

    /**
     * validates the users login information against the database
     */
    public boolean validateLogin(){
        String email = this.getTextFieldText(emailTextField);
        String password = this.getTextFieldText(passwordTextField);
        boolean test = userDB.validateUser(email, password);
        String message = "User: " + email + " Password: " + password +
                " from: " + locale.getDisplayCountry() + " Language: " + locale.getDisplayLanguage()
                + " Timestamp: " + LocalDateTime.now().toString();
        if(test){
            message = message + " Login: PASS";
        }else{
            message = message + " Login: FAIL";
        }
        FileUtil.appendWriteToFile(this.LOG_FILE, message);
        return test;
    }
    //---------------------------
}
