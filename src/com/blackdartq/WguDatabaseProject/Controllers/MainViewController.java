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

        getWeekDayRange();

        // fills in the current months days and at the end will fill in next months
        boolean changedCount = false;
        int count = 1;
        int currentMonthEndDay = getEndMonthDay(0);
        outerloop:
        for(int column = 0; column < 7; column++){
            if(count > currentMonthEndDay && !changedCount){
                count = 1;
                changedCount = true;
            }
            addToGridPane(weekGridPane, count, column, 1);
            count++;
        }
    }

    @FXML
    public void onPrevButtonClicked(){
        monthMultiplier--;
        clearAndReloadMonthGridPane();
    }

    @FXML
    public void onNextButtonClicked(){
        monthMultiplier++;
        clearAndReloadMonthGridPane();
    }

    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++

    public void getMonday(int dayOfTheWeek){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH-1 + monthMultiplier);

    }

    public String getWeekDayRange(){
        String output = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH-1 + monthMultiplier);
        calendar.set(Calendar.WEEK_OF_MONTH, Calendar.WEEK_OF_MONTH);
        System.out.println(calendar.getTime());
        System.out.println("Max of week: " + calendar.getFirstDayOfWeek());


        return output;
    }


    public String getCompleteDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH-1 + monthMultiplier);
        return new SimpleDateFormat("dd/MM/YYYY").format(calendar.getTime());
    }

    public String getFirstDayOfCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH-1 + monthMultiplier);

        // sets the beginning of the month
        calendar.set(calendar.DAY_OF_MONTH,  calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat("EEEE").format(calendar.getTime());
    }

    public int getEndMonthDay(int previousMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH-1 + monthMultiplier + previousMonth);

        calendar.set(calendar.DAY_OF_MONTH,  calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return Integer.parseInt(new SimpleDateFormat("d").format(calendar.getTime()));

    }

    public void clearAndReloadMonthGridPane(){
        for(Label label : this.labelArrayList){
            monthGridPane.getChildren().remove(label);
        }
        loadMonthGridPane();
    }

    public void addToGridPane(GridPane gridPane, int text, int column, int row){
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
