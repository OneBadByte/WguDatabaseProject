package com.blackdartq.WguDatabaseProject.Controllers;

import com.blackdartq.WguDatabaseProject.DatabaseUtil.*;
import javafx.application.Platform;
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

    enum Action {
        ADD,
        MODIFY
    }

    Action currentAction = Action.ADD;

    /**
     * Checks if the action being executed is adding or modifying data
     * in the database
     *
     * @return
     */
    public boolean checkIfModifing() {
        return currentAction == Action.MODIFY;
    }


    // Database connections
    private CustomerDB customerDB = new CustomerDB();
    private AddressDB addressDB = new AddressDB();
    private AppointmentDB appointmentDB = new AppointmentDB();

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
        generateMonthGridPane();

        // loads data for the customer and appointment list views
        fillOutListView(customerDB.getAllCustomerNames(), customerListView);
        fillOutListView(appointmentDB.getAppointmentTitles(), appointmentListView);
        fillOutChoiceBox(countryChoiceBox, addressDB.getCountries());
        fillOutChoiceBox(appointmentCustomerNameChoiceBox, customerDB.getAllCustomerNames());

        Runnable r = () -> {
            while (true) {
                if (appointmentDB.checkAppointmentStartTimes()) {
                    sendAnAlert("appointment starts soon", ColorPicker.YELLOW);
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException("couldn't sleep?");
                }
            }
//            throw new RuntimeException("Alert thread died");
        };
        Thread thread = new Thread(r);
        thread.start();
