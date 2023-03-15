package com.example.client.service;

import com.example.client.HelloApplication;
import com.example.client.model.Date;
import com.example.client.util.Counter;
import com.example.client.util.PDFTest;
import java.util.*;
import java.util.stream.Collectors;

public class FinanceService {
    private static Date[] dates = null;
    private static LinkedHashMap<String,int[]>[] countedArray = new LinkedHashMap[]{Counter.countByWeeks(dates,5),Counter.countByMonths(dates,4),Counter.countByYears(dates,2)};
    public static Date[] getDates( ){
        return dates;
    }
    public static LinkedHashMap<String,int[]> getSpecifiedCountedData(int val){
        if (val >= countedArray.length) return null;
        return countedArray[val];
    }
    public static void clearFinanceData(){
        HelloApplication.getFinanceController().clearData();
        dates = new Date[0];
        countedArray = new LinkedHashMap[0];
    }
    public static void generateFile(LinkedHashMap<String,byte[][]> chartsPanes, String currencyProperty, float rateProperty){
        System.out.println(chartsPanes.size());
        Runnable runnable = () -> {
            PDFTest.setCharts(chartsPanes);
            PDFTest.setData(setStatisticsForDocument(currencyProperty,rateProperty));
            PDFTest.createDocument("C:/Users/Kaiwren/Desktop/test.pdf");
        };
        Thread thread =new Thread(runnable,"creating");
        thread.start();
    }
    private static LinkedHashMap<String ,HashMap<String,int[]>> setStatisticsForDocument(String currency,Float rate){
        LinkedHashMap<String ,HashMap<String,int[]>> returnedMap = new LinkedHashMap<>();
        Arrays.asList(countedArray).forEach(counter -> {
            Map.Entry<String,int[]>[] list = counter.entrySet().toArray(Map.Entry[]::new);
            List<Integer> arrayList = new ArrayList<>();
            counter.values().forEach(localArr -> {
                for(int i = 0; i < localArr.length; i++) {
                    localArr[i] = (int) (localArr[i] * rate);
                    arrayList.add(localArr[i]);
                }}
            );
            IntSummaryStatistics intSummaryStatistics = arrayList.stream().collect(Collectors.summarizingInt(Integer::intValue));
            String statistics = "[%s] THE INFORMATION FROM %s TO %s  \n Statistics during this period \n Maximum: %s \n Average: %s"
                    .formatted(currency,list[0].getKey().split(" ")[0], list[list.length - 1].getKey().split(" ")[0],
                    intSummaryStatistics.getMax(), (float) intSummaryStatistics.getAverage()
                    );
            returnedMap.put(statistics,counter);

        });
        return returnedMap;
    }

    public static void setData(Set<Date> revenues) {
        clearFinanceData();
        if (revenues == null) return;
        List<Date> unsorted = new ArrayList<>(revenues);
        if (unsorted.size() == 0){
            dates = null;
            return;
        }
        unsorted.sort(Comparator.comparing(Date::getDate));
        dates = unsorted.toArray(Date[]::new);
        countedArray = new LinkedHashMap[]{Counter.countByWeeks(dates,5),Counter.countByMonths(dates,4),Counter.countByYears(dates,2)};
        HelloApplication.getFinanceController().setData(dates);
    }
}
