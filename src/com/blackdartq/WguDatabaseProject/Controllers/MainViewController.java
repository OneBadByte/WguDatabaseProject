package com.blackdartq.WguDatabaseProject.Controllers;

import com.blackdartq.WguDatabaseProject.CommonUtil.CommonUtil;
import com.blackdartq.WguDatabaseProject.DatabaseUtil.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private Action currentAction = Action.ADD;

    /**
     * Checks if the action being executed is adding or modifying data
     * in the database
     *
     * @return
     */
    private boolean checkIfModifying() {
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

        // Keeps the data gathered from the database updated incase changes are made by another program
        // using the same database
        // LAMBDA EXAMPLE
        Runnable databaseKeeper = () -> {
            while (true){
                customerDB.getAllCustomersFromDatabase();
                appointmentDB.getAppointmentsFromDatabase();
                addressDB.getCountriesFromDatabase();
                addressDB.getCitiesFromDatabase();
                addressDB.getAddressesFromTheDatabase();
                try{
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };

        // starts up a thread that will check if any appointments are about to start in 15 minutes
        // LAMBDA EXAMPLE
        Runnable r = () -> {
            while (true) {
                ArrayList<Integer> appointmentsWith15Minutes = appointmentDB.getAllStartTimesWithin15Minutes();
                if(appointmentsWith15Minutes.size() > 0){
                   StringBuilder message = new StringBuilder();
                   message.append("Appointment: ");
                   for(int index : appointmentsWith15Minutes){
                       message.append(appointmentDB.getTitleFromIndex(index)).append(" starts in ");
                       message.append(appointmentDB.getMinutesTillStartFromIndex(index)).append(" minutes");
                       sendWarning(message.toString());
                   }
                }
                try {
                    Thread.sleep(10000);
                    clearAlert();
                } catch (InterruptedException e) {
                    throw new RuntimeException("couldn't sleep?");
                }
            }
        };
        Thread thread = new Thread(r);
        Thread thread2 = new Thread(databaseKeeper);
        thread.start();
        thread2.start();
    }

    //++++++ com.blackdartq.WguDatabaseProject.FXML Controls ++++++

    // Labels
    @FXML
    Label monthWeekLabel;
    @FXML
    Label alertLabel;

    // Date Picker
    @FXML
    DatePicker appointmentDatePicker;

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

    @FXML
    Button reportButton;

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
        weekMultiplier = 0;
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
        monthMultiplier = 0;
        switchViabilityOfGridPane(GridPanes.WEEK);
        monthWeekLabel.setText(getWeekDayRange());
        for (int i = 1; i < 8; i++) {
            addToGridPane(weekGridPane, getDayNumberFromWeekDay(i), i - 1, 1);
        }
    }


    /**
     * Switches between grids that the user can view
     */
    private void switchViabilityOfGridPane(GridPanes gridPanes) {
        setPaneVisible(gridPanes);
        // checks if the modify action is to be used in Customer pane
        if (checkIfModifying() && gridPanes == GridPanes.CUSTOMER) {
            addressDB.updateAllAddressData();
            int index = getIndexInListView(customerListView);
            setAllCustomerFields(index);
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
     * clears the alert bar
     */
    private void clearAlert(){
        sendAnAlert("", ColorPicker.GREEN);
    }

    /**
     * sends a warning message to the user
     */
    private void sendWarning(String message){
        sendAnAlert(message, ColorPicker.YELLOW);
    }

    /**
     * sends an error message to the user
     */
    private void sendError(String message){
        sendAnAlert(message, ColorPicker.RED);
    }

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
        if (checkIfModifying()) {
            modifyCustomerDataToDatabase();
        } else {
            addCustomerDataToDatabase();
        }

        // fills out the list views with updated data
        fillOutListView(customerDB.getAllCustomerNames(), customerListView);
        resetCustomerFields();
        switchViabilityOfGridPane(GridPanes.MONTH);
        clearAlert();
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
     * Switches to the appointment pane and clears all fields of old data
     */
    @FXML
    public void onAddAppointmentButtonClicked() {
        resetAppointmentFields();
        currentAction = Action.ADD;
        switchViabilityOfGridPane(GridPanes.APPOINTMENT);
    }

    /**
     * loads up all fields when the appointment modify buttons clicked
     */
    @FXML
    public void onModifyAppointmentButtonClicked() {
        currentAction = Action.MODIFY;
        resetAppointmentFields();
        int appointmentIndex = getIndexInListView(appointmentListView);
        Appointment appointment = appointmentDB.getAppointmentFromIndex(appointmentIndex);

        setBasicAppointmentFields(appointment);
        setAppointmentDateFields(appointmentIndex);

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
        if (!checkAppointmentFieldsFilledOut()) {
            return;
        }

        if (!checkAppointmentTimes()) {
            return;
        }

        int appointmentIndex = getIndexInListView(appointmentListView);
        Appointment appointment;

        if(checkIfModifying()){
            appointment = appointmentDB.getAppointmentFromIndex(appointmentIndex);
            getAllAppointmentFields(appointment);
            appointmentDB.updateAppointment(appointment);
        }else{
            appointment = new Appointment();
            getAllAppointmentFields(appointment);
            appointmentDB.addAppointment(appointment);
        }

        // fills out appointment list view and switch to the month gridpane
        fillOutListView(appointmentDB.getAppointmentTitles(), appointmentListView);
        switchViabilityOfGridPane(GridPanes.MONTH);
        clearAlert();
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

    // ++++++++++++++++++++++++++ Report controls ++++++++++++++++++++++++++++++++
    @FXML
    public void onReportButtonClicked(){
        changeSceneTo(this.REPORT_FXML, new ReportController(), reportButton);
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
        Platform.exit();
    }
    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++

    /**
     * Saves modifies the customer info to the database
     */
    private void modifyCustomerDataToDatabase() {
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
    private void addCustomerDataToDatabase() {
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
        sendWarning(customerName + " was added to the database");
    }

    /**
     * tests that the appointment fields are correctly filled out and the date picker is not null
     */
    private boolean checkAppointmentTimes(){
        if(appointmentDatePicker.getValue() == null){
            sendError("Enter a date please!");
            return false;
        }
        LocalDateTime start = getLocalDateTimeFromAppointmentFields(appointmentStartTextField, appointmentDatePicker);
        LocalDateTime end = getLocalDateTimeFromAppointmentFields(appointmentEndTextField, appointmentDatePicker);
        int appointmentId;

        Boolean output = true;
        Boolean test1 = appointmentDatePicker.valueProperty().isNotNull().get();
        Boolean test2 = isTimeTextFieldWithinBusinessHours(appointmentStartTextField);
        Boolean test3 = isTimeTextFieldWithinBusinessHours(appointmentEndTextField);
        Boolean test4 = !CommonUtil.localDateTimeAfter(start, end);
        Boolean test5 = true;

        if(checkIfModifying()){
            appointmentId = appointmentDB.getAppointmentIdFromIndex(getIndexInListView(appointmentListView));
            test5 = !appointmentDB.isAlreadyScheduled(start, end, appointmentId);
        }else{
            test5 = !appointmentDB.isAlreadyScheduled(start, end);
        }
        Boolean test6 = validateTimeFormat(appointmentStartTextField);
        Boolean test7 = validateTimeFormat(appointmentEndTextField);

        // tests
        output = validateControl(test1, appointmentDatePicker, "Please add start and end dates");
        if(!output){
            return false;
        }
        output = validateControl(test2, appointmentStartTextField, "please enter a time within business hours");
        if(!output){
            return false;
        }
        output = validateControl(test3, appointmentEndTextField, "please enter a time within business hours");
        if(!output){
            return false;
        }
        output = validateControl(test4, appointmentStartTextField, appointmentEndTextField,
                "Please enter a start time before the end time");
        if(!output){
            return false;
        }
        output = validateControl(test5, appointmentStartTextField, appointmentEndTextField,
        "Time section already scheduled for!");
        if(!output){
            return false;
        }

        // checks if the time fields for start and end are filled out correctly
        output = validateControl(test6, appointmentStartTextField, "Please fill out end time correctly  EX: 12:23 PM");
        if(!output){
            return false;
        }
        output = validateControl(test7, appointmentStartTextField, "Please fill out end time correctly  EX: 12:23 PM");
        return output;
    }

    /**
     * Checks that all the fields are filled out in the customer pane
     */
    private boolean checkAppointmentFieldsFilledOut() {
        boolean output = true;
        TextField[] textFields = {
                appointmentTitleTextField,
                appointmentUrlTextField,
                appointmentStartTextField,
                appointmentEndTextField
        };

        TextArea[] textAreas = {
                appointmentDescriptionTextArea,
                appointmentLocationTextArea,
                appointmentContactTextArea,
                appointmentTypeTextArea
        };

        for (TextField textField : textFields) {
            boolean test = validateControl(!textField.getText().isEmpty(),
                    textField, "Please fill out all fields");
            if(!test){
                output = false;
            }
        }

        for (TextArea textArea: textAreas) {
            boolean test = validateControl(!textArea.getText().isEmpty(),
                    textArea, "Please fill out all fields");
            if(!test){
                output = false;
            }
        }
        return output;
    }

    private boolean stringIsAllNumbers(String string){
        try{
            Long.parseLong(string);
            return true;
        }catch (Exception e){
            return false;
        }
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
            boolean test = validateControl(!textField.getText().isEmpty(), textField, "Please fill out all customer fields");
            if (!test) {
                output = false;
            }
        }

        boolean test1 = stringIsAllNumbers(customerPhoneNumberTextField.getText());
        boolean test2 = stringIsAllNumbers(customerPostalCodeTextField.getText());

       boolean check1 = validateControl(test1, customerPhoneNumberTextField, "please enter only numbers in phone number field");
       boolean check2 = validateControl(test2, customerPostalCodeTextField, "please enter only numbers in postal code field");
       if(!check1 || !check2){
           return false;
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
        appointmentCustomerNameChoiceBox.getSelectionModel().clearSelection();
        appointmentTitleTextField.clear();
        appointmentUrlTextField.clear();

        appointmentDescriptionTextArea.clear();
        appointmentLocationTextArea.clear();
        appointmentContactTextArea.clear();
        appointmentTypeTextArea.clear();

        appointmentStartTextField.clear();
        appointmentEndTextField.clear();
        appointmentDatePicker.getEditor().clear();
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
        return getDateFromWeekDay(calendar.getFirstDayOfWeek()) + " - " + getDateFromWeekDay(calendar.getFirstDayOfWeek() + 6);
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
        return new SimpleDateFormat("MM/dd/YYYY").format(calendar.getTime());
    }

    /**
     *
     */
    private String getCompleteCurrentDate() {
        Calendar calendar = createCalendar();
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

    public boolean validateTimeFormat(TextField textField){
        String[] time = textField.getText().split(" ");
        if(time.length != 2){
            return false;
        }
        String[] hourMin = time[0].split(":");
        if(hourMin.length != 2){
            return false;
        }
        String amPm = time[1].toUpperCase();
        switch(amPm){
            case "AM":
            case "PM":
                return true;
            default:
                return false;
        }
    }

    private boolean isTimeTextFieldWithinBusinessHours(TextField textField){
        String[] time = textField.getText().split(" ");
        String[] hourMin = time[0].split(":");
        if(time.length != 2 || hourMin.length != 2){
            sendAnAlert("Please add AM/PM to the time boxes", ColorPicker.RED);
            return false;
        }
        String amPm = time[1].toUpperCase();
        int hours = Integer.valueOf(hourMin[0]);
        if(hours == 12 && amPm.equals("PM")){
            return true;
        }
        if(hours >= 5 && amPm.equals("PM") || hours < 9 && amPm.equals("AM")){
            sendAnAlert("Please enter a time within business hours (9AM - 5PM)", ColorPicker.RED);
            return false;
        }
        return true;
    }

    private LocalDateTime getLocalDateTimeFromAppointmentFields(TextField textField, DatePicker datePicker){
        // TODO validate these two lines below
        String[] time = textField.getText().split(" ");
        String[] hourMin = time[0].split(":");
        String amPm = time[1].toUpperCase();
        int hours = Integer.valueOf(hourMin[0]);
        int min = Integer.valueOf(hourMin[1]);
        if(amPm.equals("PM") && hours != 12){
            hours += 12;
        }else if(amPm.equals("AM") && hours == 12){
            hours = 0;
        }
        LocalTime localTime = LocalTime.of(hours, min);
        LocalDate localDate = datePicker.getValue();
        return LocalDateTime.of(localDate, localTime);
    }

    private void setAllCustomerFields(int customerIndex){
        customerNameTextField.setText(customerDB.getCustomerNameByIndex(customerIndex));

        // fills out address text fields
        int addressId = customerDB.getCustomerAddressIdByIndex(customerIndex);
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

    /**
     * Get all appointment fields
     */
    private void getAllAppointmentFields(Appointment appointment){
        int customer_index = getIndexInChoiceBox(appointmentCustomerNameChoiceBox);
        appointment.customerId = customerDB.getCustomerIdByIndex(customer_index);
        appointment.title = appointmentTitleTextField.getText();
        appointment.description = appointmentDescriptionTextArea.getText();
        appointment.location = appointmentLocationTextArea.getText();
        appointment.contact = appointmentContactTextArea.getText();
        appointment.type = appointmentTypeTextArea.getText();
        appointment.url = appointmentUrlTextField.getText();

        LocalDateTime start = getLocalDateTimeFromAppointmentFields(appointmentStartTextField, appointmentDatePicker);
        LocalDateTime end = getLocalDateTimeFromAppointmentFields(appointmentEndTextField, appointmentDatePicker);
        appointment.start = Timestamp.valueOf(start);
        appointment.end = Timestamp.valueOf(end);
    }

    private void setBasicAppointmentFields(Appointment appointment){
        // gets values of the normal text fields/areas
        appointmentTitleTextField.setText(appointment.title);
        appointmentDescriptionTextArea.setText(appointment.description);
        appointmentLocationTextArea.setText(appointment.location);
        appointmentContactTextArea.setText(appointment.contact);
        appointmentTypeTextArea.setText(appointment.type);
        appointmentUrlTextField.setText(appointment.url);
    }

    private void setAppointmentDateFields(int appointmentIndex) {
        // sets the time and date for the start and end sections
        LocalDate date = appointmentDB.getStartLocalDateTimeByIndex(appointmentIndex).toLocalDate();
        appointmentDatePicker.valueProperty().set(date);
        appointmentStartTextField.setText(appointmentDB.getStartTimeFromIndex(appointmentIndex));
        appointmentEndTextField.setText(appointmentDB.getEndTimeFromIndex(appointmentIndex));
    }

    private void setPaneVisible(GridPanes gridPane){
        switch (gridPane) {
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

    public boolean validateControl(Boolean test, Control control){
        if(!test){
            turnControlRed(control);
            return false;
        }
        turnControlWhite(control);
        return true;
    }

    public boolean validateControl(Boolean test, Control control, String message){
        if(!test){
            turnControlRed(control);
            sendError(message);
            return false;
        }
        turnControlWhite(control);
        clearAlert();
        return true;
    }

    public boolean validateControl(Boolean test, Control control, Control control2, String message){
        if(!test){
            turnControlRed(control);
            turnControlRed(control2);
            sendError(message);
            return false;
        }
        turnControlWhite(control);
        turnControlWhite(control2);
        clearAlert();
        return true;
    }
    //---------------------------
}

