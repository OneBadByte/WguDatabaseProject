package com.blackdartq.WguDatabaseProject.CommonUtil;

import java.time.LocalDateTime;

public class CommonUtil {
    public static boolean localDateTimeAfter(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        int[] dateTime = {
                localDateTime1.getYear() - localDateTime2.getYear(),
                localDateTime1.getMonthValue() - localDateTime2.getMonthValue(),
                localDateTime1.getDayOfMonth() - localDateTime2.getDayOfMonth(),
                localDateTime1.getHour() - localDateTime2.getHour(),
                localDateTime1.getMinute() - localDateTime2.getMinute(),
        };
        for (int t: dateTime){
            System.out.print(t + " ");
            if(t < 0){
                System.out.println(" ");
                return false;
            }
        }
        System.out.println(" ");
        return true;
    }

    public static boolean localDateTimeBefore(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        int[] dateTime = {
                localDateTime1.getYear() - localDateTime2.getYear(),
                localDateTime1.getMonthValue() - localDateTime2.getMonthValue(),
                localDateTime1.getDayOfMonth() - localDateTime2.getDayOfMonth(),
                localDateTime1.getHour() - localDateTime2.getHour(),
                localDateTime1.getMinute() - localDateTime2.getMinute(),
        };
        for (int t: dateTime){
            System.out.print(t + " ");
            if(t > 0){
                System.out.println(" ");
                return false;
            }
        }
        System.out.println(" ");
        return true;
    }
}
