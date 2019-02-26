package com.blackdartq.WguDatabaseProject.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

class FxUtil{
    private final String PATH_TO_FXML = "/com/blackdartq/WguDatabaseProject/FXML/";
    private final String PATH_TO_LOGFILES = "src/com/blackdartq/WguDatabaseProject/LogFiles/";
    final public String LOGIN_FXML = PATH_TO_FXML + "Login.fxml";
    final public String MAIN_FXML = PATH_TO_FXML + "MainView.fxml";
    final public String LOG_FILE = PATH_TO_LOGFILES + "login.txt";

    //+++++++++ Stage functions ++++++++++
    public Stage getStage(Control control){
        return (Stage)  control.getScene().getWindow();
    }

    public void changeSceneTo(String fxmlFIle, Object controller, Stage stage){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFIle));
        loader.setController(controller);
        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage.setScene(new Scene(parent));
        stage.show();
    }

    public void changeSceneTo(String fxmlFile, Object controller, Control control){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        loader.setController(controller);
        try {
            Parent parent = loader.load();
            Stage stage = this.getStage(control);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
    }
}

public class ControllerUtil<T> extends FxUtil{

    //+++++++++ control functions ++++++++++
    public String getTextFieldText(TextField textField){
        return textField.getText();
    }

    public int getTextFieldInt(TextField textField){
        return Integer.parseInt(textField.getText());
    }

    public double getTextFieldDouble(TextField textField){
        return Double.parseDouble(textField.getText());
    }

    public void setTextField(TextField textField, Object object){
        if(object instanceof Integer || object instanceof Double){
            textField.setText(String.valueOf(object));
        }else{
            textField.setText((String) object);
        }
    }

}