//        customerDB.dropTables();
    }

    //++++++ com.blackdartq.WguDatabaseProject.FXML Controls ++++++

    // Labels
    @FXML
    Label monthWeekLabel;
    @FXML
    Label alertLabel;

    // Date Picker
    @FXML
    DatePicker appointmentStartDatePicker;
    @FXML
    DatePicker appointmentEndDatePicker;

    // split choice box
    @FXML
    ChoiceBox countryChoiceBox;
    @FXML
    ChoiceBox appointmentCustomerNameChoiceBox;

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

    @FXML
    TextField appointmentTitleTextField;
    @FXML
    TextField appointmentUrlTextField;
    @FXML
    TextField appointmentStartTextField;
    @FXML
    TextField appointmentEndTextField;

    // Text Areas
    @FXML
    TextArea appointmentDescriptionTextArea;
    @FXML
    TextArea appointmentLocationTextArea;
    @FXML
    TextArea appointmentContactTextArea;
    @FXML
    TextArea appointmentTypeTextArea;

    // Buttons
    @FXML
    Button customerSaveButton;
    @FXML
    Button customerCancelButton;

    @FXML
    Button appointmentSaveButton;
    @FXML
    Button appointmentCancelButton;

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
     * Generates the labels in the month grid pane
     */
    @FXML
    public void generateMonthGridPane() {
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
     * generates the week gridpane by adding labels into the cells
     */
    @FXML
    public void generateWeekGridPane() {
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
            int index = getIndexInListView(customerListView);
            customerNameTextField.setText(customerDB.getCustomerNameByIndex(index));

            // fills out address text fields
            int addressId = customerDB.getCustomerAddressIdByIndex(index);
            Address addressData = addressDB.getAddressFromAddressesById(addressId);
            customerAddressTextField.setText(addressData.address);
            customerPostalCodeTextField.setText(addressData.postalCode);
            customerPhoneNumberTextField.setText(addressData.phone);

            // fills out city text fields
            City cityData = addressDB.getCityFromCitiesByCityId(addressData.cityId);
            customerCityTextField.setText(cityData.cityName);

            // fills out country fields
            String countryName = addressDB.getCountryNameFromCountriesById(cityData.countryId);
            countryChoiceBox.getSelectionModel().select(countryName);
        }
    }

    /**
     * Handles the cancel buttons for both customer and appointment
     */
    @FXML
    public void onCancelButtonClicked() {
        resetCustomerFields();
        resetAppointmentFields();
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
            generateMonthGridPane();
        } else {
            weekMultiplier--;
            clearGridPane(weekGridPane);
            generateWeekGridPane();
        }
    }

    //+++++++++++++++++ Customer controls ++++++++++++++++++++++++

    /**
     * Sends an alert to the user using the alert label
     */
    private void sendAnAlert(String message, ColorPicker colors) {
        Platform.runLater(() -> {
            alertLabel.setText(message);
        });
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
     * Deletes a customer from the database
     */
    @FXML
    public void onDeleteCustomerButtonClicked() {
        if (customerDB.getCustomersSize() == 0) {
            return;
        }
        // deletes the index from the database
        customerDB.deleteCustomerByIndex(getIndexInListView(customerListView));
        // clears and refills the listview
        customerListView.getItems().clear();
        fillOutListView(customerDB.getAllCustomerNames(), customerListView);
//        customerDB.dropTables();
    }

    /**
     * Handler for the customer save button
     */
    @FXML
    public void onCustomerSaveButtonClicked() {
        if (!checkAllCustomerFieldsFilledOut()) {
            return;
        }
        if (checkIfModifing()) {
            modifyCustomerDataToDatabase();
        } else {
            addCustomerDataToDatabase();
        }

        // fills out the list views with updated data
        fillOutListView(customerDB.getAllCustomerNames(), customerListView);
        resetCustomerFields();
        switchViabilityOfGridPane(GridPanes.MONTH);
        sendAnAlert("", ColorPicker.GREEN);
    }


    //+++++++++++++++++++++ Appointment controls ++++++++++++++++++++++++++

    /**
     * Loads customer names into the choice box
     */
    @FXML
    public void onCustomerNameChoiceBoxClicked() {
        fillOutChoiceBox(appointmentCustomerNameChoiceBox, customerDB.getAllCustomerNames());
    }

    /**
     *
     */
    @FXML
    public void onAddAppointmentButtonClicked() {
        currentAction = Action.ADD;
        switchViabilityOfGridPane(GridPanes.APPOINTMENT);
    }

    /**
     * loads up all fields when the appointment modify buttons clicked
     */
    @FXML
    public void onModifyAppointmentButtonClicked() {
        currentAction = Action.MODIFY;
        int appointmentIndex = getIndexInListView(appointmentListView);
        Appointment appointment = appointmentDB.getAppointmentFromIndex(appointmentIndex);
        appointmentTitleTextField.setText(appointment.title);
        appointmentUrlTextField.setText(appointment.url);
//        appointmentStartTextField.setText(appointment.url);
//        appointmentStartTextField.setText(appointment.);
        appointmentDescriptionTextArea.setText(appointment.description);
        appointmentLocationTextArea.setText(appointment.location);
        appointmentContactTextArea.setText(appointment.contact);
        appointmentTypeTextArea.setText(appointment.type);
        int customerIndex = customerDB.getCustomerIndexById(appointment.customerId);
        appointmentCustomerNameChoiceBox.getSelectionModel().select(customerIndex);

        switchViabilityOfGridPane(GridPanes.APPOINTMENT);
    }

    /**
     * handles saving appointment data gathering and saving to the database
     */
    @FXML
    public void onAppointmentSaveButtonClicked() {
        // checks that all the fields are filled out
        if (!checkAllAppointmentFieldsFilledOut()) {
            return;
        }
        if (checkIfModifing()) {
            int appointmentIndex = getIndexInListView(appointmentListView);
            Appointment appointment = appointmentDB.getAppointmentFromIndex(appointmentIndex);
            int customer_index = getIndexInChoiceBox(appointmentCustomerNameChoiceBox);
            appointment.customerId = customerDB.getCustomerIdByIndex(customer_index);
            appointment.title = appointmentTitleTextField.getText();
            appointment.description = appointmentDescriptionTextArea.getText();
            appointment.location = appointmentLocationTextArea.getText();
            appointment.contact = appointmentContactTextArea.getText();
            appointment.type = appointmentTypeTextArea.getText();
            appointmentDB.updateAppointment(appointment);
        } else {
            // Gets all the data from the UI and saves to the database
            Appointment appointment = new Appointment();
            int customer_index = getIndexInChoiceBox(appointmentCustomerNameChoiceBox);
            appointment.customerId = customerDB.getCustomerIdByIndex(customer_index);
            appointment.title = appointmentTitleTextField.getText();
            appointment.description = appointmentDescriptionTextArea.getText();
            appointment.location = appointmentLocationTextArea.getText();
            appointment.contact = appointmentContactTextArea.getText();
            appointment.type = appointmentTypeTextArea.getText();
            appointmentDB.addAppointment(appointment);
        }
        // fills out appointment list view and switch to the month gridpane
        fillOutListView(appointmentDB.getAppointmentTitles(), appointmentListView);
        switchViabilityOfGridPane(GridPanes.MONTH);
        sendAnAlert("", ColorPicker.GREEN);
    }

    /**
     * Handler for deleting appointments based on index in appointment listview
     */
    @FXML
    public void onDeleteAppointmentButtonClicked() {
        int index = getIndexInListView(appointmentListView);
        appointmentDB.deleteByIndex(index);
        appointmentDB.getAppointmentsFromDatabase();
        fillOutListView(appointmentDB.getAppointmentTitles(), appointmentListView);
    }

    // ++++++++++++++++++++++++ Calendar controls ++++++++++++++++++++++++++++++++

    /**
     * Changes the data generated into the calendar grid panes
     */
    @FXML
    public void onNextButtonClicked() {
        if (monthGridPane.isVisible()) {
            monthMultiplier++;
            clearGridPane(monthGridPane);
            generateMonthGridPane();
        } else {
            weekMultiplier++;
            clearGridPane(weekGridPane);
            generateWeekGridPane();
        }
    }


    /**
     * Drops every table in the database
     */
    @FXML
    public void deleteDatabase() {
        customerDB.dropTables();
        sendAnAlert("deleting database!!!", ColorPicker.RED);
        Platform.exit();
    }
    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++

    /**
     * Saves modifies the customer info to the database
     */
    public void modifyCustomerDataToDatabase() {
        // Updates the customer portion of the database
        String customerName = customerNameTextField.getText();
        int selectedIndex = getIndexInListView(customerListView);
        int customerId = customerDB.getCustomerIdByIndex(selectedIndex);
        customerDB.updateCustomer(customerId, customerName);

        // Updates the address portion of the database
        Address address = addressDB.getAddressFromAddressesById(
                customerDB.getCustomerAddressIdByIndex(selectedIndex)
        );
        address.address = customerAddressTextField.getText();
        address.address2 = customerAddress2TextField.getText();
        address.postalCode = customerPostalCodeTextField.getText();
        address.phone = customerPhoneNumberTextField.getText();
        addressDB.updateAddress(address);

        // Updates the city portion of the database
        City city = addressDB.getCityFromCitiesByCityId(address.cityId);
        city.cityName = customerCityTextField.getText();
        city.countryId = addressDB.getCountryIdByIndex(getIndexInChoiceBox(countryChoiceBox));
        addressDB.updateCity(city);

        // adding data to database tables
    }

    /**
     * Saves all the customer info to the database
     */
    public void addCustomerDataToDatabase() {
        // gets data from text fields
        String customerName = customerNameTextField.getText();
        String phoneNumber = customerPhoneNumberTextField.getText();
        String address = customerAddressTextField.getText();
        String city = customerCityTextField.getText();
        String postalCode = customerPostalCodeTextField.getText();
        int countryId = addressDB.getCountryIdByIndex(getIndexInChoiceBox(countryChoiceBox));

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
    private boolean checkAllAppointmentFieldsFilledOut() {
        boolean output = true;
        TextField[] textFields = {
                appointmentTitleTextField,
//                appointmentUrlTextField,
                appointmentStartTextField,
                appointmentEndTextField
        };

//        TextArea[] textAreas = {
//                appointmentDescriptionTextArea,
//                appointmentContactTextArea,
//                appointmentLocationTextArea,
//                appointmentTypeTextArea
//        };

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
     * resets all the fields in the customer info pane
     */
    private void resetAppointmentFields() {
        appointmentTitleTextField.clear();
        appointmentUrlTextField.clear();
        appointmentStartTextField.clear();
        appointmentEndTextField.clear();

        appointmentDescriptionTextArea.clear();
        appointmentLocationTextArea.clear();
        appointmentContactTextArea.clear();
        appointmentTypeTextArea.clear();
    }

    /**
     * Creates a new calendar to be used in other functions
     */
    private Calendar createCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + monthMultiplier);
        calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + weekMultiplier);
        return calendar;
    }

    /**
     * Creates a new calendar to be used in other functions but with changes
     */
    private Calendar createCalendar(int monthDelta, int weekDelta) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + monthMultiplier + monthDelta);
        calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + weekMultiplier + weekDelta);
        return calendar;
    }

    /**
     * Creates a string of the week for the weekMonthLabel
     */
    private String getWeekDayRange() {
        Calendar calendar = createCalendar();
        return getDateFromWeekDay(calendar.get(Calendar.SUNDAY)) + " - " + getDateFromWeekDay(calendar.get(Calendar.SATURDAY));
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
//        generateMonthGridPane();
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

