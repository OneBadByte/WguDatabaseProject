package com.blackdartq.WguDatabaseProject.Controllers;

import com.blackdartq.WguDatabaseProject.DatabaseUtil.AddressDB;
import com.blackdartq.WguDatabaseProject.DatabaseUtil.CustomerDB;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

enum GridPanes{
    APPOINTMENT, MONTH, WEEK, CUSTOMER,
}

public class MainViewController extends ControllerUtil {

    private CustomerDB customerDB = new CustomerDB();
    private AddressDB addressDB = new AddressDB();

    // used to change the month for the calendars
    private int monthMultiplier = 0;


    //used to change the week for the week view calendar
    private int weekMultiplier = 0;

    // used to store the labels in the calendar so they can be removed when changing months/weeks
    private ArrayList<Label> labelArrayList = new ArrayList<>();

    public void initialize(){
//        generateCalendar();
        loadMonthGridPane();

        // loads data for the customer and appointment list views
        fillOutListView(customerDB.getAllCustomerNames(), customerListView);
        fillOutListView(customerDB.getAllCustomerIDs(), appointmentListView);

        MenuItem menuItem = (MenuItem) addressDB.getCountries();
        countryMenuButton.getItems().setAll(menuItem);

//        customerDB.dropTables();
    }

    //++++++ com.blackdartq.WguDatabaseProject.FXML Controls ++++++

    // Labels
    @FXML
    Label monthWeekLabel;
    @FXML
    Label alertLabel;

    // split menu button
    @FXML
    MenuButton countryMenuButton;

    // Input Fields
    @FXML
    TextField customerNameTextField;

    // Buttons
    @FXML
    Button customerSaveButton;

    // Panes
    @FXML
    public GridPane monthGridPane = new GridPane();
    @FXML
    public GridPane weekGridPane = new GridPane();
    @FXML
    public Pane appointmentPane = new Pane();
    @FXML
    public Pane customerPane = new Pane();

    // ListViews
    @FXML
    ListView customerListView;
    @FXML
    ListView appointmentListView;
    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control functions ++++++
    @FXML
    public void loadMonthGridPane() {
        // changes which gridpane is being viewed
        switchViabilityOfGridPane(GridPanes.MONTH);

//        // creates a calender
        monthWeekLabel.setText(getCompleteCurrentDate());

        int startingColumn = 0;
        switch (getFirstDayOfCurrentMonth()) {
            case "Monday":
                startingColumn = 1;
                break;
            case "Tuesday":
                startingColumn = 2;
                break;
            case "Wednesday":
                startingColumn = 3;
                break;
            case "Thursday":
                startingColumn = 4;
                break;
            case "Friday":
                startingColumn = 5;
                break;
            case "Saturday":
                startingColumn = 6;
                break;
            default:
            case "Sunday":
                break;
        }

        // fills in the days before the current months starting day
        int previousMonthsFinalDay = getPreviousMonthEndDay();
        for(int i = startingColumn-1; i >= 0; i--){
            addToGridPane(monthGridPane, previousMonthsFinalDay--, i, 1);
        }

        // fills in the current months days and at the end will fill in next months
        boolean changedCount = false;
        int count = 1;
        int currentMonthEndDay = getEndMonthDay();
        for(int row = 1; row < 7; row++){
            for(int column = startingColumn; column < 7; column++){
                if(count > currentMonthEndDay && !changedCount){
                    count = 1;
                    changedCount = true;
                }
                addToGridPane(monthGridPane, count, column, row);
                count++;
            }
            startingColumn = 0;
        }
    }

    @FXML
    public void loadWeekGridPane(){
        switchViabilityOfGridPane(GridPanes.WEEK);
        monthWeekLabel.setText(getWeekDayRange());
        for(int i = 1; i < 8; i++){
            addToGridPane(weekGridPane, getDayNumberFromWeekDay(i), i-1, 1);
        }
    }


    public void switchViabilityOfGridPane(GridPanes gridPanes){
        switch(gridPanes){
            case APPOINTMENT:
                appointmentPane.setVisible(true);
                customerPane.setVisible(false);
                weekGridPane.setVisible(false);
                monthGridPane.setVisible(false);
                break;
            case CUSTOMER:
                appointmentPane.setVisible(false);
                customerPane.setVisible(true);
                weekGridPane.setVisible(false);
                monthGridPane.setVisible(false);
                break;
            case WEEK:
                appointmentPane.setVisible(false);
                customerPane.setVisible(false);
                weekGridPane.setVisible(true);
                monthGridPane.setVisible(false);
                break;
            case MONTH:
                appointmentPane.setVisible(false);
                customerPane.setVisible(false);
                weekGridPane.setVisible(false);
                monthGridPane.setVisible(true);
                break;
        }
    }

    @FXML
    public void onPrevButtonClicked(){
        if(monthGridPane.isVisible()){
            monthMultiplier--;
            clearGridPane(monthGridPane);
            loadMonthGridPane();
        }else {
            weekMultiplier--;
            clearGridPane(weekGridPane);
            loadWeekGridPane();
        }
    }

