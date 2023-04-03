package com.example.client.ui.cells;

import com.example.client.model.Product;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCell extends TableCell<Product,String> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d");

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            String[] arr = item.split("\n");

            Label t = new Label(LocalDate.parse(arr[0]).format(formatter) + "\n" +LocalDate.parse(arr[1]).format(formatter) ,new Label("From: \n To:"));
            t.setTextFill(Color.GREY);
            t.setContentDisplay(ContentDisplay.LEFT);
            this.setGraphic(t);
        }
    }
}
