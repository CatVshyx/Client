package com.example.client.util;

import com.example.client.model.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;


public class Counter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d uuuu");
    private static LinkedHashMap<String, int[]> weeks;
    private static LinkedHashMap<String,int[]> months;
    private static LinkedHashMap<String,int[]> years;

    public static LinkedHashMap<String,int[]> countByWeeks(Date[] arr, int limit){
        // limit - means how many weeks are to be counter from current weeks (for example: 4 weeks - means last 4 weeks, and so on)
        if (arr == null || arr.length == 0) return null;
        weeks = new LinkedHashMap<>();
        int[] values = new int[7];
        int j = 0;
        int counted = Math.max(arr.length - limit * 7, 0);
        LocalDate startDate = arr[counted].getDate();
        for(int i = counted; i < arr.length; i++){
            LocalDate localDate = arr[i].getDate();
            values[j++] = arr[i].getSold();
            if(j == 7 || arr.length - i == 1){

                weeks.put(startDate.format(formatter) + '\n' + localDate.format(formatter),values);
                startDate = localDate.plusDays(1);
                values = new int[7];
                j = 0;
            }
        }
//        System.out.println("WEEKS "+weeks);
        return weeks;
    }
    public static LinkedHashMap<String,int[]> countByMonths(Date[] arr,int limit){
        // limit - is the number of months to be counted. Each month is an array of 2 days counted
        if (arr == null || arr.length == 0) return null;
        months =  new LinkedHashMap<>();
        Month startMonth = arr[0].getDate().getMonth();
        int[] values = new int[startMonth.maxLength()];
        int counted = Math.max(arr.length - limit * 7, 0);
        for(int i = 0; i < arr.length; i++){
            LocalDate localDate = arr[i].getDate();
            if(localDate.getMonth().compareTo(startMonth) != 0 || arr.length - i == 1){
                months.put(startMonth.name(),values);

                startMonth = localDate.getMonth();
                values = new int[startMonth.maxLength()];
            }
            values[localDate.getDayOfMonth() - 1 ] = arr[i].getSold();
        }
        LinkedHashMap<String,int[]> newMap = new LinkedHashMap<>();
        months.keySet().stream().skip(counted).forEach((selected ) -> newMap.put(selected,months.get(selected)));
//        System.out.println("WEEKS "+newMap);
        return newMap;
    }
    public static  LinkedHashMap<String,int[]> countByYears(Date[] arr,int limit){
        // limit here refers to how many years (from current one) are to be shown
        // in the future can update all int[] to []long
        if (arr == null || arr.length == 0) return null;
        years = new LinkedHashMap<>();
        LocalDate startDate = arr[0].getDate();
        int [] values = new int[12];
        for(int i = 0; i < arr.length; i++){
            LocalDate localDate = arr[i].getDate();
            if(localDate.getYear() != startDate.getYear() || arr.length - i == 1){
                years.put(String.valueOf(startDate.getYear()),values);
                startDate = localDate;
                values = new int[12];
            }
            values[localDate.getMonth().getValue() - 1] = values[localDate.getMonth().getValue() - 1]  + arr[i].getSold();
        }

        return years;
    }

    public static LinkedHashMap<String, int[]> getWeeks() {
        return null;
    }

    public static LinkedHashMap<String, int[]> getMonths() {
        return null;
    }

    public static LinkedHashMap<String, int[]> getYears() {
        return null;
    }
}
