package com.blackdartq.WguDatabaseProject.Controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.lang.invoke.SwitchPoint;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainViewController extends ControllerUtil {

    private int monthMultiplier = 0;
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
//    public void generateCalendar(){
//        GridPane gridPane = new GridPane();
//        gridPane.setStyle("-fx-border-color: black; -fx-border-width: 3");
//        gridPane.setAlignment(Pos.CENTER);
//        gridPane.setPrefSize(890, 705);
//        gridPane.setGridLinesVisible(true);
//        for(int row = 0; row < 7; row++){
//            for(int column = 0; column < 8; column++){
//                Label label = new Label();
//                label.setText("test");
//                gridPane.add(label, column, row);
//            }
//        }
//        calendarView.getChildren().add(gridPane);
//
//    }

    @FXML
    public void loadMonthGridPane(){
        monthGridPane.setVisible(true);
        weekGridPane.setVisible(false);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MONTH + monthMultiplier);
        String currentMonth = new SimpleDateFormat("dd/MM/YYYY").format(calendar.getTime());
        monthWeekLabel.setText(currentMonth);

         calendar.set(calendar.DAY_OF_MONTH,  calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        String beginningDay = new SimpleDateFormat("EEEE").format(calendar.getTime());

        calendar.set(calendar.DAY_OF_MONTH,  calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int finalDay = Integer.parseInt(new SimpleDateFormat("d").format(calendar.getTime()));
        System.out.println(finalDay);


        int startingColumn = 0;
        switch (beginningDay){

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
        System.out.println(beginningDay);



        boolean changedCount = false;
        int count = 1;
        outerloop:
        for(int row = 1; row < 7; row++){
            for(int column = startingColumn; column < 7; column++){
                if(count > finalDay && !changedCount){
                    count = 1;
                    changedCount = true;
                }
                Label label = new Label();
                label.setFont(Font.font(18));
                label.setText(String.valueOf(count));
                monthGridPane.add(label, column, row);
                this.labelArrayList.add(label);
                count++;
            }
            startingColumn = 0;
        }


    }

    @FXML
    public void loadWeekGridPane(){
        monthGridPane.setVisible(false);
        weekGridPane.setVisible(true);
    }

    public void onPrevButtonClicked(){
        monthMultiplier--;
        clearAndReloadMonthGridPane();
    }

    public void onNextButtonClicked(){
        monthMultiplier++;
        clearAndReloadMonthGridPane();
    }

    //---------------------------

    //++++++ com.blackdartq.WguDatabaseProject.FXML Control Helpers ++++++
    public void clearAndReloadMonthGridPane(){
        for(Label label : this.labelArrayList){
            monthGridPane.getChildren().remove(label);
        }
        loadMonthGridPane();
    }

    //---------------------------
}

class CellData{
    public String dayNumber;
    public String appointmentName;
}
