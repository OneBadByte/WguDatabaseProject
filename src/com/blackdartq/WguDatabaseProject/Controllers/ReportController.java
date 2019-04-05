package com.blackdartq.WguDatabaseProject.Controllers;

import com.blackdartq.WguDatabaseProject.DatabaseUtil.AppointmentDB;
import com.blackdartq.WguDatabaseProject.DatabaseUtil.CustomerDB;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReportController extends ControllerUtil {

    //++++++ com.blackdartq.WguDatabaseProject.FXML Controls ++++++
    AppointmentDB appointmentDB = new AppointmentDB();
    CustomerDB customerDB = new CustomerDB();
    final String[] MONTHS = {
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    // buttons
    @FXML
    Button returnButton;

    // VBoxes
    @FXML
    ScrollPane appointmentByMonthScrollPane;
    @FXML
    ScrollPane userScheduleScrollPane;
    @FXML
    private VBox appointmentByCustomerVBox;

    // Labels
    @FXML
    private Label test = new Label();
    //---------------------------

    public void initialize(){
        try{
            fillOutAppointmentsByCustomer();
            fillOutUserSchedule();
            fillOutAppointmentTypesByMonth();
        }catch (Exception e){
            System.out.println("init failed: " + e);
        }
    }

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control functions ++++++
    @FXML
    public void onReturnButtonClicked(){
        changeSceneTo(this.MAIN_FXML, new MainViewController(), returnButton);
    }

    @FXML
    public void onMonthChoiceBoxSelected(){
        fillOutAppointmentTypesByMonth();
    }

    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++

    private void fillOutAppointmentsByCustomer(){
        ArrayList<Integer> customerIds = customerDB.getAllCustomerIDs();
        for(int id : customerIds){
            appointmentByCustomerVBox.getChildren().add(
                    createAppointmentByCustomerLabel(customerDB.getCustomerNameById(id),
                    appointmentDB.getNumberOfAppointmentsByCustomerId(id))
            );
        }
    }

    private Label createAppointmentByCustomerLabel(String customerName, int amount){
        return createLabelWithText(customerName + ": " + amount);
    }

    private Label createLabelWithText(String text){
        Label label = new Label();
        label.setText(text);
        return label;
    }

    private void fillOutUserSchedule(){
        ArrayList<LocalTime> times = createWorkHoursSchedule();
        ArrayList<String> formattedTimes = new ArrayList<>();
        ListView listView = new ListView();
        for(LocalTime localTime : times){
            formattedTimes.add(createUserScheduleText(localTime));
        }
        listView.getItems().addAll(formattedTimes);
        userScheduleScrollPane.setContent(listView);
    }

    private ArrayList<LocalTime> createWorkHoursSchedule(){
        final int FIVE_PM = 17;
        final int NINE_AM = 9;
        ArrayList<LocalTime> localTimes = new ArrayList<>();
        int[] minutes = {0, 15, 30, 45};
        for(int i = NINE_AM; i < FIVE_PM; i++){
            for(int min : minutes){
                localTimes.add(LocalTime.of(i, min));
            }
        }
        return localTimes;
    }

    private String createUserScheduleText(LocalTime localTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        String text = localTime.format(dateTimeFormatter) + "     " + appointmentDB.checkIfLocalTimeBetweenTimestamps(localTime);
        return text;
    }

    private void fillOutAppointmentTypesByMonth(){
        ListView listView = new ListView();
        for(int month = 0; month < 12; month++){
//            label.setText(MONTHS[month]);
//            label.setStyle("-fx-font-weight: bold;");
            ArrayList<String> appointmentTypesInMonth = appointmentDB.checkIfLocalDateBetweenTimestamps(month+1);
            listView.getItems().add(MONTHS[month] + " Type count: " + appointmentTypesInMonth.size());
            listView.getItems().addAll(appointmentTypesInMonth);
        }
        appointmentByMonthScrollPane.setContent(listView);
    }

    //---------------------------
}


