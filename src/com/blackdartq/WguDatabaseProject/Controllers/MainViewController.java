package com.blackdartq.WguDatabaseProject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainViewController extends ControllerUtil {

    // used to change the month for the calendars
    private int monthMultiplier = 0;


    //used to change the week for the week view calendar
    private int weekMultiplier = 0;

    // used to store the labels in the calendar so they can be removed when changing months/weeks
    private ArrayList<Label> labelArrayList = new ArrayList<>();

    public void initialize(){
//        generateCalendar();
        loadMonthGridPane();
    }

    //++++++ com.blackdartq.WguDatabaseProject.FXML Controls ++++++

    // Labels
    @FXML
    Label monthWeekLabel;

    // Panes
    @FXML
    GridPane monthGridPane;
    @FXML
    GridPane weekGridPane;
    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control functions ++++++
    @FXML
    public void loadMonthGridPane(){
        // changes which gridpane is being viewed
        monthGridPane.setVisible(true);
        weekGridPane.setVisible(false);

//        // creates a calender
        monthWeekLabel.setText(getCompleteDate());

        int startingColumn = 0;
        switch (getFirstDayOfCurrentMonth()){
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
        int previousMonthsFinalDay = getEndMonthDay(-1);
        for(int i = startingColumn-1; i >= 0; i--){
            addToGridPane(monthGridPane, previousMonthsFinalDay--, i, 1);
        }

        // fills in the current months days and at the end will fill in next months
        boolean changedCount = false;
        int count = 1;
        int currentMonthEndDay = getEndMonthDay(0);
        outerloop:
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
        monthGridPane.setVisible(false);
        weekGridPane.setVisible(true);
        monthWeekLabel.setText(getWeekDayRange());
        for(int i = 1; i < 8; i++){
            addToGridPane(weekGridPane, getDayNumberFromWeekDay(i), i-1, 1);
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

    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++
    private String getWeekDayRange(){
        return getDateFromWeekDay(Calendar.SUNDAY) + " - " + getDateFromWeekDay(Calendar.SATURDAY);
    }

    private int getDayNumberFromWeekDay(int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH-1 + monthMultiplier);
        calendar.set(Calendar.WEEK_OF_MONTH, Calendar.WEEK_OF_MONTH+1 + weekMultiplier);
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return Integer.parseInt(new SimpleDateFormat("dd").format(calendar.getTime()));
    }

    private String getDateFromWeekDay(int day){
        System.out.println(getDayNumberFromWeekDay(day));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH-1 + monthMultiplier);
        calendar.set(Calendar.WEEK_OF_MONTH, Calendar.WEEK_OF_MONTH+1 + weekMultiplier);
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return new SimpleDateFormat("dd/MM/YYYY").format(calendar.getTime());
    }

    private String getCompleteDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH-1 + monthMultiplier);
        return new SimpleDateFormat("dd/MM/YYYY").format(calendar.getTime());
    }

    private String getFirstDayOfCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH-1 + monthMultiplier);

        // sets the beginning of the month
        calendar.set(Calendar.DAY_OF_MONTH,  calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat("EEEE").format(calendar.getTime());
    }

    private int getEndMonthDay(int previousMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH-1 + monthMultiplier + previousMonth);

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

class CellData{
    public String dayNumber;
    public String appointmentName;
}
