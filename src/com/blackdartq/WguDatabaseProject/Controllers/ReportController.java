package com.blackdartq.WguDatabaseProject.Controllers;

import com.blackdartq.WguDatabaseProject.DatabaseUtil.AppointmentDB;
import com.blackdartq.WguDatabaseProject.DatabaseUtil.CustomerDB;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ReportController extends ControllerUtil {

    //++++++ com.blackdartq.WguDatabaseProject.FXML Controls ++++++
    AppointmentDB appointmentDB = new AppointmentDB();
    CustomerDB customerDB = new CustomerDB();

    // buttons
    @FXML
    Button returnButton;

    // VBoxes
    @FXML
    VBox appointmentByMonthVBox;
    @FXML
    VBox userScheduleVbox;
    @FXML
    private VBox appointmentByCustomerVBox;

    // Choice box
    @FXML
    private ChoiceBox monthChoiceBox;

    // Labels
    @FXML
    private Label test = new Label();
    //---------------------------

    public void initialize(){
        try{
            fillOutMonthChoiceBox();
            fillOutAppointmentsByCustomer();
        }catch (Exception e){
            System.out.println("init failed: " + e);
        }
    }

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control functions ++++++
    @FXML
    public void onReturnButtonClicked(){
        changeSceneTo(this.MAIN_FXML, new MainViewController(), returnButton);
    }

    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++

    /**
     * Fills out the choice box with months
     */
    public void fillOutMonthChoiceBox(){
        String[] months = {
                "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        fillOutChoiceBox(monthChoiceBox, months);
    }

    public void fillOutAppointmentsByCustomer(){
        ArrayList<Integer> customerIds = customerDB.getAllCustomerIDs();
        int count = 1;
        for(int id : customerIds){
            appointmentByCustomerVBox.getChildren().add(
                    createLabel(customerDB.getCustomerNameById(id),
                    appointmentDB.getNumberOfAppointmentsByCustomerId(id))
            );
            System.out.println("customers: " + count);
            count++;
        }
    }

    public Label createLabel(String customerName, int amount){
        Label label = new Label();
        label.setText(customerName + ": " + amount);
        return label;
    }



    //---------------------------
}


