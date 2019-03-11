package com.blackdartq.WguDatabaseProject.Controllers;

import com.blackdartq.WguDatabaseProject.DatabaseUtil.Address;
import com.blackdartq.WguDatabaseProject.DatabaseUtil.AddressDB;
import com.blackdartq.WguDatabaseProject.DatabaseUtil.City;
import com.blackdartq.WguDatabaseProject.DatabaseUtil.CustomerDB;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

// used to switch between panes
enum GridPanes {
    APPOINTMENT, MONTH, WEEK, CUSTOMER,
}

public class MainViewController extends ControllerUtil {

    enum Action{
        ADD,
        MODIFY
    }

    Action currentAction = Action.ADD;

    /**
     * Checks if the action being executed is adding or modifying data
     * in the database
     * @return
     */
    public boolean checkIfModifing(){
        return currentAction == Action.MODIFY;
    }


    // Database connections
    private CustomerDB customerDB = new CustomerDB();
    private AddressDB addressDB = new AddressDB();

    // used to change the month for the calendars
    private int monthMultiplier = 0;

    //used to change the week for the week view calendar
    private int weekMultiplier = 0;

    // used to store the labels in the calendar so they can be removed when changing months/weeks
    private ArrayList<Label> labelArrayList = new ArrayList<>();

    /**
     * Constructor for Class
     */
    public void initialize() {
//        generateCalendar();
        loadMonthGridPane();

        // loads data for the customer and appointment list views
        fillOutListView(customerDB.getAllCustomerNames(), customerListView);
        fillOutListView(customerDB.getAllCustomerIDs(), appointmentListView);
        fillOutChoiceBox(countryChoiceBox, addressDB.getCountries());

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
    ChoiceBox countryChoiceBox;

    // Text Fields
    @FXML
    TextField customerNameTextField;
    @FXML
    TextField customerPhoneNumberTextField;
    @FXML
    TextField customerAddressTextField;
    @FXML
    TextField customerAddress2TextField;
    @FXML
    TextField customerCityTextField;
    @FXML
    TextField customerPostalCodeTextField;

    // Buttons
    @FXML
    Button customerSaveButton;
    @FXML
    Button customerCancelButton;

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

    /**
     *
     */
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
        for (int i = startingColumn - 1; i >= 0; i--) {
            addToGridPane(monthGridPane, previousMonthsFinalDay--, i, 1);
        }

        // fills in the current months days and at the end will fill in next months
        boolean changedCount = false;
        int count = 1;
        int currentMonthEndDay = getEndMonthDay();
        for (int row = 1; row < 7; row++) {
            for (int column = startingColumn; column < 7; column++) {
                if (count > currentMonthEndDay && !changedCount) {
                    count = 1;
                    changedCount = true;
                }
                addToGridPane(monthGridPane, count, column, row);
                count++;
            }
            startingColumn = 0;
        }
    }

    /**
     *
     */
    @FXML
    public void loadWeekGridPane() {
        switchViabilityOfGridPane(GridPanes.WEEK);
        monthWeekLabel.setText(getWeekDayRange());
        for (int i = 1; i < 8; i++) {
            addToGridPane(weekGridPane, getDayNumberFromWeekDay(i), i - 1, 1);
        }
    }