    // Customer controls
    private void sendAnAlert(String message, ColorPicker colors){
        alertLabel.setText(message);
        String color = "";
        switch (colors){
            case GREEN:
                color = CustomColors.GREEN;
                break;
            case YELLOW:
                color = CustomColors.YELLOW;
                break;
            case RED:
                color = CustomColors.RED;
                break;
        }
        alertLabel.setStyle("-fx-background-color: " + color + ";");
    }

    @FXML
    public void onAddCustomerButtonClicked(){
        switchViabilityOfGridPane(GridPanes.CUSTOMER);
    }

    @FXML
    public void onModifyCustomerButtonClicked(){
        switchViabilityOfGridPane(GridPanes.CUSTOMER);
    }

    @FXML
    public void onDeleteCustomerButtonClicked(){
        if(customerDB.getCustomersSize() == 0){
           return;
        }
        // deletes the index from the database
        customerDB.deleteCustomerByIndex(getSelectedElementInListView(customerListView));
        // clears and refills the listview
        customerListView.getItems().clear();
        fillOutListView(customerDB.getAllCustomerNames(), customerListView);
//        customerDB.dropTables();
    }

    // Appointment controls
    @FXML
    public void onAddAppointmentButtonClicked(){
        switchViabilityOfGridPane(GridPanes.APPOINTMENT);
    }
    @FXML
    public void onModifyAppointmentButtonClicked(){
        switchViabilityOfGridPane(GridPanes.APPOINTMENT);
    }

    @FXML
    public void onNextButtonClicked(){
        if(monthGridPane.isVisible()){
            monthMultiplier++;
            clearGridPane(monthGridPane);
            loadMonthGridPane();
        }else {
            weekMultiplier++;
            clearGridPane(weekGridPane);
            loadWeekGridPane();
        }
    }

    @FXML
    public void onCustomerSaveButtonClicked(){
        //TODO add address
        String customerName = customerNameTextField.getText();
        sendAnAlert(customerName + " was added to the database", ColorPicker.YELLOW);
        System.out.println(customerName);
        if(customerName.equals("")){
            sendAnAlert("Enter a customer name please", ColorPicker.RED);
            return;
        }
        customerDB.addCustomer(customerName, 1);
        fillOutListView(customerDB.getAllCustomerNames(), customerListView);
        resetCustomerFields();
    }

    @FXML
    public void deleteDatabase(){
        customerDB.dropTables();
        sendAnAlert("deleting database!!!", ColorPicker.RED);
    }

    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++

    private void resetCustomerFields(){
        customerNameTextField.clear();
    }

    private Calendar createCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + monthMultiplier);
        return calendar;
    }

    private Calendar createCalendar(int monthDelta, int weekDelta){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + monthMultiplier + monthDelta);
        calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + weekMultiplier + weekDelta);
        return calendar;
    }

    private String getWeekDayRange(){
        return getDateFromWeekDay(Calendar.SUNDAY) + " - " + getDateFromWeekDay(Calendar.SATURDAY);
    }

    private int getDayNumberFromWeekDay(int day){
        Calendar calendar = createCalendar();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return Integer.parseInt(new SimpleDateFormat("dd").format(calendar.getTime()));
    }

    private String getDateFromWeekDay(int day){
        Calendar calendar = createCalendar();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return new SimpleDateFormat("dd/MM/YYYY").format(calendar.getTime());
    }

    private String getCompleteCurrentDate(){
        Calendar calendar = createCalendar();
        System.out.println(new SimpleDateFormat("dd/MM/YYYY").format(calendar.getTime()));
        return new SimpleDateFormat("dd/MM/YYYY").format(calendar.getTime());
    }

    private String getFirstDayOfCurrentMonth(){
        Calendar calendar = createCalendar();
        // sets the beginning of the month
        calendar.set(Calendar.DAY_OF_MONTH,  calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat("EEEE").format(calendar.getTime());
    }

    private int getEndMonthDay(){
        Calendar calendar = createCalendar();
        calendar.set(Calendar.DAY_OF_MONTH,  calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return Integer.parseInt(new SimpleDateFormat("d").format(calendar.getTime()));
    }
    private int getPreviousMonthEndDay(){
        Calendar calendar = createCalendar(-1, 0);
        calendar.set(Calendar.DAY_OF_MONTH,  calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return Integer.parseInt(new SimpleDateFormat("d").format(calendar.getTime()));
    }

    private void clearGridPane(GridPane gridPane){
        for(Label label : this.labelArrayList){
            gridPane.getChildren().remove(label);
        }
//        loadMonthGridPane();
    }

    private void addToGridPane(GridPane gridPane, int text, int column, int row){
        Label label = new Label();
        label.setFont(Font.font(18));
        label.setText(String.valueOf(text));
        gridPane.add(label, column, row);
        this.labelArrayList.add(label);
    }

    //---------------------------
}

