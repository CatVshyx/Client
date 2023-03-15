package com.example.client.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public  class SystemClock extends Thread{
    //class which changes time on labels on current time
    private static final Set<Label> currDates = new HashSet<>();

    private static final SystemClock instance = new SystemClock();
    // TODO check up with updates of data
    private SystemClock(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(900), e -> {
                    String ld = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM d uuuu H:m:s"));
                    currDates.forEach(date -> date.setText(ld));
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    public static void addDate(Label l){
        currDates.add(l);
    }



}