    /**
     * Switches between grids that the user can view
     */
    public void switchViabilityOfGridPane(GridPanes gridPanes) {
        switch (gridPanes) {
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

        // checks if the modify action is to be used in Customer pane
        if (checkIfModifing() && gridPanes == GridPanes.CUSTOMER) {
            addressDB.updateAllAddressData();
            int index = getSelectedElementInListView(customerListView);
            customerNameTextField.setText(customerDB.getCustomerNameByIndex(index));

            // fills out address text fields
            int addressId = customerDB.getCustomerAddressIdByIndex(index);
            Address addressData = addressDB.getAddressFromAddressesById(addressId);
            customerAddressTextField.setText(addressData.address);
            customerPostalCodeTextField.setText(addressData.postalCode);
            customerPhoneNumberTextField.setText(addressData.phone);

            // fills out city text field
            City cityData = addressDB.getCityFromCitiesByCityId(addressData.cityId);
            customerCityTextField.setText(cityData.cityName);
            countryChoiceBox.getSelectionModel().select(addressDB.getCountryNameFromCountriesById(cityData.countryId));
        }
    }

    @FXML
    public void onCancelButtonClicked(){
        resetCustomerFields();
        switchViabilityOfGridPane(GridPanes.MONTH);
    }

    /**
     * changes month or week that the gridpanes are showing
     */
    @FXML
    public void onPrevButtonClicked() {
        if (monthGridPane.isVisible()) {
            monthMultiplier--;
            clearGridPane(monthGridPane);
            loadMonthGridPane();
        } else {
            weekMultiplier--;
            clearGridPane(weekGridPane);
            loadWeekGridPane();
        }
    }

    // Customer controls

    /**
     * Sends an alert to the user using the alert label
     */
    private void sendAnAlert(String message, ColorPicker colors) {
        alertLabel.setText(message);
        String color = "";
        switch (colors) {
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

    /**
     * changes month or week that the gridpanes are showing
     */
    @FXML
    public void onAddCustomerButtonClicked() {
        currentAction = Action.ADD;
        switchViabilityOfGridPane(GridPanes.CUSTOMER);
    }

    /**
     * Changes to the customer pane and sets the current action to modify
     */
    @FXML
    public void onModifyCustomerButtonClicked() {
        currentAction = Action.MODIFY;
        switchViabilityOfGridPane(GridPanes.CUSTOMER);
    }

    /**
     *
     */
    @FXML
    public void onDeleteCustomerButtonClicked() {
        if (customerDB.getCustomersSize() == 0) {
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

    /**
     *
     */
    @FXML
    public void onAddAppointmentButtonClicked() {
        switchViabilityOfGridPane(GridPanes.APPOINTMENT);
    }

    /**
     *
     */
    @FXML
    public void onModifyAppointmentButtonClicked() {
        switchViabilityOfGridPane(GridPanes.APPOINTMENT);
    }

    /**
     *
     */
    @FXML
    public void onNextButtonClicked() {
        if (monthGridPane.isVisible()) {
            monthMultiplier++;
            clearGridPane(monthGridPane);
            loadMonthGridPane();
        } else {
            weekMultiplier++;
            clearGridPane(weekGridPane);
            loadWeekGridPane();
        }
    }


    /**
     * Handler for the customer save button
     */
    @FXML
    public void onCustomerSaveButtonClicked() {
        if (!checkAllCustomerFieldsFilledOut()) {
            return;
        }
        addCustomerDataToDatabase();
        fillOutListView(customerDB.getAllCustomerNames(), customerListView);
        resetCustomerFields();
        switchViabilityOfGridPane(GridPanes.MONTH);
        sendAnAlert("", ColorPicker.GREEN);
    }

    /**
     *
     */
    @FXML
    public void deleteDatabase() {
        customerDB.dropTables();
        sendAnAlert("deleting database!!!", ColorPicker.RED);
    }

    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++

    /**
     * Saves all the customer info to the database
     */
    public void addCustomerDataToDatabase() {
        String customerName = customerNameTextField.getText();
        String phoneNumber = customerPhoneNumberTextField.getText();
        String address = customerAddressTextField.getText();
        String city = customerCityTextField.getText();
        int countryId = addressDB.getCountryIdByIndex(getSelectedElementInChoiceBox(countryChoiceBox));
        String postalCode = customerPostalCodeTextField.getText();

        // adding data to database tables
        addressDB.addCity(city, countryId);
        int cityId = addressDB.getCityId(city, countryId);
        addressDB.addAddressToDatabase(address, address, cityId, postalCode, phoneNumber);
        int addressId = addressDB.getAddressId(address, cityId, postalCode, phoneNumber);
        customerDB.addCustomer(customerName, addressId);
        sendAnAlert(customerName + " was added to the database", ColorPicker.YELLOW);
    }

    /**
     * Checks that all the fields are filled out in the customer pane
     */
    public boolean checkAllCustomerFieldsFilledOut() {
        TextField[] textFields = {
                customerNameTextField,
                customerPhoneNumberTextField,
                customerAddressTextField,
                customerCityTextField,
                customerPostalCodeTextField,
        };
        boolean output = true;
        for (TextField textField : textFields) {
            if (textField.getText().equals("")) {
                textField.setStyle("-fx-border-color: grey; -fx-background-color: " + ColorPicker.RED);
                sendAnAlert("Please fill in all fields", ColorPicker.RED);
                output = false;
            } else {
                textField.setStyle("-fx-border-color: grey; -fx-background-color: white;");
            }
        }
        return output;
    }

    /**
     * resets all the fields in the customer info pane
     */
    private void resetCustomerFields() {
        customerNameTextField.clear();
        customerPostalCodeTextField.clear();
        customerCityTextField.clear();
        customerAddressTextField.clear();
        customerPhoneNumberTextField.clear();
    }

    /**
     *
     */
    private Calendar createCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + monthMultiplier);
        return calendar;
    }

    /**
     *
     */
    private Calendar createCalendar(int monthDelta, int weekDelta) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + monthMultiplier + monthDelta);
        calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + weekMultiplier + weekDelta);
        return calendar;
    }

    /**
     *
     */
    private String getWeekDayRange() {
        return getDateFromWeekDay(Calendar.SUNDAY) + " - " + getDateFromWeekDay(Calendar.SATURDAY);
    }

    /**
     *
     */
    private int getDayNumberFromWeekDay(int day) {
        Calendar calendar = createCalendar();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return Integer.parseInt(new SimpleDateFormat("dd").format(calendar.getTime()));
    }

    /**
     *
     */
    private String getDateFromWeekDay(int day) {
        Calendar calendar = createCalendar();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return new SimpleDateFormat("dd/MM/YYYY").format(calendar.getTime());
    }

    /**
     *
     */
    private String getCompleteCurrentDate() {
        Calendar calendar = createCalendar();
//        System.out.println(new SimpleDateFormat("dd/MM/YYYY").format(calendar.getTime()));
        return new SimpleDateFormat("MM/YYYY").format(calendar.getTime());
    }

    /**
     *
     */
    private String getFirstDayOfCurrentMonth() {
        Calendar calendar = createCalendar();
        // sets the beginning of the month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat("EEEE").format(calendar.getTime());
    }

    /**
     *
     */
    private int getEndMonthDay() {
        Calendar calendar = createCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return Integer.parseInt(new SimpleDateFormat("d").format(calendar.getTime()));
    }

    /**
     *
     */
    private int getPreviousMonthEndDay() {
        Calendar calendar = createCalendar(-1, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return Integer.parseInt(new SimpleDateFormat("d").format(calendar.getTime()));
    }

    /**
     *
     */
    private void clearGridPane(GridPane gridPane) {
        for (Label label : this.labelArrayList) {
            gridPane.getChildren().remove(label);
        }
//        loadMonthGridPane();
    }

    /**
     *
     */
    private void addToGridPane(GridPane gridPane, int text, int column, int row) {
        Label label = new Label();
        label.setFont(Font.font(18));
        label.setText(String.valueOf(text));
        gridPane.add(label, column, row);
        this.labelArrayList.add(label);
    }

    //---------------------------
}

