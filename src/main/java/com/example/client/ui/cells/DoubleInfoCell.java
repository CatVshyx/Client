package com.example.client.ui.cells;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DoubleInfoCell<S> extends TableCell<S,String> {
    private final boolean orientation;
    public DoubleInfoCell(boolean vertical){
        this.orientation = vertical;
    }
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            Pane box = orientation ? new VBox() : new HBox();
            String[] arr = item.split("\n");

            Label l2 = new Label(arr[1]);
            l2.setStyle("-fx-text-fill:grey;");
            box.getChildren().addAll(new Label(arr[0]),l2);

            this.setGraphic(box);
        }
    }
}