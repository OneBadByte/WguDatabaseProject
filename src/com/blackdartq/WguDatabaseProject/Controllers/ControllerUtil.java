package com.blackdartq.WguDatabaseProject.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

enum ColorPicker{
    RED,
    YELLOW,
    GREEN
}
class CustomColors{
    final static String GREEN = " #439775";
    final static String YELLOW = " #ffff66";
    final static String RED = "#FF7F7F";
}

class FxUtil{
    private final String PATH_TO_FXML = "/com/blackdartq/WguDatabaseProject/FXML/";
    private final String PATH_TO_LOGFILES = "src/com/blackdartq/WguDatabaseProject/LogFiles/";
    final public String LOGIN_FXML = PATH_TO_FXML + "Login.fxml";
    final public String MAIN_FXML = PATH_TO_FXML + "MainView.fxml";
    final public String LOG_FILE = PATH_TO_LOGFILES + "login.txt";

    //+++++++++ Stage functions ++++++++++

    /**
     * gets the current stage from a control
     */
    public Stage getStageFromControl(Control control){
        return (Stage)  control.getScene().getWindow();
    }

    /**
     * Changes the javaFX scene by using an FXML file
     */
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

    /**
     * Changes the scenes of the entire program
     */
    public void changeSceneTo(String fxmlFile, Object controller, Control control){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        loader.setController(controller);
        try {
            Parent parent = loader.load();
            Stage stage = this.getStageFromControl(control);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("couldn't change scene");
        }
    }
}

public class ControllerUtil<T> extends FxUtil{

    //+++++++++ control functions ++++++++++

    /**
     *
     */
    public String getTextFieldText(TextField textField){
        return textField.getText();
    }

    /**
     *
     */
    public int getTextFieldInt(TextField textField){
        return Integer.parseInt(textField.getText());
    }

    /**
     *
     */
    public double getTextFieldDouble(TextField textField){
        return Double.parseDouble(textField.getText());
    }

    /**
     *
     */
    public void setTextField(TextField textField, Object object){
        if(object instanceof Integer || object instanceof Double){
            textField.setText(String.valueOf(object));
        }else{
            textField.setText((String) object);
        }
    }

    /**
     *
     */
    public void fillOutChoiceBox(ChoiceBox choiceBox , ArrayList<String> arrayList){
        choiceBox.getItems().setAll(arrayList);
//        for(String stuff : arrayList){
//        }
    }

    /**
     *
     */
    public int getIndexInChoiceBox(ChoiceBox choiceBox){
       return choiceBox.getSelectionModel().getSelectedIndex();
    }

    /**
     *
     */
    public void fillOutListView(ArrayList arrayList, ListView listView){
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getItems().setAll(arrayList);
    }

    /**
     *
     */
    public int getIndexInListView(ListView listView){
        int test = listView.getSelectionModel().getSelectedIndex();
        return test;
    }

}
