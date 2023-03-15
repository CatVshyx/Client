package com.example.client.additional;

import com.example.client.HelloApplication;
import javafx.scene.layout.Pane;

public abstract class PopUpUtility {
    // this class provides functionality for Pop up windows in the application
    // for instance ability to move the window inside the main window, to place it in the center of the main Window etc
    // All the PopUp controllers extend this class -> their main panes extend such methods and behaviour
    private Pane pane;
    private double xAxis,yAxis;

    protected void setPane(Pane pane){
        this.pane = pane;
        setRelocatable();
        setPaneAlignment();
    }
    private void setPaneAlignment(){
        pane.setLayoutY( (HelloApplication.SCREEN_HEIGHT - pane.getPrefHeight())/2 );
        pane.setLayoutX( (HelloApplication.SCREEN_WIDTH - pane.getPrefWidth())/2 );
    }
    private void setRelocatable(){
        pane.setOnMousePressed(mouseEvent -> {
            xAxis = mouseEvent.getSceneX() - pane.getTranslateX();
            yAxis = mouseEvent.getSceneY() - pane.getTranslateY();
        });
        pane.setOnMouseDragged(mouseEvent -> {
            pane.setTranslateX(mouseEvent.getSceneX() - xAxis);
            pane.setTranslateY(mouseEvent.getSceneY() - yAxis);
        });
    }
    protected void closeWindow(){
        HelloApplication.getSideBarController().removePane(this.pane);
    }
}
