package com.example.client.ui.smoothCharts;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class SmoothedChartEvent extends Event {
    public static final EventType<SmoothedChartEvent> DATA_SELECTED  = new EventType<>(ANY, "DATA_SELECTED");
    private final double yValue;

    // ******************** Constructors **************************************
    public SmoothedChartEvent(final EventType<SmoothedChartEvent> TYPE, final double Y_VALUE) {
        super(TYPE);
        yValue = Y_VALUE;
    }
    public SmoothedChartEvent(final Object SRC, final EventTarget TARGET, final EventType<SmoothedChartEvent> TYPE, final double Y_VALUE) {
        super(SRC, TARGET, TYPE);
        yValue = Y_VALUE;
    }


    // ******************** Methods *******************************************
    public double getValue() { return yValue; }

}